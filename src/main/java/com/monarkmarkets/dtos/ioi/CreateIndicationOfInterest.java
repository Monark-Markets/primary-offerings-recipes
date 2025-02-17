package com.monarkmarkets.dtos.ioi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateIndicationOfInterest {

	private UUID investorId;
	private UUID preIPOCompanyId;
	private double notionalAmount;
	private String currency;
}
