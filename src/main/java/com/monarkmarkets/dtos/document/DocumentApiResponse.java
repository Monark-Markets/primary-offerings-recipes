package com.monarkmarkets.dtos.document;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for Document.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentApiResponse {

	/**
	 * List of results returned.
	 */
	private List<Document> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
