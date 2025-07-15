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
	 * The amountReservedDollars must be larger than the minCommitmentAmount in the associated SPV.
	 * The amountReservedDollars must be a decimal up to two decimals. Monark supports fractional shares, and we calculate the fractional number of shares allocated to each subscription, up to 9 decimal places.
	 * The amountReservedDollars of the Subscription must be less than or equal to the remainingDollarAllocation in the associated SPV.
	 *
	 * @param spv the PreIPOCompanySPV object containing the SPV details
	 * @return the calculated SubscriptionAmount
	 */
	public static SubscriptionAmount calculateSubscriptionAmount(PreIPOCompanySPV spv) {
		double remainingShares = spv.getRemainingShareAllocation();
		double remainingDollars = spv.getRemainingDollarAllocation();
		double pricePerShare = spv.getAllInPricePerShare();
		double minCommitment = spv.getMinCommitmentAmount();

		// This will generate a more realistic amountReservedDollars value, tied to 2%–5% of remaining shares, keeping
		// the SPV from being depleted too quickly.
		double minShares = Math.floor(remainingShares * 0.02);
		double maxShares = Math.floor(remainingShares * 0.05);

		if (minShares < 0.000000001 || maxShares < 0.000000001 || minShares > maxShares) {
			throw new IllegalArgumentException("Insufficient share allocation to create a valid subscription.");
		}

		double minDollars = Math.max(minShares * pricePerShare, minCommitment);
		double maxDollars = Math.min(maxShares * pricePerShare, remainingDollars);

		if (minDollars > maxDollars) {
			throw new IllegalArgumentException("Insufficient dollar allocation to create a valid subscription.");
		}

		for (int i = 0; i < 10; i++) {
			double amountDollars = Math.round(
					current().nextDouble(minDollars, maxDollars + 0.01) * 100.0
			) / 100.0;

			double shares = Math.round(
					(amountDollars / pricePerShare) * 1_000_000_000.0
			) / 1_000_000_000.0;

			if (shares <= remainingShares && amountDollars <= remainingDollars && amountDollars >= minCommitment) {
				return new SubscriptionAmount(shares, amountDollars);
			}
		}

		throw new RuntimeException("Could not calculate valid subscription within constraints after 10 attempts.");
	}
}