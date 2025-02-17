package com.monarkmarkets.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Partner represents the primary offering information of the investor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

	/**
	 * Unique ID, provided by Monark, associated with a Partner.
	 */
	private UUID id;

	/**
	 * Name of the Partner.
	 */
	private String name;

	/**
	 * Type of the Partner.
	 */
	private PartnerType partnerType;

	/**
	 * Legal Entity Name of the partner.
	 */
	private String legalEntityName;

	/**
	 * ISO Phone Country Code.
	 */
	private String primaryPhoneCountryCode;

	/**
	 * The phone number of the Partner.
	 */
	private String primaryPhone;

	/**
	 * Partner’s street 1 address.
	 */
	private String street1;

	/**
	 * Partner’s secondary street address.
	 */
	private String street2;

	/**
	 * City where the Partner resides.
	 */
	private String city;

	/**
	 * State of the Partner.
	 */
	private String state;

	/**
	 * Zip code of the Partner.
	 */
	private String zipCode;

	/**
	 * Country code of the Partner.
	 */
	private String countryCode;

	/**
	 * Website of the Partner.
	 */
	private String website;

	/**
	 * The primary contact name of the Partner.
	 */
	private String primaryContactName;

	/**
	 * The primary email address of the Partner.
	 */
	private String primaryEmail;

	/**
	 * Executive Team of the Partner.
	 */
	private List<String> executiveTeam;

	/**
	 * Created at the specified date.
	 */
	private LocalDateTime createdAt;

	/**
	 * Updated at the specified date.
	 */
	private LocalDateTime updatedAt;

	/**
	 * Enum representing the type of Partner.
	 */
	public enum PartnerType {
		BrokerageFirm
	}
}