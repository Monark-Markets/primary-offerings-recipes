package com.monarkmarkets;

import com.monarkmarkets.OmnibusRecipes.OmnibusReferenceData;
import com.monarkmarkets.primary.client.api.OmnibusOrderApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
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

	private static final EnumSet<OmnibusOrderResponse.StatusEnum> TERMINAL_STATUSES = EnumSet.of(
			OmnibusOrderResponse.StatusEnum.EXECUTED,
			OmnibusOrderResponse.StatusEnum.SETTLED,
			OmnibusOrderResponse.StatusEnum.CANCELLED,
			OmnibusOrderResponse.StatusEnum.REJECTED,
			OmnibusOrderResponse.StatusEnum.FAILED
	);

	private static final OmnibusOrderApi omnibusOrderApi = ApiFactory.getOmnibusOrderApi();

	/**
	 * Submit two small buy orders for the supplied investor, check each order by ID,
	 * and finally exercise the paginated order lookup.
	 *
	 * <p>The SDK contract requires an external account number. The optional
	 * {@code OMNIBUS_EXTERNAL_ACCOUNT_NUMBER} override is useful when the sandbox
	 * has a preconfigured account; otherwise the investor reference is used.</p>
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

		String externalAccountNumber = Config.getEnv(
				"OMNIBUS_EXTERNAL_ACCOUNT_NUMBER",
				investor.getInvestorReferenceId());
		if (externalAccountNumber == null || externalAccountNumber.isBlank()) {
			throw new IllegalStateException(
						"OMNIBUS_EXTERNAL_ACCOUNT_NUMBER or investorReferenceId is required for Omnibus orders.");
		}

		List<CreateOmnibusOrderRequest> requests = List.of(
				createBuyRequest(investor.getId(), externalAccountNumber, fund, shareClass, 100.00),
				createBuyRequest(investor.getId(), externalAccountNumber, fund, shareClass, 150.00)
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

	private static CreateOmnibusOrderRequest createBuyRequest(
			UUID investorId,
			String externalAccountNumber,
			OmnibusFund fund,
			OmnibusShareClass shareClass,
			double notionalAmount
	) {
		return CreateOmnibusOrderRequest.builder()
				.referenceId("recipes-omnibus-buy-" + UUID.randomUUID())
				.externalAccountNumber(externalAccountNumber)
				.investorId(investorId)
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
