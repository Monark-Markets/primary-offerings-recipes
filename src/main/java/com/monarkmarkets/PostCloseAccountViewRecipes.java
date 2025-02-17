package com.monarkmarkets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
import com.monarkmarkets.dtos.preipocompany.PreIPOCompany;
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
	public static void main(String[] args) {
		// Step 1: Get InvestorSubscriptions by InvestorId
		// The investorId must be active, had subscribed to investments and had documents uploaded
		Investor investor = getInvestorById(UUID.randomUUID());
		List<InvestorSubscription> investorSubscriptions = getAllInvestorSubscriptions(investor);
		logger.info("Investor subscriptions: {}", investorSubscriptions);

		// Step 2: Get a PreIPOCompanySPV by Id
		List<PreIPOCompanySPV> preIPOCompanySPVs = getAllPreIPOCompanySPVs(investor);
		logger.info("PreIPO company SPVs: {}", preIPOCompanySPVs);

		// Step 3: Get a PreIPOCompany by Id
		preIPOCompanySPVs.forEach(preIPOCompanySPV -> {
			PreIPOCompany preIPOCompany = getPreIPOCompanyById(preIPOCompanySPV, investor);
			logger.info("PreIPOCompany: {}", preIPOCompany);
		});

		// Step 4: Get all Documents
		investorSubscriptions.forEach(investorSubscription -> {
			List<Document> documents = getAllDocuments(investor, investorSubscription);
			logger.info("Documents: {}", documents);
		});
	}

	private static Investor getInvestorById(UUID investorId) {
		try {
			logger.info("GetInvestorById *****");
			return ApiClient.sendRequest("/primary/v1/investor/" + investorId, "GET", null,
					Investor.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<InvestorSubscription> getAllInvestorSubscriptions(Investor investor) {
		try {
			logger.info("GetAllInvestorSubscriptions *****");
			return ApiClient.sendRequest("/primary/v1/investor-subscription/investor/" + investor.getId(),
					"GET", null, new TypeReference<>() { });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	private static PreIPOCompany getPreIPOCompanyById(PreIPOCompanySPV preIPOCompanySPV, Investor investor) {
		try {
			logger.info("GetPreIPOCompanyById *****");
			return ApiClient.sendAdminRequest("/primary/v1/pre-ipo-company/" + preIPOCompanySPV.getPreIPOCompanyId() +
							"/investor/" + investor.getId(), "GET", null, PreIPOCompany.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Document> getAllDocuments(Investor investor, InvestorSubscription investorSubscription) {
		try {
			logger.info("GetAllDocuments *****");
			String endpoint = "/primary/v1/document?investorId=" + investor.getId() + "&investorSubscriptionId=" + investorSubscription.getId();
			return ApiClient.getAllPaged(endpoint, 25, Document.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
