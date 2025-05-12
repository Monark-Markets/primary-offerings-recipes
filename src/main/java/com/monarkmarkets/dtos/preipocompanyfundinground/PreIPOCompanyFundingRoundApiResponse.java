package com.monarkmarkets.dtos.preipocompanyfundinground;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompanyFundingRound.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyFundingRoundApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompanyFundingRound> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
