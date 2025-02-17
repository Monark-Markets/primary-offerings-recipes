package com.monarkmarkets.dtos.preipocompanyspv;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePreIPOCompanySPV {

	// --- Required Fields ---

	/**
	 * The unique ID of the PreIPOCompany that the SPV is investing into.
	 */
	private UUID preIPOCompanyInvestmentId;

	/**
	 * Indicates whether this SPV will implement banking via Sydecar.
	 */
	private boolean bankingWithSydecar;

	/**
	 * The unique ID associated with the deal lead.
	 * (maxLength: 100, minLength: 1)
	 */
	private String leadProfileID;

	/**
	 * The name of the SPV.
	 * (maxLength: 100, minLength: 1)
	 */
	private String name;

	/**
	 * The type of asset that will be held by the PreIPOCompanySPV.
	 */
	private AssetType assetType;

	/**
	 * Security type held by an SPV.
	 */
	private SecurityType securityType;

	/**
	 * Indicates whether the SPV will own 20% or more of the total voting power of stock after closing the transaction.
	 */
	private boolean ownStock;

	/**
	 * Gets or sets the name of the platform provider.
	 * (maxLength: 100)
	 */
	private String platformProvider;

	/**
	 * Indicates whether the SPV will be prefunded.
	 */
	private boolean preFundedInventory;

	/**
	 * Gets or sets the tax form type.
	 * (maxLength: 20)
	 */
	private String taxForm;

	/**
	 * Indicates whether the SPV is using the default Sydecar Master LLC.
	 */
	private boolean usingSydecarMasterSeriesLLC;

	// --- Optional Fields ---
	/**
	 * This number describes the total amount of capital that will be raised into the SPV, including fees and expenses.
	 * (Format: double, nullable)
	 */
	private Double totalAllocation;

	/**
	 * The minimum amount that an Investor can invest into an SPV during a primary offering.
	 * (Format: double, nullable)
	 */
	private Double minCommitmentAmount;

	/**
	 * Discount rate if the security type held by the SPV is a SAFE.
	 * (Format: double, nullable)
	 */
	private Double discountRate;

	/**
	 * The round of an SPV.
	 */
	private Round round;

	/**
	 * Valuation type.
	 */
	private ValuationType valuationType;

	/**
	 * Describes the implied valuation of the associated PreIPOCompany.
	 * (Format: double, minimum: 1)
	 */
	private Double valuation;

	/**
	 * Short description of the SPV.
	 * (maxLength: 500, nullable)
	 */
	private String synopsis;

	/**
	 * Longer description of the SPV’s strategy or purpose.
	 * (maxLength: 1000, nullable)
	 */
	private String description;

	/**
	 * A list of notable investors associated with an SPV.
	 */
	private List<String> notableInvestors;

	/**
	 * Indicates whether the SPV offers pro-rata rights.
	 */
	private Boolean proRata;

	/**
	 * Indicates whether the SPV offers information rights.
	 */
	private Boolean informationRights;

	/**
	 * Indicates whether the SPV offers most favored nation (MFN) rights.
	 */
	private Boolean mostFavoredNation;

	/**
	 * A list of notable terms for the SPV.
	 * (maxLength: 1000, nullable)
	 */
	private String otherNotableTerms;

	/**
	 * Full series name for the SPV.
	 * (maxLength: 69, nullable)
	 */
	private String sydecarSeriesName;

	/**
	 * EIN number for the Series LLC entity.
	 * (maxLength: 20, nullable)
	 */
	private String seriesLLCEIN;

	/**
	 * The bank account ID associated with the SPV.
	 * (maxLength: 100, nullable)
	 */
	private String leadBankAccountID;

	/**
	 * Gets or sets the Sydecar Master LLC ID.
	 * (maxLength: 100, nullable)
	 */
	private String sydecarMasterLLCID;

	/**
	 * The EIN number associated with the Master LLC.
	 * (maxLength: 20, nullable)
	 */
	private String masterLLCEIN;

	/**
	 * The name of the Master LLC.
	 * (maxLength: 100, nullable)
	 */
	private String masterLLCName;

	/**
	 * The amount held back for additional costs.
	 * (Format: double, nullable)
	 */
	private Double holdbackAmount;

	/**
	 * The one time, up front management fee charged by the SPV.
	 * (Format: double, nullable)
	 */
	private Double managementFee;

	/**
	 * The number of years over which the management fee will be paid.
	 * (Format: int32, nullable)
	 */
	private Integer managementFeeYearsPayable;

	/**
	 * Funding deadline associated with the primary issuance of an SPV.
	 * (Format: date, nullable)
	 */
	private LocalDate fundingDeadline;

	/**
	 * The exemptions claimed by a PreIPOCompanySPV.
	 */
	private List<ExemptionClaimed> exemptionsClaimed;

	/**
	 * Additional dates related to the SPV.
	 * (Format: date, nullable)
	 */
	private List<LocalDate> dates;

	/**
	 * CUSIP assigned to the SPV.
	 * (maxLength: 9, nullable)
	 */
	private String cusip;

	/**
	 * Symbol assigned to the SPV.
	 * (maxLength: 10, nullable)
	 */
	private String tradingSymbol;

	/**
	 * URL link to a template version of the PreIPOCompanySPV subscription agreement.
	 * (maxLength: 500, nullable)
	 */
	private String subscriptionAgreementUrl;

	/**
	 * A URL link to the operating agreement for a PreIPOCompanySPV.
	 * (maxLength: 500, nullable)
	 */
	private String operatingAgreementUrl;

	/**
	 * The date that the SPV officially closed.
	 * (Format: date-time, nullable)
	 */
	private OffsetDateTime closeDate;

	/**
	 * Custodian where SPV shares are held.
	 * (maxLength: 100, nullable)
	 */
	private String spvCustodian;

	/**
	 * The number of shares in the PreIPOCompany that will be held by the SPV.
	 * (Format: double)
	 */
	private Double preIPOCompanyNumberShares;

	/**
	 * The number of remaining LP positions in an SPV available to Investors.
	 * (Format: int32)
	 */
	private Integer numberOfSeatsRemaining;

	/**
	 * The remaining dollar amount in a primary fundraise of an SPV.
	 * (Format: double)
	 */
	private Double remainingDollarAllocation;

	/**
	 * The remaining number of shares in a primary fundraise of an SPV.
	 * (Format: double)
	 */
	private Double remainingShareAllocation;

	/**
	 * Size, in USD, of the brokerage commission associated with this PreIPOCompanyInvestment.
	 * (Format: double, nullable)
	 */
	private Double totalCommission;

	/**
	 * The percentage of the TotalCommission that will be paid to MMM Securities LLC.
	 * (Format: double, nullable)
	 */
	private Double monarkCommissionSplit;

	/**
	 * The percentage of the TotalCommission that will be paid to the Partner distributing the SPV.
	 * (Format: double, nullable)
	 */
	private Double demandSideCommissionSplit;

	/**
	 * The percentage of the ManagementFee that will be paid to a Partner distributing the SPV, if the Partner is an ERA or RIA.
	 * (Format: double, nullable)
	 */
	private Double demandSideFeeSplit;

	/**
	 * The price per share of the PreIPOCompany, excluding fees, that an investor will pay.
	 * (Format: double, nullable)
	 */
	private Double investorPricePerShare;

	/**
	 * The fee per share of the PreIPOCompany that an Investor will pay on the primary issuance.
	 * (Format: double, nullable)
	 */
	private Double investorFeePerShare;

	/**
	 * The total amount per share that an Investor will pay, including fees.
	 * (Format: double, nullable)
	 */
	private Double allInPricePerShare;

	/**
	 * SPV account ID.
	 * (Format: int64, nullable)
	 */
	private Long spvAccountID;
}
