package com.monarkmarkets.dtos.investor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents an entity investor within the primary offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityInvestor {

	/**
	 * Id of the Entity Investor.
	 */
	private UUID id;

	/**
	 * DBA or Trade name of the entity.
	 */
	private String dbaName;

	/**
	 * Total number of beneficial owners associated with this entity investor.
	 * Used to calculate remaining seats in an SPV after an entity invests.
	 */
	private Integer beneficialOwnershipCount;

	/**
	 * An unformatted phone number which does not include the country code.
	 */
	private String phone;

	/**
	 * Phone country code (do not include a '+' sign).
	 */
	private String phoneCountryCode;

	/**
	 * Type of the entity.
	 */
	private EntityType entityType;

	/**
	 * Either a US state or country of incorporation.
	 */
	private String jurisdiction;

	/**
	 * Date that the entity was formed.
	 */
	private OffsetDateTime formationDate;

	/**
	 * Tax classification of the entity.
	 */
	private TaxClassification taxClassification;

	/**
	 * The tax id (EIN) for entities; for revocable trusts, a tax id is not required.
	 * This PII field is tokenized.
	 */
	private String taxId;

	/**
	 * Primary address street of the entity.
	 */
	private String street;

	/**
	 * City where the entity is located.
	 */
	private String city;

	/**
	 * State (or region) of the entity. Use valid state/region codes.
	 */
	private String state;

	/**
	 * Zip code of the entity.
	 */
	private String zipCode;

	/**
	 * Country code of the entity.
	 * See: https://github.com/country-regions/country-region-data/blob/master/data.json
	 */
	private String countryCode;

	/**
	 * Street mailing address of the entity.
	 */
	private String mailingAddressStreet;

	/**
	 * City mailing address of the entity.
	 */
	private String mailingAddressCity;

	/**
	 * State mailing address of the entity.
	 */
	private String mailingAddressState;

	/**
	 * Zip code mailing address of the entity.
	 */
	private String mailingAddressZipCode;

	/**
	 * Country mailing address of the entity.
	 * See: https://github.com/country-regions/country-region-data/blob/master/data.json
	 */
	private String mailingAddressCountry;

	/**
	 * Indicates if the person is registered as an adviser or exempt reporting adviser.
	 */
	private Boolean isSubscriptionAdvisorOrERA;

	/**
	 * Trust type (nullable).
	 */
	private TrustType trustType;
}
