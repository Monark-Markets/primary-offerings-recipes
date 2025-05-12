package com.monarkmarkets.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO representing a Partner entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

	/**
	 * Unique ID for the Partner.
	 */
	private UUID id;

	/**
	 * Display name of the Partner.
	 */
	private String name;

	/**
	 * Type of the Partner (e.g., BrokerageFirm).
	 */
	private PartnerType partnerType;

	/**
	 * Legal entity name of the Partner.
	 */
	private String legalEntityName;

	/**
	 * Primary contact name at the Partner organization.
	 */
	private String primaryContactName;

	/**
	 * Primary email address for contact.
	 */
	private String primaryEmail;

	/**
	 * Primary phone number.
	 */
	private String primaryPhone;

	/**
	 * Country code for the primary phone number.
	 */
	private String primaryPhoneCountryCode;

	/**
	 * Street address line 1.
	 */
	private String street1;

	/**
	 * Street address line 2 (optional).
	 */
	private String street2;

	/**
	 * City of the address.
	 */
	private String city;

	/**
	 * State of the address.
	 */
	private String state;

	/**
	 * Zip or postal code.
	 */
	private String zipCode;

	/**
	 * ISO country code.
	 */
	private String countryCode;

	/**
	 * Website URL of the Partner.
	 */
	private String website;

	/**
	 * List of executive team members (optional).
	 */
	private List<String> executiveTeam;

	/**
	 * List of financial institutions associated with the Partner (nullable).
	 */
	private List<FinancialInstitution> financialInstitutions;

	/**
	 * Timestamp of Partner creation.
	 */
	private String createdAt;

	/**
	 * Timestamp of last update to Partner details.
	 */
	private String updatedAt;
}
