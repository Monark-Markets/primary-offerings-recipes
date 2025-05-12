package com.monarkmarkets.dtos.investorsubscription;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for InvestorSubscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscriptionApiResponse {

	/**
	 * List of results returned.
	 */
	private List<InvestorSubscription> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
