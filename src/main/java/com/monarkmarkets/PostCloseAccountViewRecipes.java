package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
import com.monarkmarkets.dtos.preipocompanyspv.PreIPOCompanySPV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class PostCloseAccountViewRecipes {

	private static final Logger logger = LoggerFactory.getLogger(PostCloseAccountViewRecipes.class);

	/**
	 * Post-Close Account View
	 * This view describes how a Partner might access and display an Investor’s private company holdings after an SPV(s)
	 * have closed in which the Investor was a Subscriber.
	 */
	public static List<Document> postCloseAccountView(
			UUID investorId,
			InvestorSubscription investorSubscription
	) {
		// Step 1: Get InvestorSubscriptions by InvestorId
		// The investorId must be active, had subscribed to investments and had documents uploaded
		List<InvestorSubscription> investorSubscriptions = getAllInvestorSubscriptions(investorId);
		logger.info("Investor subscriptions: {}", investorSubscriptions);

		// Step 2: Get a PreIPOCompanySPV by Id
		List<PreIPOCompanySPV> preIPOCompanySPVs = getAllPreIPOCompanySPVs(investorId);
		logger.info("PreIPO company SPVs: {}", preIPOCompanySPVs);

		// Step 4: Get all Documents
		List<Document> documents = getAllDocuments(investorId, investorSubscription);
		logger.info("Documents: {}", documents);

		return documents;
	}

	private static List<InvestorSubscription> getAllInvestorSubscriptions(UUID investorId) {
		try {
			logger.info("GetAllInvestorSubscriptions *****");
			return ApiClient.sendRequest("/primary/v1/investor-subscription/investor/" + investorId,
					"GET", null, new TypeReference<>() {
					});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private static List<Document> getAllDocuments(
			UUID investorId,
			InvestorSubscription investorSubscription
	) {
		try {
			logger.info("GetAllDocuments *****");
			String endpoint = "/primary/v1/document?investorId=" + investorId + "&investorSubscriptionId=" + investorSubscription.getId();
			return ApiClient.getAllPaged(endpoint, 25, Document.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
