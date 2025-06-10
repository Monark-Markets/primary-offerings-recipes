package com.monarkmarkets;

import com.monarkmarkets.api.primary.webapi.model.IndicationOfInterest;
import com.monarkmarkets.api.primary.webapi.model.Investor;
import com.monarkmarkets.api.primary.webapi.model.InvestorSubscription;
import com.monarkmarkets.api.primary.webapi.model.RegisteredFundSubscription;
import lombok.extern.slf4j.Slf4j;

import static com.monarkmarkets.IndicationOfInterestRecipes.submitIndicationOfInterest;
import static com.monarkmarkets.InvestorRecipes.investorOnboarding;
import static com.monarkmarkets.InvestorSubscriptionRecipes.submitInvestorSubscription;
import static com.monarkmarkets.PostCloseAccountViewRecipes.postCloseAccountView;
import static com.monarkmarkets.RegisteredFundRecipes.submitRegisteredFundSubscription;

@Slf4j
public class Recipes {

	public static void main(String[] args) {
		try {
			// Execute Investor Onboarding recipe
			Investor investor = investorOnboarding();
			log.info("Investor: {}", investor);

			// Execute Submission of Indication of Interest
			IndicationOfInterest indicationOfInterest = submitIndicationOfInterest(investor.getInvestorReferenceId());
			log.info("IndicationOfInterest: {}", indicationOfInterest);

			// Execute Investor Subscription
			InvestorSubscription investorSubscription = submitInvestorSubscription(investor.getId());
			log.info("InvestorSubscription: {}", investorSubscription);

			// Execute Post-Close Account View
			postCloseAccountView(investor.getId());
			log.info("Post-Close Account View completed for Investor ID: {}", investor.getId());

			// Execute Registered Fund Subscription
			RegisteredFundSubscription registeredFundSubscription = submitRegisteredFundSubscription(investor.getId());
			log.info("RegisteredFundSubscription: {}", registeredFundSubscription);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.exit(-1);
		}
	}
}
