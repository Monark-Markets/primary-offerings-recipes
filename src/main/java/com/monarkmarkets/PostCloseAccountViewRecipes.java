package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
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
	public static void postCloseAccountView(UUID investorId) {
		// Step 1: Get InvestorSubscriptions by InvestorId
		// The investorId must be active, had subscribed to investments and had documents uploaded
		List<InvestorSubscription> investorSubscriptions = getAllInvestorSubscriptions(investorId);
		logger.info("Investor subscriptions: {}", investorSubscriptions);

		investorSubscriptions.forEach(investorSubscription -> {
			logger.info("Getting documents for InvestorSubscription: {}", investorSubscription);

			// Step 2: Get all Documents for the investorSubscription
			List<Document> documents = getAllDocuments(investorId, investorSubscription);
			logger.info("InvestorSubscription Documents: {}", documents);
		});
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
