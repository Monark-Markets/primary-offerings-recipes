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
public class Questionnaire {
	private UUID id;
	private String name;
	private String title;
	private List<QuestionnaireQuestion> questions;
	private String createdAt;
	private String updatedAt;
}
