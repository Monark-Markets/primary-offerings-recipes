package com.monarkmarkets.dtos.indicationofinterest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Full representation of an Indication of Interest (IOI).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicationOfInterest {

	/**
	 * Unique identifier for the IOI.
	 */
	private UUID id;

	/**
	 * Associated investor ID.
	 */
	private UUID investorId;

	/**
	 * Associated PreIPOCompany ID.
	 */
	private UUID preIPOCompanyId;

	/**
	 * Notional amount of the IOI.
	 */
	private double notionalAmount;

	/**
	 * Currency in which the IOI is denominated.
	 */
	private String currency;

	/**
	 * Timestamp when the IOI was created.
	 */
	private String createdAt;

	/**
	 * Timestamp of the last update to the IOI.
	 */
	private String updatedAt;

	/**
	 * Name of the associated partner.
	 */
	private String partnerName;

	/**
	 * First name of the investor.
	 */
	private String investorFirstName;

	/**
	 * Last name of the investor.
	 */
	private String investorLastName;

	/**
	 * Name of the PreIPOCompany.
	 */
	private String preIPOCompanyName;
}
