package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvestorSubscription {

	/**
	 * The SPV ID that this Subscription relates to.
	 * (minLength: 1, format: uuid)
	 */
	private UUID preIPOCompanySPVId;

	/**
	 * ID of the Investor associated with this InvestorSubscription.
	 * (minLength: 1, format: uuid)
	 */
	private UUID investorId;

	/**
	 * Describes the amount, in dollars, an Investor intends to invest into an SPV.
	 * (minimum: 0, exclusiveMinimum: true, format: double)
	 */
	private Double amountReservedDollars;

	/**
	 * Describes how many underlying shares in the pre-IPO company are associated with this InvestorSubscription.
	 * This must be a whole number of shares.
	 * (minimum: 0, exclusiveMinimum: true, format: int32)
	 */
	private Integer amountReservedShares;
}
