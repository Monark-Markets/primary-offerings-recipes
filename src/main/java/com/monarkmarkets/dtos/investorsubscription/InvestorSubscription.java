package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a full InvestorSubscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscription {

	/**
	 * Unique ID associated with the subscription.
	 */
	private UUID id;

	/**
	 * The SPV ID that this subscription relates to.
	 */
	private UUID preIPOCompanySPVId;

	/**
	 * ID of the Investor associated with the subscription.
	 */
	private UUID investorId;

	/**
	 * The amount, in dollars, the Investor intends to invest.
	 */
	private double amountReservedDollars;

	/**
	 * Number of shares the Investor reserves.
	 */
	private int amountReservedShares;

	/**
	 * Current status of the subscription.
	 */
	private InvestorSubscriptionStatus status;

	/**
	 * Timestamp of creation.
	 */
	private String createdAt;

	/**
	 * Timestamp of last update (nullable).
	 */
	private String updatedAt;

	/**
	 * Name of the Partner associated with the Investor (nullable).
	 */
	private String partnerName;

	/**
	 * Name of the Investor (nullable).
	 */
	private String investorName;
}
