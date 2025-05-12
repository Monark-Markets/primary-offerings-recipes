package com.monarkmarkets.dtos.preipocompany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO representing the public information for a PreIPOCompany in the Primary Offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompany {

	/**
	 * Unique ID associated with a PreIPOCompany.
	 */
	private UUID id;

	/**
	 * The name of the PreIPOCompany.
	 */
	private String name;

	/**
	 * Domain URL of the PreIPOCompany.
	 */
	private String domain;

	/**
	 * The LinkedIn profile URL associated with the PreIPOCompany.
	 */
	private String linkedIn;

	/**
	 * The X (Twitter) profile URL of the PreIPOCompany.
	 */
	private String x;

	/**
	 * The Facebook profile URL of the PreIPOCompany.
	 */
	private String facebook;

	/**
	 * Country where the PreIPOCompany is located.
	 */
	private String country;

	/**
	 * Headquarters address of the PreIPOCompany.
	 */
	private String address;

	/**
	 * Type of entity of the PreIPOCompany.
	 */
	private String type;

	/**
	 * Year the PreIPOCompany was established.
	 */
	private String yearEst;

	/**
	 * Estimated number of employees.
	 */
	private Double numJobs;

	/**
	 * Description or mission of the PreIPOCompany.
	 */
	private String description;

	/**
	 * Name of the founder.
	 */
	private String founder;

	/**
	 * URL to the logo of the company.
	 */
	private String logoURL;

	/**
	 * Total funding raised by the PreIPOCompany in USD.
	 */
	private Double totalFunding;

	/**
	 * Most recent funding round series.
	 */
	private String lastFundingSeries;

	/**
	 * Amount raised in the last round.
	 */
	private Double lastFundingTotal;

	/**
	 * Total number of funding rounds completed.
	 */
	private Double totalFundingRounds;

	/**
	 * Date of the most recent funding round.
	 */
	private String lastFundingDate;

	/**
	 * Type of last funding (e.g., equity or debt).
	 */
	private String lastFundingType;

	/**
	 * Number of distinct investors in the company.
	 */
	private Double numInvestors;

	/**
	 * Revenue generated in the last 12 months.
	 */
	private Double twelveMonthTrailingRevenue;

	/**
	 * Valuation from the last funding round.
	 */
	private Double lastValuation;

	/**
	 * Share price from the last round.
	 */
	private Double lastSharePrice;

	/**
	 * Number of customers served.
	 */
	private Double numCustomers;

	/**
	 * List of key executives or team members.
	 */
	private List<String> executiveTeam;

	/**
	 * Total shares issued by the company.
	 */
	private Double numSharesOutstanding;

	/**
	 * List of logos of notable investors.
	 */
	private List<String> notableInvestorsLogos;

	/**
	 * Categories or sectors the company belongs to.
	 */
	private List<String> categories;

	/**
	 * Market reference price per share.
	 */
	private Double referencePrice;

	/**
	 * Date when the reference price was last updated.
	 */
	private String referencePriceDate;
}
