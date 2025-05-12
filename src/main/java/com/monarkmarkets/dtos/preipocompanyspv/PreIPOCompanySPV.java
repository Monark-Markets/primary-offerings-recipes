package com.monarkmarkets.dtos.preipocompanyspv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * PreIPOCompanySPV represents the public information provided for a PreIPOCompanySPV in the Primary Offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanySPV {

	/**
	 * Unique ID associated with an SPV.
	 */
	private UUID id;

	/**
	 * The unique ID of the PreIPOCompany that the SPV is investing into.
	 */
	private UUID preIPOCompanyInvestmentId;

	/**
	 * The PreIPOCompany which the SPV will be investing into.
	 */
	private UUID preIPOCompanyId;

	/**
	 * Total amount of capital that will be raised into the SPV.
	 */
	private Double totalDollarAllocation;

	/**
	 * Minimum investment amount during the primary offering.
	 */
	private Double minCommitmentAmount;

	/**
	 * Name of the SPV.
	 */
	private String name;

	/**
	 * Implied valuation of the associated PreIPOCompany.
	 */
	private Double valuation;

	/**
	 * Short description of the SPV.
	 */
	private String synopsis;

	/**
	 * Longer description of the SPV.
	 */
	private String description;

	/**
	 * Notable investors associated with the SPV.
	 */
	private List<String> notableInvestors;

	/**
	 * Name of the Master LLC.
	 */
	private String masterLLCName;

	/**
	 * One-time upfront management fee charged by the SPV.
	 */
	private Double managementFee;

	/**
	 * Number of years the management fee is payable.
	 */
	private Integer managementFeeYearsPayable;

	/**
	 * ID of the lead profile user.
	 */
	private UUID leadProfileID;

	/**
	 * Unique Sydecar ID for the SPV.
	 */
	private UUID sydecarId;

	/**
	 * Whether the SPV banks with Sydecar.
	 */
	private Boolean bankingWithSydecar;

	/**
	 * Whether the SPV uses Sydecar Master Series LLC.
	 */
	private Boolean usingSydecarMasterSeriesLLC;

	/**
	 * Platform provider for the SPV.
	 */
	private String platformProvider;

	/**
	 * Tax form associated with the SPV.
	 */
	private String taxForm;

	/**
	 * Monark stage of the SPV lifecycle.
	 */
	private MonarkStage monarkStage;

	/**
	 * CUSIP assigned to the SPV.
	 */
	private String cusip;

	/**
	 * Symbol used for trading the SPV.
	 */
	private String tradingSymbol;

	/**
	 * Close date of the SPV.
	 */
	private String closeDate;

	/**
	 * Custodian where SPV shares are held.
	 */
	private String spvCustodian;

	/**
	 * Total share allocation of the SPV.
	 */
	private Double totalShareAllocation;

	/**
	 * Total number of seats in the SPV.
	 */
	private Integer totalNumberOfSeats;

	/**
	 * Number of LP positions remaining.
	 */
	private Integer numberOfSeatsRemaining;

	/**
	 * Remaining dollar allocation in the SPV.
	 */
	private Double remainingDollarAllocation;

	/**
	 * Remaining share allocation in the SPV.
	 */
	private Double remainingShareAllocation;

	/**
	 * Indicates whether the SPV is prefunded.
	 */
	private Boolean preFundedInventory;

	/**
	 * Total commission in USD after ERA fees.
	 */
	private Double totalCommission;

	/**
	 * Commission percentage allocated to Monark.
	 */
	private Double monarkCommissionSplit;

	/**
	 * Commission percentage allocated to the Partner.
	 */
	private Double demandSideCommissionSplit;

	/**
	 * Price per share excluding fees.
	 */
	private Double investorPricePerShare;

	/**
	 * Fee per share paid by the investor.
	 */
	private Double investorFeePerShare;

	/**
	 * All-in price per share including fees.
	 */
	private Double allInPricePerShare;

	/**
	 * Funding deadline for the SPV.
	 */
	private String fundingDeadline;

	/**
	 * Exemptions claimed under SEC regulations.
	 */
	private List<String> exemptionsClaimed;

	/**
	 * The base documents associated with the SPV (Optional).
	 */
	private List<String> documents;

	/**
	 * SPV account ID.
	 */
	private Long spvAccountID;

	/**
	 * Name of the PreIPOCompany.
	 */
	private String preIPOCompanyName;
}
