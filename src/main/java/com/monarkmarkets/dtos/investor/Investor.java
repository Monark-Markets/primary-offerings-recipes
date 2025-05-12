package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents an Investor in the primary offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Investor {

	/**
	 * Unique ID, provided by Monark, associated with an Investor.
	 */
	private UUID id;

	/**
	 * ID of the Partner associated with an Investor.
	 */
	private UUID partnerId;

	/**
	 * The Financial Institution ID associated with the Investor.
	 */
	private UUID financialInstitutionId;

	/**
	 * Updated at the specified date.
	 */
	private OffsetDateTime updatedAt;

	/**
	 * Represents the unique ID provided by a Partner, used to identify an Investor.
	 */
	private String investorReferenceId;

	/**
	 * Status of the Investor.
	 */
	private String status;

	/**
	 * Type of the Investor.
	 */
	private InvestorType type;

	/**
	 * Information about the individual investor if this Investor is of that type.
	 */
	private IndividualInvestor individualInvestor;

	/**
	 * Information about the entity investor if this Investor is of that type.
	 */
	private EntityInvestor entityInvestor;

	/**
	 * The name of the associated partner.
	 */
	private String partnerName;

	/**
	 * The name of the associated Financial Institution.
	 */
	private String financialInstitutionName;
}
