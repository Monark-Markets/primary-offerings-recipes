package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Create Investor Lead represents the investor lead information required for the primary offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvestor {

	/**
	 * The Financial Institution ID associated with the Investor.
	 * If not provided, the default Financial Institution for the Partner will be used.
	 */
	private UUID financialInstitutionId;

	/**
	 * Represents the unique ID provided by a Partner, used to identify an Investor.
	 */
	private String investorReferenceId;

	/**
	 * Type of the Investor.
	 */
	private InvestorType type;
}
