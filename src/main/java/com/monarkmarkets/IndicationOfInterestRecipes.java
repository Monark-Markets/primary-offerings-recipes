package com.monarkmarkets;

import com.monarkmarkets.api.primary.webapi.api.IndicationOfInterestApi;
import com.monarkmarkets.api.primary.webapi.api.InvestorApi;
import com.monarkmarkets.api.primary.webapi.api.PreIpoCompanyApi;
import com.monarkmarkets.api.primary.webapi.invoker.ApiException;
import com.monarkmarkets.api.primary.webapi.model.CreateIndicationOfInterest;
import com.monarkmarkets.api.primary.webapi.model.IndicationOfInterest;
import com.monarkmarkets.api.primary.webapi.model.Investor;
import com.monarkmarkets.api.primary.webapi.model.Pagination;
import com.monarkmarkets.api.primary.webapi.model.PreIPOCompany;
import com.monarkmarkets.api.primary.webapi.model.PreIPOCompanyApiResponse;
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
						null,
						null,
						null,
						null,
						currentPage, // current page
						pageSize // page size
				);

				// Extract items and append to the result list
				if (response.getItems() != null) {
					allPreIPOCompanies.addAll(response.getItems());
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
