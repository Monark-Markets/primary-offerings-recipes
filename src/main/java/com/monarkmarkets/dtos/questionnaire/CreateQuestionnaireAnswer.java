package com.monarkmarkets.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQuestionnaireAnswer {
	private List<CreateQuestionAnswer> createQuestionAnswers;
	private UUID questionnaireId;
	private UUID investorId;

	@Data
	public static class CreateQuestionAnswer {
		private UUID questionnaireQuestionId;
		private String value;
	}
}
