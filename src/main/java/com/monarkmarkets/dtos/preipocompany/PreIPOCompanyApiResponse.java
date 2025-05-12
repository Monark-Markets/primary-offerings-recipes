package com.monarkmarkets.dtos.preipocompany;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompany.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompany> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
