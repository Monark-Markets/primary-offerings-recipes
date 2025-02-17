package com.monarkmarkets.dtos.preipocompanyspv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanySPVReviewRequest {

	private UUID preIPOCompanySPVId;

	private MonarkStage monarkStage;
}
