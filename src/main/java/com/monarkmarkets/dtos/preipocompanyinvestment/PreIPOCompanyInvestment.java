package com.monarkmarkets.dtos.preipocompanyinvestment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * PreIPOCompanyInvestment represents an investment in a PreIPOCompany.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyInvestment {

	/**
	 * Unique identifier for the PreIPOCompanyInvestment.
	 * (Format: uuid)
	 */
	private UUID id;

	/**
	 * The PreIPOCompany associated with this Investment.
	 * (Format: uuid)
	 */
	private UUID preIPOCompanyID;

	/**
	 * Created at the specified Date.
	 * (Format: date-time)
	 */
	private OffsetDateTime createdAt;

	/**
	 * Updated at the specified Date.
	 * (Format: date-time, nullable)
	 */
	private OffsetDateTime updatedAt;

	/**
	 * Indicates the type of shares for the PreIPOCompanyInvestment.
	 * Possible values may include "Common", "Preferred", or "Common and Preferred".
	 * <p>
	 * <b>Required.</b>
	 */
	private String shareType;

	/**
	 * Share class of PreIPOCompany. Can be Series A, Series B, Series C, etc.
	 * <p>
	 * <b>Required.</b>
	 */
	private String shareClass;

	/**
	 * The number of shares in the PreIPOCompany associated with this Investment.
	 * (Format: double, nullable)
	 */
	private Double numberOfShares;

	/**
	 * The net amount of capital, including all commissions and fees associated with this PreIPOCompanyInvestment.
	 * This number determines the total amount of cash that needs to be syndicated in order to execute the transaction.
	 * (Format: double, nullable)
	 */
	private Double totalCapitalRaise;

	/**
	 * This is the total amount that will be sent to the seller or issuer, after all commissions and fees have been paid.
	 * (Format: double)
	 */
	private Double investmentSize;

	/**
	 * The price per share of the PreIPOCompany that will be paid to acquire the shares.
	 * (Format: double, nullable)
	 */
	private Double pricePerShare;

	/**
	 * A transactionType of Primary means that Monark has received an allocation in a PreIPOCompany's primary capital raise,
	 * and we are syndicating capital through an SPV to participate in the raise.
	 * transactionType of Secondary means that an existing shareholder in the PreIPOCompany is looking for liquidity and Monark
	 * is interacting with the shareholder as a single LP, raising capital through the SPV and buying shares from the seller.
	 * transactionType of TenderOffer means that Monark is participating in a company sponsored share repurchase program through an SPV,
	 * helping the PreIPOCompany offer shareholders liquidity.
	 */
	private TransactionType transactionType;

	/**
	 * Describes whether or not the PreIPOCompanyInvestment is taking place through a Layered SPV.
	 * (Nullable)
	 */
	private LayeredSPV layeredSPV;
}
