package com.monarkmarkets;

import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.ioi.CreateIndicationOfInterest;
import com.monarkmarkets.dtos.ioi.IndicationOfInterest;
import com.monarkmarkets.dtos.preipocompany.PreIPOCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class IndicationOfInterestRecipes {

	private static final Random random = new Random();
	private static final Logger logger = LoggerFactory.getLogger(IndicationOfInterestRecipes.class);

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
		List<PreIPOCompany> allPreIPOCompanies = getAllPreIPOCompanies(investor);

		// Step 3: Select one PreIPOCompany from the list,
		// here we pick one at random for illustration purposes
		PreIPOCompany preIPOCompany = allPreIPOCompanies.get(random.nextInt(allPreIPOCompanies.size()));
		logger.info("PreIPOCompany: " + preIPOCompany);

		// Step 4: Create IoI
		CreateIndicationOfInterest createIndicationOfInterest = CreateIndicationOfInterest.builder()
				.investorId(investor.getId())
				.preIPOCompanyId(preIPOCompany.getId())
				.notionalAmount(5000.0)
				.currency("USD")
				.build();
		IndicationOfInterest indicationOfInterest = createIndicationOfInterest(createIndicationOfInterest);
		logger.info("IndicationOfInterest: " + indicationOfInterest);

		return indicationOfInterest;
	}

	private static Investor getInvestorByReferenceId(String investorReferenceId) {
		try {
			logger.info("GetInvestorByReferenceId *****");
			return ApiClient.sendRequest("/primary/v1/investor/by-reference/" + investorReferenceId,
					"GET", null, Investor.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<PreIPOCompany> getAllPreIPOCompanies(Investor investor) {
		try {
			logger.info("GetAllPreIPOCompanies *****");
			String endpoint = "/primary/v1/pre-ipo-company/investor/" + investor.getId().toString();
			return ApiClient.getAllPaged(endpoint, 25, PreIPOCompany.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static IndicationOfInterest createIndicationOfInterest(
			CreateIndicationOfInterest createIndicationOfInterest
	) {
		try {
			logger.info("CreateIndicationOfInterest *****");
			return ApiClient.sendRequest("/primary/v1/indication-of-interest", "POST",
					createIndicationOfInterest, IndicationOfInterest.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
