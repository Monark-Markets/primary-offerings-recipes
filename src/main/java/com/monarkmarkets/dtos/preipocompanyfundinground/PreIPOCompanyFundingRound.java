package com.monarkmarkets.dtos.preipocompanyfundinground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO representing a PreIPOCompanyFundingRound.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyFundingRound {

	/**
	 * The entity ID attached to this funding round.
	 */
	private UUID id;

	/**
	 * The unique ID of the PreIPOCompany associated with the funding round.
	 */
	private UUID preIPOCompanyId;

	/**
	 * Represents the name of the funding round.
	 */
	private String roundName;

	/**
	 * Collection of share details associated with the funding rounds.
	 */
	private List<ShareDetail> shareDetails;

	/**
	 * Collection of citations associated with the funding rounds.
	 */
	private List<Citation> citations;

	/**
	 * The source of the PreIPOCompanyFundingRound.
	 */
	private String source;

	/**
	 * Issued at the specified date.
	 */
	private String issuedAt;
}
