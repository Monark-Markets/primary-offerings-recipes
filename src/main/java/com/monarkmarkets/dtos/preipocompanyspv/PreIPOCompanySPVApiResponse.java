package com.monarkmarkets.dtos.preipocompanyspv;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompanySPV.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanySPVApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompanySPV> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
