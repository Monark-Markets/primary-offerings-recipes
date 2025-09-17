package com.monarkmarkets;

import com.monarkmarkets.primary.client.api.IndicationOfInterestApi;
import com.monarkmarkets.primary.client.api.InvestorApi;
import com.monarkmarkets.primary.client.api.PreIpoCompanyApi;
import com.monarkmarkets.primary.client.invoker.ApiException;
import com.monarkmarkets.primary.client.model.CreateIndicationOfInterest;
import com.monarkmarkets.primary.client.model.IndicationOfInterest;
import com.monarkmarkets.primary.client.model.Investor;
import com.monarkmarkets.primary.client.model.Pagination;
import com.monarkmarkets.primary.client.model.PreIPOCompany;
import com.monarkmarkets.primary.client.model.PreIPOCompanyApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class IndicationOfInterestRecipes {

	private static final Random random = new Random();

	private static final InvestorApi investorApi = ApiFactory.getInvestorApi();
	private static final PreIpoCompanyApi preIpoCompanyApi = ApiFactory.getPreIpoCompanyApi();
	private static final IndicationOfInterestApi indicationOfInterestApi = ApiFactory.getIndicationOfInterestApi();

	/**
	 * Submit Indication of Interest
	 * Create an indication of interest for an investor to a PreIPO company
	 *
	 * @return an IndicationOfInterest
	 */
	public static IndicationOfInterest submitIndicationOfInterest(String investorReferenceId) {
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

		// Step 4: Create IoI
		CreateIndicationOfInterest createIndicationOfInterest = CreateIndicationOfInterest.builder()
				.investorId(investor.getId())
				.preIPOCompanyId(preIPOCompany.getId())
				.notionalAmount(5000.0)
				.currency("USD")
				.build();
		IndicationOfInterest indicationOfInterest = createIndicationOfInterest(createIndicationOfInterest);
		log.info("IndicationOfInterest: {}", indicationOfInterest);

		return indicationOfInterest;
	}

	private static Investor getInvestorByReferenceId(String investorReferenceId) {
		try {
			log.info("Getting investor by referenceId: {}", investorReferenceId);
			return investorApi.primaryV1InvestorByReferenceInvestorReferenceIdGet(investorReferenceId);
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
				PreIPOCompanyApiResponse response = preIpoCompanyApi.primaryV1PreIpoCompanyGet(
						null, // searchTerm (optional)
						null, // searchCategories (optional)
						"UpdatedAt", // sortBy
						"Descending", // sortOrder
						null, // minValuation
						null, // maxValuation
						null, // minMinimumInvestmentAmount
						null, // maxMinimumInvestmentAmount
						null, // sortType (optional)
						false, // showArchived
						currentPage, // current page
						pageSize // page size
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

	private static IndicationOfInterest createIndicationOfInterest(CreateIndicationOfInterest createIndicationOfInterest) {
		try {
			log.info("Create indication of interest: {}", createIndicationOfInterest);
			return indicationOfInterestApi.primaryV1IndicationOfInterestPost(createIndicationOfInterest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
