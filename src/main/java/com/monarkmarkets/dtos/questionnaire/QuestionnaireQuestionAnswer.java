package com.monarkmarkets.dtos.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireQuestionAnswer {
	private UUID id;
	private String questionText;
	private String questionExplanation;
	private QuestionnaireQuestionFormat questionFormat;
	private List<String> questionOptions;
	private String value;
	private String createdAt;
	private String updatedAt;
}
