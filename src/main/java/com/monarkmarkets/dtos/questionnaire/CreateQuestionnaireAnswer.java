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
public class CreateQuestionnaireAnswer {
	private UUID questionnaireId;
	private UUID investorId;
	private List<CreateQuestionnaireQuestionAnswer> createQuestionAnswers;
}
