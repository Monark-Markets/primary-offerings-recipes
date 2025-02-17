package com.monarkmarkets.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents an answer to a questionnaire.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireAnswer {

    /**
     * Unique ID associated with a Questionnaire answer.
     * (Format: uuid)
     */
    private UUID id;

    /**
     * Unique ID of the Questionnaire being answered.
     * (Format: uuid, Required)
     */
    private UUID questionnaireId;

    /**
     * The linked Investor ID.
     * (Format: uuid)
     */
    private String investorId;

    /**
     * All the questions answered from the Questionnaire.
     * (Nullable)
     */
    private List<QuestionnaireQuestionAnswer> questionAnswers;

    /**
     * Created at the specified date.
     * (Format: date-time)
     */
    private LocalDateTime createdAt;

    /**
     * Updated at the specified date.
     * (Format: date-time)
     */
    private LocalDateTime updatedAt;

    /**
     * Represents a question-answer pair in a questionnaire.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionnaireQuestionAnswer {

        /**
         * Unique ID associated with a Questionnaire question-answer pairing.
         * (Format: uuid)
         */
        private UUID id;

        /**
         * Text of the question asked.
         * (Nullable)
         */
        private String questionText;

        /**
         * Explanation of the question asked.
         * (Nullable)
         */
        private String questionExplanation;

        /**
         * Format of the question asked.
         * (Required)
         */
        private QuestionFormat questionFormat;

        /**
         * A list of potential answers associated with a question.
         * (Nullable)
         */
        private List<String> questionOptions;

        /**
         * Value of the answer to the question.
         * (Nullable, Required)
         */
        private String value;

        /**
         * Created at the specified date.
         * (Format: date-time)
         */
        private LocalDateTime createdAt;

        /**
         * Updated at the specified date.
         * (Format: date-time)
         */
        private LocalDateTime updatedAt;

        public enum QuestionFormat {
            INTEGER("Integer"),
            FLOAT("Float"),
            PERCENTAGE("Percentage"),
            MULTIPLE_CHOICE_SINGLE("MultipleChoiceSingle"),
            MULTIPLE_CHOICE_MULTIPLE("MultipleChoiceMultiple"),
            BOOLEAN("Boolean"),
            DATE("Date"),
            TEXT("Text"),
            EMAIL("Email"),
            SCALE("Scale");

            private final String value;

            QuestionFormat(String value) {
                this.value = value;
            }

            @JsonValue
            public String getValue() {
                return value;
            }

            @JsonCreator
            public static QuestionFormat fromString(String key) {
                for (QuestionFormat format : values()) {
                    if (format.value.equalsIgnoreCase(key)) {
                        return format;
                    }
                }
                throw new IllegalArgumentException("Invalid QuestionFormat: " + key);
            }
        }
    }
}
