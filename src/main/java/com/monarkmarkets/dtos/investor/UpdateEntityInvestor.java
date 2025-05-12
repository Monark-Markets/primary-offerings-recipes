package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Details for updating an entity investor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEntityInvestor {

	private String legalName;
	private String dbaName;
	private Integer beneficialOwnershipCount;
	private Boolean isUSBased;
	private String phone;
	private String phoneCountryCode;
	private EntityType entityType;
	private String jurisdiction;
	private OffsetDateTime formationDate;
	private TaxClassification taxClassification;
	private String taxId;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String countryCode;
	private String mailingAddressStreet;
	private String mailingAddressCity;
	private String mailingAddressState;
	private String mailingAddressZipCode;
	private String mailingAddressCountry;
	private Boolean isSubscriptionAdvisorOrERA;
	private TrustType trustType;
}
