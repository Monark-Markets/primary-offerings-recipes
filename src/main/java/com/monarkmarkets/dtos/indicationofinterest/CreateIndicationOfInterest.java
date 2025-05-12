package com.monarkmarkets.dtos.indicationofinterest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for creating an Indication of Interest (IOI).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIndicationOfInterest {

	/**
	 * Represents the unique ID associated with the Investor submitting an IOI.
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
