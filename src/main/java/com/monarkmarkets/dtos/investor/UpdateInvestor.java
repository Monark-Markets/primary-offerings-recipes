package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Update Investor represents the information required to update an investor in the primary offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvestor {

	/**
	 * Unique ID of the Investor.
	 */
	private UUID id;

	/**
	 * Represents the unique ID provided by a Partner, used to identify an Investor.
	 */
	private String investorReferenceId;

	/**
	 * Information about the individual investor if this Investor is of that type.
	 */
	private ModifyIndividualInvestor individualInvestor;

	/**
	 * Information about the entity investor if this Investor is of that type.
	 */
	private UpdateEntityInvestor entityInvestor;
}
