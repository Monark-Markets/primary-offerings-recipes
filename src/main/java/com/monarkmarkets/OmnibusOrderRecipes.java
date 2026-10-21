package com.monarkmarkets;

import com.monarkmarkets.OmnibusRecipes.OmnibusReferenceData;
import com.monarkmarkets.primary.client.api.AccountControllerV2Api;
import com.monarkmarkets.primary.client.api.OmnibusOrderApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.Account;
import com.monarkmarkets.primary.client.model.AccountApiResponse;
import com.monarkmarkets.primary.client.model.CreateOmnibusOrderRequest;
import com.monarkmarkets.primary.client.model.CreateOmnibusOrderResponse;
import com.monarkmarkets.primary.client.model.Investor;
import com.monarkmarkets.primary.client.model.OmnibusFund;
import com.monarkmarkets.primary.client.model.OmnibusOrderResponse;
import com.monarkmarkets.primary.client.model.OmnibusOrderResponseApiResponse;
import com.monarkmarkets.primary.client.model.OmnibusShareClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Recipes for submitting and inspecting individual Omnibus orders.
 */
@Slf4j
public class OmnibusOrderRecipes {

	private static final int STATUS_POLL_ATTEMPTS = 10;
	private static final long STATUS_POLL_DELAY_MILLIS = 1_000L;
	private static final double DEFAULT_BUY_AMOUNT = 100.00;

	private static final EnumSet<OmnibusOrderResponse.StatusEnum> TERMINAL_STATUSES = EnumSet.of(
			OmnibusOrderResponse.StatusEnum.EXECUTED,
			OmnibusOrderResponse.StatusEnum.SETTLED,
			OmnibusOrderResponse.StatusEnum.CANCELLED,
			OmnibusOrderResponse.StatusEnum.REJECTED,
			OmnibusOrderResponse.StatusEnum.FAILED
	);

	private static final OmnibusOrderApi omnibusOrderApi = ApiFactory.getOmnibusOrderApi();
	private static final AccountControllerV2Api accountApi = ApiFactory.getAccountControllerV2Api();

	/**
	 * Submit two small buy orders for the supplied investor, check each order by ID,
	 * and finally exercise the paginated order lookup.
	 *
	 * <p>The SDK contract requires an account ID. The optional {@code OMNIBUS_ACCOUNT_ID}
	 * selects a specific account, while {@code OMNIBUS_EXTERNAL_ACCOUNT_NUMBER} can
	 * select it by its external account number.</p>
	 */
	public static List<OmnibusOrderResponse> submitAndCheckBuyOrders(
			Investor investor,
			OmnibusReferenceData referenceData
	) {
		if (investor == null || investor.getId() == null) {
			throw new IllegalArgumentException("An investor with an ID is required for Omnibus order recipes.");
		}
		if (referenceData == null || referenceData.funds() == null || referenceData.funds().isEmpty()) {
			log.warn("Skipping Omnibus order recipe: no Omnibus funds are available.");
			return List.of();
		}

		if (investor.getFinancialInstitutionId() == null) {
			log.warn(
					"Skipping Omnibus order recipe: investor {} does not belong to a financial institution.",
					investor.getId());
			return List.of();
		}

		Optional<OmnibusFund> selectedFund = selectFund(referenceData);
		if (selectedFund.isEmpty()) {
			log.warn(
					"Skipping Omnibus order recipe: no unambiguous Omnibus fund was selected. "
							+ "Set OMNIBUS_FUND_ID when more than one fund is visible.");
			return List.of();
		}

		OmnibusFund fund = selectedFund.get();
		log.info("Selected Omnibus fund {}.", fund.getId());

		Optional<OmnibusShareClass> matchingShareClass = referenceData.shareClasses() == null
				? Optional.empty()
				: referenceData.shareClasses().stream()
				.filter(candidate -> fund.getId().equals(candidate.getOmnibusFundId()))
				.findFirst();
		if (matchingShareClass.isEmpty()) {
			log.warn("Skipping Omnibus order recipe: no share class belongs to fund {}.", fund.getId());
			return List.of();
		}

		OmnibusShareClass shareClass = matchingShareClass.get();

		Optional<Account> selectedAccount = selectAccount(investor);
		if (selectedAccount.isEmpty()) {
			log.warn(
					"Skipping Omnibus order recipe: no unambiguous account was selected for investor {}. "
							+ "Set OMNIBUS_ACCOUNT_ID or OMNIBUS_EXTERNAL_ACCOUNT_NUMBER.",
					investor.getId());
			return List.of();
		}
		Account account = selectedAccount.get();
		log.info("Selected account {} for Omnibus orders.", account.getId());

		double baseAmount = minimumBuyAmount(shareClass);
		List<CreateOmnibusOrderRequest> requests = List.of(
				createBuyRequest(account.getId(), fund, shareClass, baseAmount),
				createBuyRequest(account.getId(), fund, shareClass, baseAmount * 1.5)
		);

		List<OmnibusOrderResponse> orders = new ArrayList<>();
		RuntimeException failure = null;
		for (CreateOmnibusOrderRequest request : requests) {
			try {
				CreateOmnibusOrderResponse created = omnibusOrderApi.createOrder(request);
				log.info(
						"Created Omnibus buy order referenceId={}, id={}, status={}, reason={}",
						created.getReferenceId(),
						created.getId(),
						created.getStatus(),
						created.getStatusReasonCode());

				if (created.getId() == null) {
					throw new IllegalStateException(
								"Omnibus order create response did not contain an order ID for "
										+ request.getReferenceId());
				}

				OmnibusOrderResponse order = pollOrderStatus(created.getId());
				orders.add(order);
				if (order.getStatus() == OmnibusOrderResponse.StatusEnum.CANCELLED
						|| order.getStatus() == OmnibusOrderResponse.StatusEnum.REJECTED
						|| order.getStatus() == OmnibusOrderResponse.StatusEnum.FAILED) {
					failure = new IllegalStateException(
								"Omnibus order " + order.getId() + " ended in " + order.getStatus()
										+ ": " + order.getRejectedStatusReasonMessage());
				}
			} catch (ApiException e) {
				throw new RuntimeException(
						"Failed to submit Omnibus order " + request.getReferenceId(), e);
			}
		}

		try {
			OmnibusOrderResponseApiResponse listedOrders = omnibusOrderApi.getOrders(
					1,
					100,
					investor.getId(),
					account.getId(),
					fund.getId(),
					shareClass.getId(),
					null,
					null,
					null,
					null,
					"Buy"
			);
			log.info(
					"Listed {} Omnibus buy orders for investor {} and fund {}.",
					listedOrders.getItems() == null ? 0 : listedOrders.getItems().size(),
					investor.getId(),
					fund.getId());

			Set<UUID> listedOrderIds = listedOrders.getItems() == null
					? Set.of()
					: listedOrders.getItems().stream()
							.map(OmnibusOrderResponse::getId)
							.collect(Collectors.toSet());
			Set<UUID> submittedOrderIds = orders.stream()
					.map(OmnibusOrderResponse::getId)
					.collect(Collectors.toSet());
			if (!listedOrderIds.containsAll(submittedOrderIds)) {
				throw new IllegalStateException(
						"Omnibus order list did not contain all submitted order IDs. Submitted: "
								+ submittedOrderIds + ", listed: " + listedOrderIds);
			}
		} catch (ApiException e) {
			throw new RuntimeException("Failed to list Omnibus buy orders", e);
		}

		if (failure != null) {
			throw failure;
		}

		return orders;
	}

	private static Optional<OmnibusFund> selectFund(OmnibusReferenceData referenceData) {
		String configuredFundId = Config.getEnv("OMNIBUS_FUND_ID", null);
		if (configuredFundId != null && !configuredFundId.isBlank()) {
			UUID fundId;
			try {
				fundId = UUID.fromString(configuredFundId);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("OMNIBUS_FUND_ID must be a valid UUID.", e);
			}

			return referenceData.funds().stream()
					.filter(candidate -> fundId.equals(candidate.getId()))
					.findFirst();
		}

		return referenceData.funds().size() == 1
				? Optional.of(referenceData.funds().get(0))
				: Optional.empty();
	}

	private static Optional<Account> selectAccount(Investor investor) {
		try {
			AccountApiResponse response = accountApi.getAccounts(1, 100, investor.getId());
			List<Account> accounts = response.getItems() == null
					? List.of()
					: response.getItems().stream()
							.filter(account -> account.getId() != null)
							.toList();

			String configuredAccountId = Config.getEnv("OMNIBUS_ACCOUNT_ID", null);
			if (configuredAccountId != null && !configuredAccountId.isBlank()) {
				UUID accountId;
				try {
					accountId = UUID.fromString(configuredAccountId);
				} catch (IllegalArgumentException e) {
					throw new IllegalStateException("OMNIBUS_ACCOUNT_ID must be a valid UUID.", e);
				}

				return accounts.stream()
						.filter(account -> accountId.equals(account.getId()))
						.findFirst();
			}

			String configuredExternalAccountNumber = Config.getEnv("OMNIBUS_EXTERNAL_ACCOUNT_NUMBER", null);
			if (configuredExternalAccountNumber != null && !configuredExternalAccountNumber.isBlank()) {
				return accounts.stream()
						.filter(account -> configuredExternalAccountNumber.equals(account.getExternalAccountNumber()))
						.findFirst();
			}

			if (accounts.size() == 1) {
				return Optional.of(accounts.get(0));
			}

			if (investor.getInvestorReferenceId() != null && !investor.getInvestorReferenceId().isBlank()) {
				return accounts.stream()
						.filter(account -> investor.getInvestorReferenceId().equals(account.getExternalAccountNumber()))
						.findFirst();
			}

			return Optional.empty();
		} catch (ApiException e) {
			throw new RuntimeException("Failed to fetch accounts for investor " + investor.getId(), e);
		}
	}

	// Both orders go to the same share class, so the second may be checked against the
	// subsequent minimum; use the larger of the two so either order clears validation.
	private static double minimumBuyAmount(OmnibusShareClass shareClass) {
		double minimum = Math.max(
				Optional.ofNullable(shareClass.getInitialMinimumInvestment()).orElse(0.0),
				Optional.ofNullable(shareClass.getSubsequentMinimumInvestment()).orElse(0.0));
		return minimum > 0 ? minimum : DEFAULT_BUY_AMOUNT;
	}

	private static CreateOmnibusOrderRequest createBuyRequest(
			UUID accountId,
			OmnibusFund fund,
			OmnibusShareClass shareClass,
			double notionalAmount
	) {
		return CreateOmnibusOrderRequest.builder()
				.referenceId("recipes-omnibus-buy-" + UUID.randomUUID())
				.accountId(accountId)
				.omnibusFundId(fund.getId())
				.omnibusShareClassId(shareClass.getId())
				.side(CreateOmnibusOrderRequest.SideEnum.BUY)
				.notionalAmount(notionalAmount)
				.currency("USD")
				.distributionElection(CreateOmnibusOrderRequest.DistributionElectionEnum.CASH)
				.taxLotElection(CreateOmnibusOrderRequest.TaxLotElectionEnum.FIFO)
				.build();
	}

	private static OmnibusOrderResponse pollOrderStatus(UUID orderId) {
		OmnibusOrderResponse latest = null;
		try {
			for (int attempt = 1; attempt <= STATUS_POLL_ATTEMPTS; attempt++) {
				latest = omnibusOrderApi.getOrderById(orderId);
				log.info(
						"Omnibus order status check {}/{}: id={}, status={}, reason={}",
						attempt,
						STATUS_POLL_ATTEMPTS,
						latest.getId(),
						latest.getStatus(),
						latest.getStatusReasonCode());

				if (TERMINAL_STATUSES.contains(latest.getStatus())) {
					return latest;
				}

				if (attempt < STATUS_POLL_ATTEMPTS) {
					Thread.sleep(STATUS_POLL_DELAY_MILLIS);
				}
			}
		} catch (ApiException e) {
			throw new RuntimeException("Failed to fetch Omnibus order status for " + orderId, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted while polling Omnibus order status for " + orderId, e);
		}

		log.warn(
				"Omnibus order {} did not reach a terminal status after {} checks; latest status={}",
				orderId,
				STATUS_POLL_ATTEMPTS,
				latest == null ? null : latest.getStatus());
		return latest;
	}
}
