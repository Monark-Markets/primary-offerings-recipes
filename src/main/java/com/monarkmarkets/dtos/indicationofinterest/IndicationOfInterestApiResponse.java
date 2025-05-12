package com.monarkmarkets.dtos.indicationofinterest;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API response DTO wrapping a list of Indications of Interest and pagination info.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicationOfInterestApiResponse {

	/**
	 * List of Indications of Interest returned by the API.
	 */
	private List<IndicationOfInterest> results;

	/**
	 * Pagination information associated with this response.
	 */
	private Pagination pagination;
}
