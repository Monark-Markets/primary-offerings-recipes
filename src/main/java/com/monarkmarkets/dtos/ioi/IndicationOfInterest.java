package com.monarkmarkets.dtos.ioi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndicationOfInterest {
	private String id;
	private String investorId;
	private String preIPOCompanyId;
	private String partnerName;
	private String investorFirstName;
	private String investorLastName;
	private BigDecimal notionalAmount;
	private String currency;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
}
