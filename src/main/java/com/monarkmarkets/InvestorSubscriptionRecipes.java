package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.document.SignDocument;
import com.monarkmarkets.dtos.investorsubscription.CreateInvestorSubscription;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscriptionAction;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscriptionActionType;
import com.monarkmarkets.dtos.investorsubscription.ResponsibleParty;
import com.monarkmarkets.dtos.preipocompanyspv.PreIPOCompanySPV;
import com.monarkmarkets.dtos.preipocompanyspv.SubscriptionCalculator;
import com.monarkmarkets.dtos.preipocompanyspv.SubscriptionCalculator.SubscriptionAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static com.monarkmarkets.dtos.preipocompanyspv.MonarkStage.PRIMARY_FUNDRAISE;

public class InvestorSubscriptionRecipes {

	private static final Logger logger = LoggerFactory.getLogger(InvestorSubscriptionRecipes.class);

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
		logger.info("PreIPOCompanySPV: {}", preIPOCompanySPV);

		// Step 1.1: Calculate the Subscription Amount based on the subscription rules
		SubscriptionAmount amount = SubscriptionCalculator.calculateSubscription(preIPOCompanySPV);

		// Step 2: Create a Subscription for the investor to the PreIPOCompanySPV
		CreateInvestorSubscription createInvestorSubscription = CreateInvestorSubscription.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.investorId(investorId)
				.amountReservedDollars(amount.amountReservedDollars)
				.amountReservedShares(amount.amountReservedShares)
				.build();
		InvestorSubscription investorSubscription = createInvestorSubscription(createInvestorSubscription);
		logger.info("InvestorSubscription: {}", investorSubscription);

		// Step 3: Get all SubscriptionActions by Subscription
		List<InvestorSubscriptionAction> investorSubscriptionActions =
				getAllInvestorSubscriptionActions(investorSubscription);
		logger.info("InvestorSubscriptionActions: {}", investorSubscriptionActions);

		// Step 4: Sign Documents by Id
		// All InvestorSubscriptionAction that have type=DocumentSign and responsibleParty=Partner
		// will require document signing
		List<InvestorSubscriptionAction> requireSigningSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						action.getType() == InvestorSubscriptionActionType.DocumentSign &&
								action.getResponsibleParty() == ResponsibleParty.Partner)
				.toList();
		requireSigningSubscriptionActions.forEach(action -> {
			// Get the document by id
			Document document = getDocument(action.getDataId());
			logger.info("Document: {}", document);

			// Sign the document
			signDocument(action.getDataId(), SignDocument.builder()
					.investorId(investorId)
					.metadata("Some metadata" + UUID.randomUUID())
					.build());
		});

		// Step 5: Document Acknowledge by Id
		// All InvestorSubscriptionAction that have type=DocumentAcknowledge or type=TextAcknowledge and
		// responsibleParty=Partner will require document signing
		List<InvestorSubscriptionAction> documentAcknowlwdgeSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						(action.getType() == InvestorSubscriptionActionType.DocumentAcknowledge ||
								action.getType() == InvestorSubscriptionActionType.TextAcknowledge) &&
								action.getResponsibleParty() == ResponsibleParty.Partner)
				.toList();
		documentAcknowlwdgeSubscriptionActions.forEach(action -> {
			// Complete the document
			completeSubscriptionAction(action.getId().toString());
		});

		return investorSubscription;
	}

	private static PreIPOCompanySPV choosePreIPOCompanySPV(List<PreIPOCompanySPV> preIPOCompanySPVS) {
		return preIPOCompanySPVS.stream()
				.filter(preIPOCompanySPV -> preIPOCompanySPV.getRemainingShareAllocation() > 0 &&
						preIPOCompanySPV.getRemainingDollarAllocation() > 0 &&
						preIPOCompanySPV.getNumberOfSeatsRemaining() > 0 &&
						preIPOCompanySPV.getMonarkStage() == PRIMARY_FUNDRAISE)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No PreIPOCompanySPV found with remaining shares or dollar " +
						"allocation"));
	}

	private static List<PreIPOCompanySPV> getAllPreIPOCompanySPVs(UUID investorId) {
		try {
			logger.info("GetAllPreIPOCompanySPVs *****");
			return ApiClient.getAllPaged("/primary/v1/pre-ipo-company-spv/investor/" + investorId,
					25, PreIPOCompanySPV.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static InvestorSubscription createInvestorSubscription(CreateInvestorSubscription createInvestorSubscription) {
		try {
			logger.info("CreateInvestorSubscription *****");
			return ApiClient.sendRequest("/primary/v1/investor-subscription", "POST",
					createInvestorSubscription, InvestorSubscription.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<InvestorSubscriptionAction> getAllInvestorSubscriptionActions(InvestorSubscription investorSubscription) {
		try {
			logger.info("GetAllInvestorSubscriptionActions *****");
			return ApiClient.sendRequest("/primary/v1/investor-subscription-action/investor-subscription/" +
							investorSubscription.getId(), "GET", null,
					new TypeReference<>() {
					});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Document getDocument(String documentId) {
		try {
			logger.info("GetDocumentById *****");
			return ApiClient.sendRequest("/primary/v1/document/" + documentId, "GET", null,
					Document.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void signDocument(
			String documentId,
			SignDocument signDocument
	) {
		try {
			logger.info("SignDocument *****");
			ApiClient.sendRequest("/primary/v1/document/" + documentId + "/sign", "POST", signDocument);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void completeSubscriptionAction(
			String subscriptionActionId
	) {
		try {
			logger.info("CompleteDocument *****");
			ApiClient.sendRequest("/primary/v1/investor-subscription-action/" + subscriptionActionId + "/complete", "PUT", null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
