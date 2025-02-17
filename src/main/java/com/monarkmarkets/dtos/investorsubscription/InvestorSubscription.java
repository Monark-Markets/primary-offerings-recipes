package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscription {

	private UUID id;
	private UUID preIPOCompanySPVId;
	private UUID investorId;

	@NonNull
	private Double amountReservedDollars;

	@NonNull
	private Integer amountReservedShares;

	@NonNull
	private Status status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public enum Status {
		Pending,
		Complete,
		Rejected
	}
}