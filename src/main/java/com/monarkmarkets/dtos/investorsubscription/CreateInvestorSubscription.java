package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for creating an InvestorSubscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvestorSubscription {

	/**
	 * The SPV ID that this Subscription relates to.
	 */
	private UUID preIPOCompanySPVId;

	/**
	 * ID of the Investor associated with this InvestorSubscription.
	 */
	private UUID investorId;

	/**
	 * The amount, in dollars, an Investor intends to invest.
	 */
	private double amountReservedDollars;

	/**
	 * Number of underlying shares associated with this subscription.
	 */
	private int amountReservedShares;
}
