package com.monarkmarkets.dtos.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionnaireQuestionAnswer {
	private UUID questionnaireQuestionId;
	private String value;
}
