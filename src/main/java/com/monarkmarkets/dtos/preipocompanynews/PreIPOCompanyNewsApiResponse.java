package com.monarkmarkets.dtos.preipocompanynews;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for PreIPOCompanyNews.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyNewsApiResponse {

	/**
	 * List of results returned.
	 */
	private List<PreIPOCompanyNews> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
