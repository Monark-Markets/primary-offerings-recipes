package com.monarkmarkets.dtos.preipocompanyinvestment;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompanyInvestment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyInvestmentApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompanyInvestment> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
