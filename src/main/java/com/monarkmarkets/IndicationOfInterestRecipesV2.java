package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.IndicationOfInterestControllerV2Api;
import com.monarkmarkets.primary.client.api.InvestorApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanyApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateIndicationOfInterestV2;
import com.monarkmarkets.primary.client.model.IndicationOfInterestV2;
import com.monarkmarkets.primary.client.model.Investor;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.PreIPOCompany;
import com.monarkmarkets.primary.client.model.PreIPOCompanyApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class IndicationOfInterestRecipesV2 {

	private static final Random random = new Random();

	private static final InvestorApi investorApi = ApiFactory.getInvestorApi();
	private static final PreIpoCompanyApi preIpoCompanyApi = ApiFactory.getPreIpoCompanyApi();
	private static final IndicationOfInterestControllerV2Api indicationOfInterestV2Api = ApiFactory.getIndicationOfInterestControllerV2Api();

	/**
	 * Submit Indication of Interest using V2 API
	 * Create an indication of interest for an investor to a PreIPO company using the new V2 endpoint
	 *
	 * @param investorReferenceId The reference ID of the investor
	 * @return an IndicationOfInterestV2
	 */
	public static IndicationOfInterestV2 submitIndicationOfInterestV2(String investorReferenceId) {
		// Step 1: Get investor by reference id
		Investor investor = getInvestorByReferenceId(investorReferenceId);

		// Step 2: Get all PreIPO Companies
		List<PreIPOCompany> allPreIPOCompanies = getAllPreIPOCompanies();

		// Check if there are any PreIPO companies available
		if (allPreIPOCompanies.isEmpty()) {
			log.warn("No PreIPO companies available to create indication of interest");
			return null;
		}

		// Step 3: Select one PreIPOCompany from the list,
		// here we pick one at random for illustration purposes
		PreIPOCompany preIPOCompany = allPreIPOCompanies.get(random.nextInt(allPreIPOCompanies.size()));
		log.info("PreIPOCompany: {}", preIPOCompany);

		// Step 4: Create IoI using V2 API
		CreateIndicationOfInterestV2 createIndicationOfInterest = CreateIndicationOfInterestV2.builder()
				.investorId(investor.getId())
				.targetId(preIPOCompany.getId())
				.targetAssetType(CreateIndicationOfInterestV2.TargetAssetTypeEnum.PRE_IPO_COMPANY)
				.notionalAmount(5000.0)
				.currency("USD")
				.build();
		IndicationOfInterestV2 indicationOfInterest = createIndicationOfInterestV2(createIndicationOfInterest);
		log.info("IndicationOfInterest: {}", indicationOfInterest);

		return indicationOfInterest;
	}

	private static Investor getInvestorByReferenceId(String investorReferenceId) {
		try {
			log.info("Getting investor by referenceId: {}", investorReferenceId);
			return investorApi.getInvestorByInvestorReference(investorReferenceId);
		} catch (ApiException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<PreIPOCompany> getAllPreIPOCompanies() {
		try {
			log.info("Fetching all PreIPO companies...");

			// Initialize parameters for pagination
			int pageSize = 100;
			int currentPage = 1;
			int totalPages = Integer.MAX_VALUE; // Placeholder until the total pages are known
			List<PreIPOCompany> allPreIPOCompanies = new ArrayList<>();

			// Loop through pages
			while (currentPage <= totalPages) {
				log.info("Fetching page {} with pageSize {}", currentPage, pageSize);
				PreIPOCompanyApiResponse response = preIpoCompanyApi.getAllPreIPOCompanies(
						currentPage, // current page
						pageSize, // page size
						null, // searchTerm (optional)
						null, // searchCategories (optional)
						"UpdatedAt", // sortBy
						"Descending", // sortOrder
						null, // minLastValuation
						null, // maxLastValuation
						null, // minTotalFunding
						null, // maxTotalFunding
						null, // listingType
						null // isActive
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allPreIPOCompanies.addAll(response.getItems());
				}

				// Get pagination info
				Pagination pagination = response.getPagination();
				if (pagination != null && pagination.getTotalPages() != null) {
					totalPages = pagination.getTotalPages();
					log.info("Total pages: {}", totalPages);
				} else {
					log.warn("Pagination information is missing, stopping iteration.");
					break;
				}

				currentPage++;
			}

			log.info("Successfully fetched {} PreIPO companies.", allPreIPOCompanies.size());
			return allPreIPOCompanies;

		} catch (ApiException e) {
			log.error("Error occurred while fetching PreIPO companies: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to fetch PreIPO companies", e);
		}
	}

	/**
	 * Create an Indication of Interest using the V2 API endpoint
	 *
	 * @param createIndicationOfInterest The IOI creation request
	 * @return The created IndicationOfInterestV2
	 */
	private static IndicationOfInterestV2 createIndicationOfInterestV2(CreateIndicationOfInterestV2 createIndicationOfInterest) {
		try {
			log.info("Create indication of interest (V2): {}", createIndicationOfInterest);

			// Use the proper V2 API
			IndicationOfInterestV2 response = indicationOfInterestV2Api.createIndicationOfInterestV2(createIndicationOfInterest);

			log.info("Successfully created IOI with V2 API. IOI ID: {}", response.getId());
			return response;
		} catch (Exception e) {
			log.error("Error creating indication of interest with V2 API: {}", e.getMessage(), e);
			throw new RuntimeException("Failed to create indication of interest", e);
		}
	}
}
