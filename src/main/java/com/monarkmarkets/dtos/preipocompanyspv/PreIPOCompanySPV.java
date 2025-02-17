package com.monarkmarkets.dtos.preipocompanyspv;

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
public class PreIPOCompanySPV {

	// ========= Required Fields =========

	/**
	 * Unique ID associated with an SPV.
	 * (format: uuid)
	 */
	private UUID id;

	/**
	 * The unique ID of the PreIPOCompany that the SPV is investing into.
	 * (format: uuid)
	 */
	private UUID preIPOCompanyInvestmentId;

	/**
	 * The PreIPOCompany which the SPV will be investing into.
	 * (format: uuid)
	 */
	private UUID preIPOCompanyId;

	/**
	 * Gets or sets the Sydecar identifier of the SPV resource.
	 * (nullable)
	 */
	private String sydecarId;

	/**
	 * Indicates whether this SPV will implement banking via Sydecar.
	 */
	private boolean bankingWithSydecar;

	/**
	 * The unique ID associated with the deal lead.
	 * (nullable)
	 */
	private String leadProfileID;

	/**
	 * The name of the SPV.
	 * (nullable)
	 */
	private String name;

	/**
	 * Gets or sets the name of the platform provider.
	 * (nullable)
	 */
	private String platformProvider;

	/**
	 * Gets or sets the tax form type.
	 * (nullable)
	 */
	private String taxForm;

	/**
	 * Indicates whether the SPV is using the default Sydecar Master LLC.
	 */
	private boolean usingSydecarMasterSeriesLLC;

	/**
	 * The stage that an SPV is in.
	 */
	private MonarkStage monarkStage;


	// ========= Optional Fields =========

	/**
	 * This number describes the total amount of capital that will be raised into the SPV,
	 * including management fees, commission paid to Partners distributing the SPV, fund admin fees, etc.
	 * (format: double, nullable)
	 */
	private Double totalAllocation;

	/**
	 * The minimum amount that an Investor can invest into an SPV during a primary offering.
	 * (format: double, nullable)
	 */
	private Double minCommitmentAmount;

	/**
	 * The type of asset that will be held by the PreIPOCompanySPV.
	 */
	private AssetType assetType;

	/**
	 * Discount rate if the security type held by the SPV is a SAFE.
	 * (format: double, nullable)
	 */
	private Double discountRate;

	/**
	 * Security type held by an SPV.
	 */
	private SecurityType securityType;

	/**
	 * The round of an SPV.
	 */
	private Round round;

	/**
	 * Indicates whether the SPV will own 20% or more of the total voting power of stock after closing the transaction.
	 */
	private Boolean ownStock;

	/**
	 * Valuation type.
	 */
	private ValuationType valuationType;

	/**
	 * Describes the implied valuation of the associated PreIPOCompany.
	 * (format: double)
	 */
	private Double valuation;

	/**
	 * Short description of the SPV.
	 */
	private String synopsis;

	/**
	 * Longer description of the SPV’s strategy or purpose.
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
	 */
	private String otherNotableTerms;

	/**
	 * Full series name for the SPV.
	 */
	private String sydecarSeriesName;

	/**
	 * EIN number for the Series LLC entity.
	 */
	private String seriesLLCEIN;

	/**
	 * The bank account ID associated with the SPV.
	 */
	private String leadBankAccountID;

	/**
	 * Gets or sets the Sydecar Master LLC ID.
	 */
	private String sydecarMasterLLCID;

	/**
	 * The EIN number associated with the Master LLC.
	 */
	private String masterLLCEIN;

	/**
	 * The name of the Master LLC.
	 */
	private String masterLLCName;

	/**
	 * The amount held back for additional costs.
	 * (format: double, nullable)
	 */
	private Double holdbackAmount;

	/**
	 * The one time, up front management fee charged by the SPV.
	 * (format: double, nullable)
	 */
	private Double managementFee;

	/**
	 * The number of years over which the management fee will be paid.
	 * (format: int32, nullable)
	 */
	private Integer managementFeeYearsPayable;

	/**
	 * Funding deadline associated with the primary issuance of an SPV.
	 * (format: date, nullable)
	 */
	private LocalDate fundingDeadline;

	/**
	 * The exemptions claimed by a PreIPOCompanySPV.
	 */
	private List<ExemptionClaimed> exemptionsClaimed;

	/**
	 * Indicates whether Sydecar is active.
	 */
	private Boolean isSydecarActive;

	/**
	 * Gets or sets the Sydecar stage.
	 */
	private SydecarStage sydecarStage;

	/**
	 * Additional dates related to the SPV.
	 * (format: date, nullable)
	 */
	private List<LocalDate> dates;

	/**
	 * CUSIP assigned to the SPV.
	 */
	private String cusip;

	/**
	 * Symbol assigned to the SPV.
	 */
	private String tradingSymbol;

	/**
	 * URL link to a template version of the PreIPOCompanySPV subscription agreement.
	 */
	private String subscriptionAgreementUrl;

	/**
	 * A URL link to the operating agreement for a PreIPOCompanySPV.
	 */
	private String operatingAgreementUrl;

	/**
	 * The date that the SPV officially closed.
	 * (format: date-time, nullable)
	 */
	private OffsetDateTime closeDate;

	/**
	 * Custodian where SPV shares are held.
	 */
	private String spvCustodian;

	/**
	 * The number of shares in the PreIPOCompany that will be held by the SPV.
	 * (format: double)
	 */
	private Double preIPOCompanyNumberShares;

	/**
	 * The number of remaining LP positions in an SPV available to Investors.
	 * (format: int32)
	 */
	private Integer numberOfSeatsRemaining;

	/**
	 * The remaining dollar amount in a primary fundraise of an SPV.
	 * (format: double)
	 */
	private Double remainingDollarAllocation;

	/**
	 * The remaining number of shares in a primary fundraise of an SPV.
	 * (format: double)
	 */
	private Integer remainingShareAllocation;

	/**
	 * Indicates whether the SPV will be prefunded.
	 */
	private Boolean preFundedInventory;

	/**
	 * Size, in USD, of the brokerage commission associated with this PreIPOCompanyInvestment.
	 * (format: double, nullable)
	 */
	private Double totalCommission;

	/**
	 * The percentage of the TotalCommission that will be paid to MMM Securities LLC.
	 * (format: double, nullable)
	 */
	private Double monarkCommissionSplit;

	/**
	 * The percentage of the TotalCommission that will be paid to the Partner distributing the SPV.
	 * (format: double, nullable)
	 */
	private Double demandSideCommissionSplit;

	/**
	 * The percentage of the ManagementFee that will be paid to a Partner distributing the SPV,
	 * if the Partner is an ERA or RIA.
	 * (format: double, nullable)
	 */
	private Double demandSideFeeSplit;

	/**
	 * The price per share of the PreIPOCompany, excluding fees, that an investor will pay.
	 * (format: double, nullable)
	 */
	private Double investorPricePerShare;

	/**
	 * The fee per share of the PreIPOCompany that an Investor will pay on the primary issuance.
	 * (format: double, nullable)
	 */
	private Double investorFeePerShare;

	/**
	 * The total amount per share that an Investor will pay, including fees.
	 * (format: double, nullable)
	 */
	private Double allInPricePerShare;

	/**
	 * SPV account ID.
	 * (format: int64, nullable)
	 */
	private Long spvAccountID;

	/**
	 * Gets or sets the date and time when the PreIPOCompanySPV was created.
	 * (format: date-time)
	 */
	private OffsetDateTime createdAt;

	/**
	 * Gets or sets the date and time when the PreIPOCompanySPV was last updated.
	 * (format: date-time, nullable)
	 */
	private OffsetDateTime updatedAt;
}
