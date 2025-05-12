package com.monarkmarkets.dtos.indicationofinterest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for updating an existing Indication of Interest (IOI).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIndicationOfInterest {

	/**
	 * Unique ID associated with the IOI.
	 */
	private UUID id;

	/**
	 * Represents the unique ID associated with the Investor.
	 */
	private UUID investorId;

	/**
	 * The unique ID of the PreIPOCompany associated with the IOI.
	 */
	private UUID preIPOCompanyId;

	/**
	 * Notional amount of the IOI.
	 */
	private double notionalAmount;

	/**
	 * Currency in which the IOI is denominated (e.g., USD).
	 */
	private String currency;
}
