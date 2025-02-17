package com.monarkmarkets;

import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.ioi.IndicationOfInterest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.monarkmarkets.IndicationOfInterestRecipes.submitIndicationOfInterest;
import static com.monarkmarkets.InvestorRecipes.investorOnboarding;

public class Recipes {

	private static final Logger logger = LoggerFactory.getLogger(Recipes.class);

	public static void main(String[] args) {
		try {
			// Execute Investor Onboarding recipe
			Investor investor = investorOnboarding();

			// Execute Submission of Indication of Interest
			IndicationOfInterest indicationOfInterest = submitIndicationOfInterest(investor.getInvestorReferenceId());

			// Include future recipes here
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
	}
}
