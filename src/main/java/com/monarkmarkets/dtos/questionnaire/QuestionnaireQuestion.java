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
public class QuestionnaireQuestion {
	private UUID id;
	private String identifier;
	private int sequence;
	private QuestionnaireQuestionStatus status;
	private String text;
	private String explanation;
	private QuestionnaireQuestionFormat format;
	private List<String> options;
	private Integer scaleMin;
	private Integer scaleMax;
	private Integer scaleStep;
	private String createdAt;
	private String updatedAt;
}
