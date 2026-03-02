package com.monarkmarkets;

import com.monarkmarkets.primary.client.model.IndicationOfInterestV2;
import com.monarkmarkets.primary.client.model.Investor;
import com.monarkmarkets.primary.client.model.Transaction;
import lombok.extern.slf4j.Slf4j;

import static com.monarkmarkets.IndicationOfInterestRecipesV2.submitIndicationOfInterestV2;
import static com.monarkmarkets.InvestorRecipes.investorOnboarding;
import static com.monarkmarkets.InvestorSubscriptionTransactionRecipes.submitAndRejectInvestorSubscription;
import static com.monarkmarkets.InvestorSubscriptionTransactionRecipes.submitInvestorSubscription;
import static com.monarkmarkets.PostCloseAccountViewRecipes.postCloseAccountView;
import static com.monarkmarkets.RegisteredFundTransactionRecipes.submitRegisteredFundSubscription;

@Slf4j
public class Recipes {

	public static void main(String[] args) {
		try {
			runAll(args);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	public static void runAll(String[] args) {
		// Execute Investor Onboarding recipe
		Investor investor = investorOnboarding();
		log.info("Investor: {}", investor);

		// Execute Submission of Indication of Interest
		IndicationOfInterestV2 indicationOfInterest = submitIndicationOfInterestV2(investor.getInvestorReferenceId());

		log.info("IndicationOfInterest: {}", indicationOfInterest);

		// Execute Investor Subscription using new Transaction API
		Transaction investorSubscriptionTransaction = submitInvestorSubscription(investor.getId());
		log.info("InvestorSubscriptionTransaction: {}", investorSubscriptionTransaction);

		// Reject Investor Subscription
		Transaction rejectedInvestorSubscriptionTransaction = submitAndRejectInvestorSubscription(investor.getId());
		log.info("Rejected InvestorSubscriptionTransaction: {}", rejectedInvestorSubscriptionTransaction);

		// Execute Post-Close Account View
		postCloseAccountView(investor.getId());
		log.info("Post-Close Account View completed for Investor ID: {}", investor.getId());

		// Execute Registered Fund Subscription using new Transaction API
		Transaction registeredFundTransaction = submitRegisteredFundSubscription(investor.getId());
		log.info("RegisteredFundTransaction: {}", registeredFundTransaction);
	}
}
