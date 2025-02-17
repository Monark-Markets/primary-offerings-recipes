package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.document.SignDocument;
import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.investorsubscription.CreateInvestorSubscription;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscriptionAction;
import com.monarkmarkets.dtos.preipocompanyspv.PreIPOCompanySPV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static com.monarkmarkets.InvestorRecipes.investorOnboarding;

public class InvestorSubscriptionRecipes {

	private static final Logger logger = LoggerFactory.getLogger(InvestorSubscriptionRecipes.class);

	/**
	 * Subscription Creation
	 * This flow describes the creation and execution of a Subscription on the primary market. This flow will start once
	 * an Investor inputs the amount of shares and dollars they want to invest and clicks submit, and it will be
	 * standardized regardless of where the Investor started the subscription process (primary market deal page,
	 * PreIPOCompany page with IOI and Subscription functionality, etc).
	**/
	public static void main(String[] args) {
		// Step 1: Create an investor
		Investor investor = investorOnboarding();

		// Step 2: Get a PreIPOCompanySPV
		// here we pick one at random for illustration purposes
		List<PreIPOCompanySPV> preIPOCompanySPVs = getAllPreIPOCompanySPVs(investor);
		PreIPOCompanySPV preIPOCompanySPV = choosePreIPOCompanySPV(preIPOCompanySPVs);
		logger.info("PreIPOCompanySPV: {}", preIPOCompanySPV);

		// Step 3: Create a Subscription for the investor to the PreIPOCompanySPV
		// here we pick one at random for illustration purposes
		CreateInvestorSubscription createInvestorSubscription = CreateInvestorSubscription.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.investorId(investor.getId())
				.amountReservedDollars(preIPOCompanySPV.getRemainingDollarAllocation())
				.amountReservedShares(preIPOCompanySPV.getRemainingShareAllocation())
				.build();
		InvestorSubscription investorSubscription = createInvestorSubscription(createInvestorSubscription);
		logger.info("InvestorSubscription: {}", investorSubscription);

		// Step 4: Get all SubscriptionActions by Subscription
		List<InvestorSubscriptionAction> investorSubscriptionActions =
				getAllInvestorSubscriptionActions(investorSubscription);
		logger.info("InvestorSubscriptionActions: {}", investorSubscriptionActions);

		// Step 5: Sign Documents by Id
		// All InvestorSubscriptionAction that have type=DocumentSign and responsibleParty=Partner
		// will require document signing
		List<InvestorSubscriptionAction> requireSigningSubscriptionActions = investorSubscriptionActions.stream()
				.filter(action ->
						action.getType() == InvestorSubscriptionAction.Type.DocumentSign &&
								action.getResponsibleParty() == InvestorSubscriptionAction.ResponsibleParty.Partner)
				.toList();
		requireSigningSubscriptionActions.forEach(action -> {
			// Get the document by id
			Document document = getDocument(action.getDataId());
			logger.info("Document: {}", document);

			// Sign the document
			signDocument(action.getDataId(), SignDocument.builder()
					.investorId(investor.getId())
					.metadata("Some metadata" + UUID.randomUUID())
					.build());
		});
	}

	private static PreIPOCompanySPV choosePreIPOCompanySPV(List<PreIPOCompanySPV> preIPOCompanySPVS) {
		return preIPOCompanySPVS.stream()
				.filter(preIPOCompanySPV -> preIPOCompanySPV.getRemainingShareAllocation() > 0 &&
						preIPOCompanySPV.getRemainingDollarAllocation() > 0 &&
						preIPOCompanySPV.getNumberOfSeatsRemaining() > 0 &&
						preIPOCompanySPV.getName().equals("Tech Innovations Ltd"))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No PreIPOCompanySPV found with remaining shares ot dollar " +
						"allocation"));
	}

	private static List<PreIPOCompanySPV> getAllPreIPOCompanySPVs(Investor investor) {
		try {
			logger.info("GetAllPreIPOCompanySPVs *****");
			return ApiClient.sendRequest("/primary/v1/pre-ipo-company-spv/investor/" + investor.getId(),
					"GET", null, new TypeReference<>() {
					});
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
			ApiClient.sendRequest("/primary/v1/document/" + documentId + "/sign", "PUT", signDocument);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
