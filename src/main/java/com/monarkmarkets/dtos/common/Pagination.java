package com.monarkmarkets.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination metadata returned in paged responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
	private int totalRecords;
	private int totalPages;
	private int currentPage;
}
