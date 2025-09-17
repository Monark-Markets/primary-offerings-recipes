package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.DocumentApi;
import com.monarkmarkets.primary.client.api.InvestorSubscriptionApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.Document;
import com.monarkmarkets.primary.client.model.DocumentApiResponse;
import com.monarkmarkets.primary.client.model.InvestorSubscription;
import com.monarkmarkets.primary.client.model.Pagination;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PostCloseAccountViewRecipes {

	private static final InvestorSubscriptionApi investorSubscriptionApi = ApiFactory.getInvestorSubscriptionApi();
	private static final DocumentApi documentApi = ApiFactory.getDocumentApi();

	/**
	 * Post-Close Account View
	 * This view describes how a Partner might access and display an Investor’s private company holdings after an SPV(s)
	 * have closed in which the Investor was a Subscriber.
	 */
	public static void postCloseAccountView(UUID investorId) {
		// Step 1: Get InvestorSubscriptions by InvestorId
		// The investorId must be active, had subscribed to investments and had documents uploaded
		List<InvestorSubscription> investorSubscriptions = getAllInvestorSubscriptions(investorId);
		log.info("Investor subscriptions: {}", investorSubscriptions);

		investorSubscriptions.forEach(investorSubscription -> {
			log.info("Getting documents for InvestorSubscription: {}", investorSubscription);

			// Step 2: Get all Documents for the investorSubscription
			List<Document> documents = getAllDocuments(investorId, investorSubscription.getId());
			log.info("InvestorSubscription Documents: {}", documents);
		});
	}

	private static List<InvestorSubscription> getAllInvestorSubscriptions(UUID investorId) {
		try {
			log.info("Get all investor subscriptions: {}", investorId);
			return investorSubscriptionApi.primaryV1InvestorSubscriptionInvestorInvestorIdGet(investorId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Document> getAllDocuments(
			UUID investorId,
			UUID investorSubscriptionId
	) {
		try {
			log.info("Get all documents for investorId {} and investorSubscriptionId: {}", investorId, investorSubscriptionId);

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<Document> allDocuments = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				DocumentApiResponse response = documentApi.primaryV1DocumentGet(
						investorId,
						null,
						null,
						null,
						investorSubscriptionId,
						null,
						null,
						null,
						false,
						currentPage,
						pageSize,
						"Descending" // sortOrder
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allDocuments.addAll(response.getItems());
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

			log.info("Successfully fetched {} documents.", allDocuments.size());
			return allDocuments;

		} catch (ApiException e) {
			log.error("Error occurred while fetching documents: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch documents", e);
		}
	}
}
