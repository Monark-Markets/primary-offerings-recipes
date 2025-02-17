package com.monarkmarkets.dtos.investor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvestor {
	private UUID id;
	private String investorReferenceId;
	private QualificationStatus qualificationStatus;

	@JsonProperty("individualInvestor")
	private IndividualInvestor individualInvestor;
}
