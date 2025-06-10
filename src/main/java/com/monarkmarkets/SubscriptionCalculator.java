package com.monarkmarkets;

import com.monarkmarkets.api.primary.webapi.model.PreIPOCompanySPV;

import static java.util.concurrent.ThreadLocalRandom.current;

public class SubscriptionCalculator {

	public static class SubscriptionAmount {
		public final int amountReservedShares;
		public final double amountReservedDollars;

		public SubscriptionAmount(
				int shares,
				double dollars
		) {
			this.amountReservedShares = shares;
			this.amountReservedDollars = dollars;
		}

		@Override
		public String toString() {
			return "SubscriptionAmount{" +
					"amountReservedShares=" + amountReservedShares +
					", amountReservedDollars=" + amountReservedDollars +
					'}';
		}
	}

	/**
	 * Logic to Create Subscriptions
	 * When creating Subscriptions, there are a few requirements that must be met:
	 * <p>
	 * - The amountReservedShares, multiplied by the allInPricePerShare of the associated SPV, must equal the amountReservedDollars.
	 * - The amountReservedDollars must be larger than the minCommitmentAmount in the associated SPV.
	 * - The amountReservedShares must be a whole number. Fractional shares are not supported.
	 * - The amountReservedShares of the Subscription must be less than or equal to the remainingShareAllocation in the associated SPV.
	 * - The amountReservedDollars of the Subscription must be less than or equal to the remainingDollarAllocation in the associated SPV.
	 *
	 * @param spv the PreIPOCompanySPV object containing the SPV details
	 * @return the calculated SubscriptionAmount
	 */
	public static SubscriptionAmount calculateSubscription(PreIPOCompanySPV spv) {
		double remainingShares = spv.getRemainingShareAllocation();
		double minShares = Math.floor(remainingShares * 0.02);
		double maxShares = Math.floor(remainingShares * 0.05);

		if (minShares < 1 || maxShares < 1 || minShares > maxShares) {
			throw new IllegalArgumentException("Insufficient share allocation to create a valid subscription.");
		}

		for (int i = 0; i < 10; i++) { // Retry up to 10 times to find a valid amount
			int randomShares = current().nextInt((int) minShares, (int) maxShares + 1);
			double amountDollars = randomShares * spv.getAllInPricePerShare();

			if (randomShares <= remainingShares &&
					amountDollars <= spv.getRemainingDollarAllocation() &&
					amountDollars >= spv.getMinCommitmentAmount()) {
				return new SubscriptionAmount(randomShares, amountDollars);
			}
		}

		throw new RuntimeException("Could not calculate valid subscription within constraints after 10 attempts.");
	}
}