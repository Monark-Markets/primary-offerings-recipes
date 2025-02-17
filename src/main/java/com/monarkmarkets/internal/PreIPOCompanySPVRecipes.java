package com.monarkmarkets.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.monarkmarkets.ApiClient;
import com.monarkmarkets.dtos.preipocompany.CreatePreIPOCompany;
import com.monarkmarkets.dtos.preipocompany.PreIPOCompany;
import com.monarkmarkets.dtos.preipocompanyinvestment.CreatePreIPOCompanyInvestment;
import com.monarkmarkets.dtos.preipocompanyinvestment.LayeredSPV;
import com.monarkmarkets.dtos.preipocompanyinvestment.PreIPOCompanyInvestment;
import com.monarkmarkets.dtos.preipocompanyinvestment.TransactionType;
import com.monarkmarkets.dtos.preipocompanyspv.AssetType;
import com.monarkmarkets.dtos.preipocompanyspv.CreatePreIPOCompanySPV;
import com.monarkmarkets.dtos.preipocompanyspv.ExemptionClaimed;
import com.monarkmarkets.dtos.preipocompanyspv.MonarkStage;
import com.monarkmarkets.dtos.preipocompanyspv.PreIPOCompanySPV;
import com.monarkmarkets.dtos.preipocompanyspv.PreIPOCompanySPVReviewRequest;
import com.monarkmarkets.dtos.preipocompanyspv.Round;
import com.monarkmarkets.dtos.preipocompanyspv.SecurityType;
import com.monarkmarkets.dtos.preipocompanyspv.ValuationType;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.monarkmarkets.dtos.preipocompany.CompanyType.C_CORPORATION;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class PreIPOCompanySPVRecipes {

	private static final Random random = new Random();
	private static final Logger logger = LoggerFactory.getLogger(PreIPOCompanySPVRecipes.class);

	/**
	 * For internal use only
	 *
	 */
	public static void main(String[] args) {
		// Step 1: Get all PreIPOCompanyInvestment
		List<PreIPOCompanyInvestment> preIPOCompanyInvestments = getAllPreIPOCompanyInvestments();
		logger.info("PreIPOCompanyInvestments: {}", preIPOCompanyInvestments);

		// Step 2: Choose one and create a PreIPOCompanySPV
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
				.withinRange('A', 'Z')
				.filteredBy(LETTERS)
				.get();

		CreatePreIPOCompany createPreIPOCompany = CreatePreIPOCompany.builder()
				.name("TechWave Inc.")
				.domain("https://www.techwave.com")
				.linkedIn("https://www.linkedin.com/company/techwave")
				.x("https://www.twitter.com/techwave")
				.facebook("https://www.facebook.com/techwave")
				.country("United States")
				.address("123 Innovation Drive, Silicon Valley, CA 94043")
				.phoneNumber("5551234567")
				.phoneCountryCode("1")
				.contactName("Paul Davis")
				.contactEmail("support@monark-markets.com")
				.type(C_CORPORATION)
				.yearEst(LocalDate.parse("2010-03-15"))
				.numJobs(120.0)
				.description("TechWave Inc. is a cutting-edge technology company specializing in AI-driven solutions for enterprise software optimization and cybersecurity.")
				.founder("Jane Doe")
				.logoURL("https://www.techwave.com/logo.png")
				.totalFunding(25000000.0)
				.lastFundingSeries("Series B")
				.lastFundingTotal(10000000.0)
				.totalFundingRounds(3.0)
				.lastFundingDate(LocalDate.parse("2023-06-10"))
				.lastFundingType("Equity")
				.numInvestors(15.0)
				.twelveMonthTrailingRevenue(50000000.0)
				.lastValuation(150000000.0)
				.lastSharePrice(15.0)
				.numCustomers(2000.0)
				.numSharesOutstanding(10000000.0)
				.executiveTeam(Arrays.asList(
						"Jane Doe (CEO)",
						"John Smith (CTO)",
						"Emily Clark (CFO)"
				))
				.notableInvestors(Arrays.asList(
						"Sequoia Capital",
						"Andreessen Horowitz"
				))
				.notableInvestorsLogos(Arrays.asList(
						"https://sequoiacap.com/logo.png",
						"https://a16z.com/logo.png"
				))
				.legalName("TechWave Incorporated")
				.jurisdiction("NY")
				.build();

		PreIPOCompany preIPOCompany = createPreIPOCompany(createPreIPOCompany);
		logger.info("PreIPOCompany: {}", preIPOCompany);

		CreatePreIPOCompanyInvestment createPreIPOCompanyInvestment = CreatePreIPOCompanyInvestment.builder()
				.preIPOCompanyID(preIPOCompany.getId())
				.shareType("Preferred")
				.shareClass("Class B")
				.numberOfShares(500000.0)
				.totalCapitalRaise(20000000.0)
				.investmentSize(5000000.0)
				.pricePerShare(40.0)
				.transactionType(TransactionType.PRIMARY)
				.layeredSPV(
						LayeredSPV.builder()
								.layeredSPVName("TechWave SPV Fund")
								.jurisdiction("NY")
								.canDeliverShared(true)
								.managementFee(2.0)
								.managementFeeFrequency("Annually")
								.carriedInterest(20.0)
								.managerNames(List.of("Hello", "Goodbye"))
								.managerType(LayeredSPV.ManagerType.RIA)
								.managerEmail("manager@techwavespv.com")
								.managerPhone("5551234567")
								.gpName("TechWave GP LLC")
								.inceptionDate(LocalDate.parse("2022-01-15"))
								.fundStructure(LayeredSPV.FundStructure.THREE_C_ONE)
								.currentBeneficialOwnershipCount(5)
								.spvOutstandingShareCount(450000.0)
								.lpStakeShareCount(400000.0)
								.lpStakePercentage(90.0)
								.lookThroughProvision(true)
								.offeringCircularUrl("https://www.example.com/offering-circular.pdf")
								.subscriptionAgreementUrl("https://www.example.com/subscription-agreement.pdf")
								.operatingAgreementUrl("https://www.example.com/operating-agreement.pdf")
								.taxForm(LayeredSPV.TaxForm.K1)
								.isCompliant(true)
								.build()
				)
				.build();

		PreIPOCompanyInvestment preIPOCompanyInvestment = createPreIPOCompanyInvestment(createPreIPOCompanyInvestment);
		logger.info("PreIPOCompanyInvestment: {}", preIPOCompanyInvestment);

		CreatePreIPOCompanySPV createPreIPOCompanySPV = CreatePreIPOCompanySPV.builder()
				.preIPOCompanyInvestmentId(preIPOCompanyInvestment.getId())
				.totalAllocation(1000000.0)
				.minCommitmentAmount(50000.0)
				.bankingWithSydecar(false)
				.leadProfileID("0dbbd2c9-e6cc-41b2-895f-92aaf396f83d")
				.name("Tech Innovations Ltd")
				.assetType(AssetType.PRIVATE_COMPANY)
				.discountRate(0.15)
				.securityType(SecurityType.SAFE)
				.round(Round.SERIES_A)
				.ownStock(false)
				.valuationType(ValuationType.POST_MONEY)
				.valuation(5000000.0)
				.platformProvider("Monark")
				.synopsis("Investing in leading tech startups.")
				.description("This SPV focuses on pre-IPO investments in innovative technology companies.")
				.notableInvestors(List.of("Investor A", "Investor B"))
				.proRata(true)
				.informationRights(true)
				.mostFavoredNation(false)
				.otherNotableTerms("Standard VC terms apply.")
				.sydecarSeriesName("Tech Innovationsskjfslkad, a Series of CGF2021 LLC")
				.seriesLLCEIN("12-3456789")
				.leadBankAccountID("bank-67890")
				.usingSydecarMasterSeriesLLC(true)
				.sydecarMasterLLCID(null)
				.masterLLCEIN("98-7654321")
				.masterLLCName("Sydecar Master LLC")
				.holdbackAmount(10000.0)
				.managementFee(20.0)
				.managementFeeYearsPayable(1)
				.fundingDeadline(LocalDate.parse("2025-02-28"))
				.exemptionsClaimed(List.of(ExemptionClaimed.RULE_506_B, ExemptionClaimed.RULE_506_C))
				.dates(List.of(LocalDate.parse("2025-01-16")))
				.cusip("123456789")
				.tradingSymbol(generator.generate(4))
				.subscriptionAgreementUrl("https://example.com/subscription-agreement")
				.operatingAgreementUrl("https://example.com/operating-agreement")
				.closeDate(OffsetDateTime.parse("2025-03-31T23:59:59Z"))
				.spvCustodian("Custodian Inc.")
				.preIPOCompanyNumberShares(10000.0)
				.numberOfSeatsRemaining(10)
				.remainingDollarAllocation(500000.0)
				.remainingShareAllocation(5000.0)
				.preFundedInventory(false)
				.taxForm("K-1")
				.totalCommission(50000.0)
				.monarkCommissionSplit(0.6)
				.demandSideCommissionSplit(0.4)
				.demandSideFeeSplit(0.2)
				.investorPricePerShare(100.0)
				.investorFeePerShare(2.0)
				.allInPricePerShare(102.0)
				.spvAccountID(987654321L)
				.build();
		PreIPOCompanySPV preIPOCompanySPV = createPreIPOCompanySPV(createPreIPOCompanySPV);
		logger.info("PreIPOCompanySPV: {}", preIPOCompanySPV);

		PreIPOCompanySPVReviewRequest reviewRequestWaitingMonarkReview = PreIPOCompanySPVReviewRequest.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.monarkStage(MonarkStage.WAITING_MONARK_REVIEW)
				.build();
		PreIPOCompanySPV preIPOCompanySPVUpdateWaitingMonarkReview = preIPOCompanySPVReviewRequest(reviewRequestWaitingMonarkReview);
		logger.info("PreIPOCompanySPV moved to WAITING_MONARK_REVIEW: {}", preIPOCompanySPVUpdateWaitingMonarkReview);

		PreIPOCompanySPVReviewRequest reviewRequestFundAdminReview = PreIPOCompanySPVReviewRequest.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.monarkStage(MonarkStage.FUND_ADMIN_REVIEW)
				.build();
		PreIPOCompanySPV preIPOCompanySPVUpdateFundAdminReview =
				preIPOCompanySPVReviewRequest(reviewRequestFundAdminReview);
		logger.info("PreIPOCompanySPV moved to FUND_ADMIN_REVIEW: {}", preIPOCompanySPVUpdateFundAdminReview);

		PreIPOCompanySPVReviewRequest reviewRequestPrimaryFundraise = PreIPOCompanySPVReviewRequest.builder()
				.preIPOCompanySPVId(preIPOCompanySPV.getId())
				.monarkStage(MonarkStage.PRIMARY_FUNDRAISE)
				.build();
		PreIPOCompanySPV preIPOCompanySPVUpdatePrimaryFundraise =
				preIPOCompanySPVReviewRequest(reviewRequestPrimaryFundraise);
		logger.info("PreIPOCompanySPV moved to PRIMARY_FUNDRAISE: {}", preIPOCompanySPVUpdatePrimaryFundraise);

		List<PreIPOCompanySPV> allPreIPOCompanySPVs = getAllPreIPOCompanySPVs();
		logger.info("All PreIPOCompanySPVs: {}", allPreIPOCompanySPVs);
	}

	private static List<PreIPOCompanyInvestment> getAllPreIPOCompanyInvestments() {
		try {
			logger.info("GetAllPreIPOCompanyInvestments *****");
			return ApiClient.sendAdminRequest("/primary-internal/v1/pre-ipo-company-investment",
					"GET", null, new TypeReference<>() { });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static PreIPOCompany createPreIPOCompany(CreatePreIPOCompany createPreIPOCompany) {
		try {
			logger.info("CreatePreIPOCompany *****");
			return ApiClient.sendAdminRequest("/primary-internal/v1/pre-ipo-company", "POST",
					createPreIPOCompany, PreIPOCompany.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static PreIPOCompanyInvestment createPreIPOCompanyInvestment(
			CreatePreIPOCompanyInvestment createPreIPOCompanyInvestment
	) {
		try {
			logger.info("CreatePreIPOCompanyInvestment *****");
			return ApiClient.sendAdminRequest("/primary-internal/v1/pre-ipo-company-investment", "POST",
					createPreIPOCompanyInvestment, PreIPOCompanyInvestment.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static PreIPOCompanySPV createPreIPOCompanySPV(CreatePreIPOCompanySPV createPreIPOCompanySPV) {
		try {
			logger.info("CreatePreIPOCompanySPV *****");
			return ApiClient.sendAdminRequest("/primary-internal/v1/pre-ipo-company-spv", "POST",
					createPreIPOCompanySPV, PreIPOCompanySPV.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static PreIPOCompanySPV preIPOCompanySPVReviewRequest(
			PreIPOCompanySPVReviewRequest preIPOCompanySPVReviewRequest
	) {
		try {
			logger.info("PreIPOCompanySPVReviewRequest *****");
			return ApiClient.sendAdminRequest("/primary-internal/v1/pre-ipo-company-spv/review-pipeline",
					"PUT", preIPOCompanySPVReviewRequest, PreIPOCompanySPV.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static List<PreIPOCompanySPV> getAllPreIPOCompanySPVs() {
		try {
			logger.info("GetAllPreIPOCompanySPVs *****");
			return ApiClient.sendRequest("/primary-internal/v1/pre-ipo-company-spv", "GET", null,
					new TypeReference<>() { });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
