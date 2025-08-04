package com.monarkmarkets;

import com.monarkmarkets.SubscriptionCalculator.SubscriptionAmount;
import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.InvestorSubscriptionActionApi;
import com.monarkmarkets.primary.client.api.InvestorSubscriptionApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanySpvApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateInvestorSubscription;
import com.monarkmarkets.primary.client.model.Document;
import com.monarkmarkets.primary.client.model.InvestorSubscription;
import com.monarkmarkets.primary.client.model.InvestorSubscriptionAction;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.PreIPOCompanySPV;
import com.monarkmarkets.primary.client.model.PreIPOCompanySPVApiResponse;
import com.monarkmarkets.primary.client.model.SignDocument;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.monarkmarkets.primary.client.model.PreIPOCompanySPV.MonarkStageEnum.PRIMARY_FUNDRAISE;
import static java.util.concurrent.ThreadLocalRandom.current;

@Slf4j
public class InvestorSubscriptionRecipes {

	private static final PreIpoCompanySpvApi preIpoCompanySpvApi = ApiFactory.getPreIpoCompanySpvApi();
	private static final InvestorSubscriptionApi investorSubscriptionApi = ApiFactory.getInvestorSubscriptionApi();
	private static final InvestorSubscriptionActionApi investorSubscriptionActionApi = ApiFactory.getInvestorSubscriptionActionApi();
	private static final DocumentApi documentApi = ApiFactory.getDocumentApi();
	private static final InvestorSubscriptionActionApi investorSubscriptionActionApi1 = ApiFactory.getInvestorSubscriptionActionApi();

	/**
	 * Subscription Creation
	 * This flow describes the creation and execution of a Subscription on the primary market. This flow will start once
	 * an Investor inputs the amount of shares and dollars they want to invest and clicks submit, and it will be
	 * standardized regardless of where the Investor started the subscription process (primary market deal page,
	 * PreIPOCompany page with IOI and Subscription functionality, etc).
	 **/
	public static InvestorSubscription submitInvestorSubscription(UUID investorId) {
		// Step 1: Get a PreIPOCompanySPV
		// SPVs can only accept Subscriptions from Investors when the MonarkStage field is set to PRIMARY_FUNDRAISE
		List<PreIPOCompanySPV> preIPOCompanySPVs = getAllPreIPOCompanySPVs(investorId);
		PreIPOCompanySPV preIPOCompanySPV = choosePreIPOCompanySPV(preIPOCompanySPVs);
		log.info("PreIPOCompanySPV: {}", preIPOCompanySPV);

		// Step 1.1: Calculate the Subscription Amount based on the subscription rules
		SubscriptionAmount amount = SubscriptionCalculator.calculateSubscriptionAmount(preIPOCompanySPV);

		// Step 2: Create a Subscription for the investor to the PreIPOCompanySPV
		CreateInvestorSubscription createInvestorSubscription = CreateInvestorSubscription.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.investorId(investorId)
				.amountReservedDollars(amount.amountReservedDollars)
				.build();
		InvestorSubscription investorSubscription = createInvestorSubscription(createInvestorSubscription);
		log.info("InvestorSubscription: {}", investorSubscription);

		// Step 3: Get all SubscriptionActions by Subscription
		List<InvestorSubscriptionAction> investorSubscriptionActions =
				getAllInvestorSubscriptionActions(investorSubscription.getId());
		log.info("InvestorSubscriptionActions: {}", investorSubscriptionActions);

		// Step 4: Complete Subscription Actions - Document Acknowledge
		// All InvestorSubscriptionAction that have type=DocumentAcknowledge and responsibleParty=Partner will
		// require document acknowledging
		List<InvestorSubscriptionAction> documentAcknowledgeSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						(action.getType() == InvestorSubscriptionAction.TypeEnum.DOCUMENT_ACKNOWLEDGE) &&
								action.getResponsibleParty() == InvestorSubscriptionAction.ResponsiblePartyEnum.PARTNER)
				.toList();
		documentAcknowledgeSubscriptionActions.forEach(action -> {
			// Acknowledge the document
			completeSubscriptionAction(action.getId());
		});

		// Step 5: Complete Subscription Actions - Text Acknowledge
		// All InvestorSubscriptionAction that have type=TextAcknowledge and responsibleParty=Partner will require
		// text acknowledging
		List<InvestorSubscriptionAction> textAcknowledgeSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						(action.getType() == InvestorSubscriptionAction.TypeEnum.TEXT_ACKNOWLEDGE) &&
								action.getResponsibleParty() == InvestorSubscriptionAction.ResponsiblePartyEnum.PARTNER)
				.toList();
		textAcknowledgeSubscriptionActions.forEach(action -> {
			// Acknowledge the text
			completeSubscriptionAction(action.getId());
		});
	
		// Step 6: Complete Subscription Actions - Sign Documents
		// All InvestorSubscriptionAction that have type=DocumentSign and responsibleParty=Partner
		// will require document signing
		List<InvestorSubscriptionAction> requireSigningSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						action.getType() == InvestorSubscriptionAction.TypeEnum.DOCUMENT_SIGN &&
								action.getResponsibleParty() == InvestorSubscriptionAction.ResponsiblePartyEnum.PARTNER)
				.toList();
		requireSigningSubscriptionActions.forEach(action -> {
			// Get the document by id
			Document document = getDocument(UUID.fromString(action.getDataId()));
			log.info("Document: {}", document);

			// Sign the document
			signDocument(UUID.fromString(action.getDataId()), SignDocument.builder()
					.investorId(investorId)
					.metadata("Some metadata" + UUID.randomUUID())
					.build());
		});

		return investorSubscription;
	}

	private static PreIPOCompanySPV choosePreIPOCompanySPV(List<PreIPOCompanySPV> preIPOCompanySPVS) {
		List<PreIPOCompanySPV> eligibleSPVs = preIPOCompanySPVS.stream()
				.filter(spv -> spv.getRemainingShareAllocation() > 0 &&
						spv.getRemainingDollarAllocation() > 0 &&
						spv.getNumberOfSeatsRemaining() > 0 &&
						spv.getMonarkStage() == PRIMARY_FUNDRAISE)
				.toList();

		if (eligibleSPVs.isEmpty()) {
			throw new RuntimeException("No PreIPOCompanySPV found with remaining shares or dollar allocation");
		}

		return eligibleSPVs.get(current().nextInt(eligibleSPVs.size()));
	}

	private static List<PreIPOCompanySPV> getAllPreIPOCompanySPVs(UUID investorId) {
		try {
			log.info("Fetching all PreIPO company SPVs...");

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<PreIPOCompanySPV> allPreIPOCompanySPVs = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				PreIPOCompanySPVApiResponse response = preIpoCompanySpvApi.primaryV1PreIpoCompanySpvInvestorInvestorIdGet(
						investorId,
						null,
						null,
						null,
						null,
						null,
						currentPage, // current page
						pageSize, // page size
						"Descending", // sortOrder
						"UpdatedAt" // sortBy
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allPreIPOCompanySPVs.addAll(response.getItems());
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

			log.info("Successfully fetched {} PreIPO company SPVs.", allPreIPOCompanySPVs.size());
			return allPreIPOCompanySPVs;

		} catch (ApiException e) {
			log.error("Error occurred while fetching PreIPO company SPVs: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch PreIPO company SPVs", e);
		}
	}

	private static InvestorSubscription createInvestorSubscription(CreateInvestorSubscription createInvestorSubscription) {
		try {
			log.info("Create investor subscription: {}", createInvestorSubscription);
			return investorSubscriptionApi.primaryV1InvestorSubscriptionPost(createInvestorSubscription);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<InvestorSubscriptionAction> getAllInvestorSubscriptionActions(UUID investorSubscriptionId) {
		try {
			log.info("GetAllInvestorSubscriptionActions: {}", investorSubscriptionId);
			return investorSubscriptionActionApi.primaryV1InvestorSubscriptionActionInvestorSubscriptionInvestorSubscriptionIdGet(investorSubscriptionId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Document getDocument(UUID documentId) {
		try {
			log.info("Get document: {}", documentId);
			return documentApi.primaryV1DocumentIdGet(documentId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void signDocument(
			UUID documentId,
			SignDocument signDocument
	) {
		try {
			log.info("Sign document: {}, signDocument: {}", documentId, signDocument);
			documentApi.primaryV1DocumentIdSignPost(documentId, signDocument);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void completeSubscriptionAction(
			UUID subscriptionActionId
	) {
		try {
			log.info("Complete subscription action: {}", subscriptionActionId);
			investorSubscriptionActionApi1.primaryV1InvestorSubscriptionActionIdCompletePut(subscriptionActionId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
