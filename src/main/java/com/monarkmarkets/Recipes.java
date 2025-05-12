package com.monarkmarkets;

import com.monarkmarkets.dtos.document.Document;
import com.monarkmarkets.dtos.indicationofinterest.IndicationOfInterest;
import com.monarkmarkets.dtos.investor.Investor;
import com.monarkmarkets.dtos.investorsubscription.InvestorSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.monarkmarkets.IndicationOfInterestRecipes.submitIndicationOfInterest;
import static com.monarkmarkets.InvestorRecipes.investorOnboarding;
import static com.monarkmarkets.InvestorSubscriptionRecipes.submitInvestorSubscription;
import static com.monarkmarkets.PostCloseAccountViewRecipes.postCloseAccountView;

public class Recipes {

	private static final Logger logger = LoggerFactory.getLogger(Recipes.class);

	public static void main(String[] args) {
		try {
			// Execute Investor Onboarding recipe
			Investor investor = investorOnboarding();
			logger.info("Investor: {}", investor);

			// Execute Submission of Indication of Interest
			IndicationOfInterest indicationOfInterest = submitIndicationOfInterest(investor.getInvestorReferenceId());
			logger.info("IndicationOfInterest: {}", indicationOfInterest);

			// Execute Investor Subscription
			InvestorSubscription investorSubscription = submitInvestorSubscription(investor.getId());
			logger.info("InvestorSubscription: {}", investorSubscription);

			// Execute Post-Close Account View
			List<Document> documents = postCloseAccountView(investor.getId(), investorSubscription);
			logger.info("Documents: {}", documents);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}
	}
}
