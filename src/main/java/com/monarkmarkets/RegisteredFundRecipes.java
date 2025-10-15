package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.RegisteredFundApi;
import com.monarkmarkets.primary.client.api.RegisteredFundSubscriptionActionApi;
import com.monarkmarkets.primary.client.api.RegisteredFundSubscriptionApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateRegisteredFundSubscription;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.RegisteredFund;
import com.monarkmarkets.primary.client.model.RegisteredFundApiResponse;
import com.monarkmarkets.primary.client.model.RegisteredFundSubscription;
import com.monarkmarkets.primary.client.model.RegisteredFundSubscriptionAction;
import com.monarkmarkets.primary.client.model.SignatureRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class RegisteredFundRecipes {

	private static final RegisteredFundApi registeredFundApi = ApiFactory.getRegisteredFundApi();
	private static final RegisteredFundSubscriptionApi registeredFundSubscriptionApi = ApiFactory.getRegisteredFundSubscriptionApi();
	private static final RegisteredFundSubscriptionActionApi registeredFundSubscriptionActionApi = ApiFactory.getRegisteredFundSubscriptionActionApi();

	public static RegisteredFundSubscription submitRegisteredFundSubscription(UUID investorId) {
		// Get all registered funds and select one at random
		List<RegisteredFund> registeredFunds = getAllRegisteredFunds();
		RegisteredFund registeredFund = registeredFunds.stream()
				.filter(rf -> "BCRED".equals(rf.getSymbol()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No Registered Fund found with symbol BCRED"));
		log.info("Selected Registered Fund: {}", registeredFund);

		// Define the subscription amount (for example, $10,000.00)
		final double subscriptionAmount = 10000.00;

		// Create the request object
		CreateRegisteredFundSubscription createRegisteredFundSubscription = CreateRegisteredFundSubscription.builder()
				.registeredFundId(registeredFund.getId())  // Use the selected registered fund's ID
				.investorId(investorId)                    // Use the provided investor ID
				.subscriptionAmount(subscriptionAmount)    // Specify the subscription amount
				.build();

		// Make the API call to create the fund subscription
		RegisteredFundSubscription registeredFundSubscription = createRegisteredFundSubscription(createRegisteredFundSubscription);
		log.info("Successfully created Fund Subscription: {}", registeredFundSubscription);

		// Call the API to generate the subscription preview
		UUID subscriptionId = registeredFundSubscription.getId();
		File subscriptionPreviewPdf = createSubscriptionPreviewPdf(subscriptionId);
		log.info("Successfully generated Fund Subscription preview. PDF location: {}", subscriptionPreviewPdf.getAbsolutePath());

		// Get all the registered fund subscription actions
		List<RegisteredFundSubscriptionAction> registeredFundSubscriptionActions =
				getAllRegisteredFundSubscriptionActions(registeredFundSubscription.getId());
		log.info("RegisteredFundSubscriptionActions: {}", registeredFundSubscriptionActions);

		// Complete the registered fund subscription actions
		registeredFundSubscriptionActions.forEach(action -> {
			// Complete each action
			completeRegisteredFundSubscriptionAction(action.getId());
		});

		// Investor Sign subscription and Advisor sign subscription
		SignatureRequest investorSignatureRequest = SignatureRequest.builder()
				.signatureName("John Doe") // Replace with the actual investor's name
				.build();

		// Call the API for the Investor sign
		RegisteredFundSubscription registeredFundSubscriptionInvestorSigned =
				sendRegisteredFundSubscriptionInvestorSigned(subscriptionId, investorSignatureRequest);
		log.info("Investor signing completed for subscription: {}", registeredFundSubscriptionInvestorSigned);

		// Create the request body for the advisor signature
		SignatureRequest advisorSignatureRequest = SignatureRequest.builder()
				.signatureName("Jane Advisor") // Replace with the actual advisor's name
				.build();

		// Call the API for the Advisor sign
		RegisteredFundSubscription registeredFundSubscriptionAdvisorSigned =
				sendRegisteredFundSubscriptionAdvisorSigned(subscriptionId, advisorSignatureRequest);
		log.info("Advisor signing completed for subscription : {}", registeredFundSubscriptionAdvisorSigned);

		return registeredFundSubscriptionAdvisorSigned;
	}

	private static List<RegisteredFund> getAllRegisteredFunds() {
		try {
			log.info("Get all registered funds");

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<RegisteredFund> allRegisteredFunds = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				RegisteredFundApiResponse response
						= registeredFundApi.getAllRegisteredFunds(
						currentPage,
						pageSize,
						null,
						null
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allRegisteredFunds.addAll(response.getItems());
				}

				// Get pagination info
				Pagination pagination = response.getPagination();
				if (pagination != null) {
					totalPages = pagination.getTotalPages();
					log.info("Total pages: {}", totalPages);
				} else {
					log.warn("Pagination information is missing, stopping iteration.");
					break;
				}

				currentPage++;
			}

			log.info("Successfully fetched {} registered funds.", allRegisteredFunds.size());
			return allRegisteredFunds;

		} catch (ApiException e) {
			log.error("Error occurred while fetching registered funds: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch registered funds", e);
		}
	}

	private static RegisteredFundSubscription createRegisteredFundSubscription(
			CreateRegisteredFundSubscription createRegisteredFundSubscription
	) {
		try {
			log.info("Create Registered Fund Subscription: {}", createRegisteredFundSubscription);
			return registeredFundSubscriptionApi
					.createRegisteredFundSubscription(createRegisteredFundSubscription);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<RegisteredFundSubscriptionAction> getAllRegisteredFundSubscriptionActions(UUID registeredFundSubscriptionId) {
		try {
			log.info("GetAllRegisteredFundSubscriptionActions: {}", registeredFundSubscriptionId);
			return registeredFundSubscriptionActionApi.getAllRegisteredFundSubscriptionActions(
					registeredFundSubscriptionId, null);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static void completeRegisteredFundSubscriptionAction(
			UUID registeredFundSubscriptionActionId
	) {
		try {
			log.info("Complete registered fund subscription action: {}", registeredFundSubscriptionActionId);
			registeredFundSubscriptionActionApi.completeRegisteredFundSubscriptionAction(
					registeredFundSubscriptionActionId, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static File createSubscriptionPreviewPdf(UUID subscriptionId) {
		try {
			log.info("Creating subscription preview PDF for subscriptionId: {}", subscriptionId);
			return registeredFundSubscriptionApi.generateRegisteredFundSubscriptionPreview(subscriptionId, null);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static RegisteredFundSubscription sendRegisteredFundSubscriptionInvestorSigned(
			UUID subscriptionId,
			SignatureRequest investorSignatureRequest
	) {
		try {
			log.info("Sending Registered Fund Subscription Investor Signed for subscriptionId: {}", subscriptionId);
			return registeredFundSubscriptionApi.signRegisteredFundSubscriptionByInvestor(subscriptionId, investorSignatureRequest);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static RegisteredFundSubscription sendRegisteredFundSubscriptionAdvisorSigned(
			UUID subscriptionId,
			SignatureRequest advisorSignatureRequest
	) {
		try {
			log.info("Sending Registered Fund Subscription Advisor Signed for subscriptionId: {}", subscriptionId);
			return registeredFundSubscriptionApi.signRegisteredFundSubscriptionByAdvisor(subscriptionId, advisorSignatureRequest);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}
}
