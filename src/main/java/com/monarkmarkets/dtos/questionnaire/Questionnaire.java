package com.monarkmarkets.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Questionnaire {
	private UUID id;
	private String name;
	private String title;

	private List<QuestionnaireQuestion> questions;

	@JsonProperty("createdAt")
	private String createdAt;

	@JsonProperty("updatedAt")
	private String updatedAt;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class QuestionnaireQuestion {
		private UUID id;
		private int sequence;
		private String status;
		private String text;
		private String explanation;
		private String format;
		private List<String> options;

		@JsonProperty("scaleMin")
		private String scaleMin;

		@JsonProperty("scaleMax")
		private String scaleMax;

		@JsonProperty("scaleStep")
		private String scaleStep;

		@JsonProperty("createdAt")
		private String createdAt;

		@JsonProperty("updatedAt")
		private String updatedAt;
	}
}