package com.monarkmarkets.dtos.preipocompanyresearch;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompanyResearch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyResearchApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompanyResearch> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
