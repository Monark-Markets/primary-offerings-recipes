package com.monarkmarkets.dtos.investor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Investor {
	private UUID id;
	private String investorReferenceId;
	private UUID partnerId;

	private KycStatus kycStatus;
	private AccreditationStatus accreditationStatus;
	private QualificationStatus qualificationStatus;
	private Status status;

	private LocalDate accreditationUpdatedAt;
	private LocalDateTime updatedAt;
	private String kycUpdatedAt;

	@JsonProperty("individualInvestor")
	private IndividualInvestor individualInvestor;
}
