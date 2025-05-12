package com.monarkmarkets.dtos.questionnaire;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for Questionnaire.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireApiResponse {

	/**
	 * List of results returned.
	 */
	private List<Questionnaire> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
