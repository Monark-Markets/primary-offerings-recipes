package com.monarkmarkets;

import com.monarkmarkets.primary.client.model.PreIPOCompanySPV;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.concurrent.ThreadLocalRandom.current;

public class SubscriptionCalculator {

	@Getter
	@RequiredArgsConstructor
	public static class SubscriptionAmount {
		public final double amountReservedShares;
		public final double amountReservedDollars;
	}

	/**
	 * Logic to Create Subscriptions
	 * When creating Subscriptions, there are a few requirements that must be met:
	 * <p>
	 *  <ul>The amountReservedDollars must be larger than the minCommitmentAmount in the associated SPV, unless the
	 *  remainingDollarAllocation is smaller than the minCommitmentAmount. If the dollar amount remaining in the SPV is
	 *  less than the minimum commitment amount, investors must take all remaining dollars.
	 *  </ul>
	 *  <ul>The amountReservedDollars must be a decimal up to two decimals. Monark supports fractional shares, and we
	 *  calculate the fractional number of shares allocated to each subscription, up to 9 decimal places.
	 *  </ul>
	 *  <ul>The amountReservedDollars of the Subscription must be less than or equal to the remainingDollarAllocation
	 *  in the associated SPV.
	 *  </ul>
	 *
	 * @param spv the PreIPOCompanySPV object containing the SPV details
	 * @return the calculated SubscriptionAmount
	 */
	public static SubscriptionAmount calculateSubscriptionAmount(PreIPOCompanySPV spv) {
		double remainingShares = spv.getRemainingShareAllocation();
		double remainingDollars = spv.getRemainingDollarAllocation();
		double pricePerShare = spv.getAllInPricePerShare();
		double minCommitment = spv.getMinCommitmentAmount();

		if (remainingShares <= 0.0 || remainingDollars <= 0.0 || pricePerShare <= 0.0 || minCommitment <= 0.0) {
			throw new IllegalArgumentException("SPV contains invalid or insufficient data.");
		}

		// Case 1: remainingDollarAllocation < minCommitmentAmount, then take it all
		if (remainingDollars < minCommitment) {
			double roundedShares = Math.round(remainingShares * 1_000_000_000.0) / 1_000_000_000.0;
			double roundedDollars = Math.round(remainingDollars * 100.0) / 100.0;
			return new SubscriptionAmount(roundedShares, roundedDollars);
		}

		// Calculate bounds using 2%–5% of remaining shares
		double minShares = Math.floor(remainingShares * 0.02);
		double maxShares = Math.floor(remainingShares * 0.05);

		// Convert share bounds to dollar bounds
		double minDollars = Math.max(minShares * pricePerShare, minCommitment);
		double maxDollars = Math.min(maxShares * pricePerShare, remainingDollars);

		// Fallback condition: min/max shares too small to compute, or no room between bounds
		if (minShares < 1 || maxShares < 1 || minDollars > maxDollars) {
			double roundedShares = Math.round(remainingShares * 1_000_000_000.0) / 1_000_000_000.0;
			double roundedDollars = Math.round(remainingDollars * 100.0) / 100.0;
			return new SubscriptionAmount(roundedShares, roundedDollars);
		}

		// Try up to 10 times to pick a valid realistic value
		for (int i = 0; i < 10; i++) {
			double amountDollars = Math.round(
					current().nextDouble(minDollars, maxDollars + 0.01) * 100.0
			) / 100.0;

			double shares = Math.round(
					(amountDollars / pricePerShare) * 1_000_000_000.0
			) / 1_000_000_000.0;

			if (shares <= remainingShares &&
				amountDollars <= remainingDollars &&
				amountDollars >= minCommitment) {
				return new SubscriptionAmount(shares, amountDollars);
			}
		}

		throw new RuntimeException("Could not calculate valid subscription within constraints after 10 attempts.");
	}
}