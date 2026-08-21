package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.OmnibusFundApi;
import com.monarkmarkets.primary.client.api.OmnibusShareClassApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.OmnibusFund;
import com.monarkmarkets.primary.client.model.OmnibusFundApiResponse;
import com.monarkmarkets.primary.client.model.OmnibusShareClass;
import com.monarkmarkets.primary.client.model.OmnibusShareClassApiResponse;
import com.monarkmarkets.primary.client.model.Pagination;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OmnibusRecipes {

	private static final int PAGE_SIZE = 1000;

	private static final OmnibusFundApi omnibusFundApi = ApiFactory.getOmnibusFundApi();
	private static final OmnibusShareClassApi omnibusShareClassApi = ApiFactory.getOmnibusShareClassApi();

	public record OmnibusReferenceData(
			List<OmnibusFund> funds,
			List<OmnibusShareClass> shareClasses
	) {
	}

	/**
	 * Fetch all Omnibus funds and share classes visible to the configured partner.
	 */
	public static OmnibusReferenceData fetchOmnibusFundsAndShareClasses() {
		List<OmnibusFund> omnibusFunds = getAllOmnibusFunds();
		List<OmnibusShareClass> omnibusShareClasses = getAllOmnibusShareClasses();

		log.info("Fetched {} Omnibus funds: {}", omnibusFunds.size(), omnibusFunds);
		log.info("Fetched {} Omnibus share classes: {}", omnibusShareClasses.size(), omnibusShareClasses);

		return new OmnibusReferenceData(omnibusFunds, omnibusShareClasses);
	}

	private static List<OmnibusFund> getAllOmnibusFunds() {
		try {
			List<OmnibusFund> allOmnibusFunds = new ArrayList<>();
			int currentPage = 1;

			while (true) {
				log.info("Fetching Omnibus funds page {} with pageSize {}", currentPage, PAGE_SIZE);
				OmnibusFundApiResponse response = omnibusFundApi.getAllOmnibusFunds(
						currentPage,
						PAGE_SIZE,
						null,
						null,
						null
				);

				if (response.getItems() != null) {
					allOmnibusFunds.addAll(response.getItems());
				}

				if (!hasNextPage(response.getPagination(), currentPage)) {
					return allOmnibusFunds;
				}

				currentPage++;
			}
		} catch (ApiException e) {
			log.error("Error occurred while fetching Omnibus funds: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch Omnibus funds", e);
		}
	}

	private static List<OmnibusShareClass> getAllOmnibusShareClasses() {
		try {
			List<OmnibusShareClass> allOmnibusShareClasses = new ArrayList<>();
			int currentPage = 1;

			while (true) {
				log.info("Fetching Omnibus share classes page {} with pageSize {}", currentPage, PAGE_SIZE);
				OmnibusShareClassApiResponse response = omnibusShareClassApi.getAllOmnibusShareClasses(
						currentPage,
						PAGE_SIZE,
						null,
						null,
						null
				);

				if (response.getItems() != null) {
					allOmnibusShareClasses.addAll(response.getItems());
				}

				if (!hasNextPage(response.getPagination(), currentPage)) {
					return allOmnibusShareClasses;
				}

				currentPage++;
			}
		} catch (ApiException e) {
			log.error("Error occurred while fetching Omnibus share classes: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch Omnibus share classes", e);
		}
	}

	private static boolean hasNextPage(Pagination pagination, int currentPage) {
		return pagination != null
				&& pagination.getTotalPages() != null
				&& currentPage < pagination.getTotalPages();
	}
}
