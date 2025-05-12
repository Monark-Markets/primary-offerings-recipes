package com.monarkmarkets.dtos.preipocompanyinvestment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents an investment in a PreIPOCompany.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyInvestment {

	/**
	 * Unique identifier for the PreIPOCompanyInvestment.
	 */
	private UUID id;

	/**
	 * The PreIPOCompany associated with this Investment.
	 */
	private UUID preIPOCompanyID;

	/**
	 * Created at the specified Date.
	 */
	private String createdAt;

	/**
	 * Updated at the specified Date.
	 */
	private String updatedAt;

	/**
	 * Indicates the type of shares for the Pre-IPOCompanyInvestment.
	 */
	private String shareType;

	/**
	 * Share class of PreIPOCompany. Can be Series A, Series B, Series C, etc.
	 */
	private String shareClass;

	/**
	 * The number of shares in the PreIPOCompany associated with this Investment.
	 */
	private Double numberOfShares;

	/**
	 * The net amount of capital, including all commissions and fees associated with this Investment.
	 */
	private Double totalCapitalRaise;

	/**
	 * The total amount sent to the seller or issuer, after all commissions and fees.
	 */
	private Double investmentSize;

	/**
	 * The price per share of the PreIPOCompany.
	 */
	private Double pricePerShare;

	/**
	 * The type of transaction: Primary, Secondary, or TenderOffer.
	 */
	private TransactionType transactionType;

	/**
	 * Describes whether the Investment is taking place through a Layered SPV.
	 */
	private LayeredSPV layeredSPV;

	/**
	 * The name of the PreIPOCompany.
	 */
	private String companyName;
}
