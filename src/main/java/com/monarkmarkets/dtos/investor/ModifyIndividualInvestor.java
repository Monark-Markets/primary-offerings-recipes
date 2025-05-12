package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details for modifying an individual investor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyIndividualInvestor {

	private String firstName;
	private String lastName;
	private Boolean isUSBased;
	private String dateOfBirth;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zipCode;
	private String countryCode;
	private String phoneCountryCode;
	private String phoneNumber;
	private String mailingStreet1;
	private String mailingStreet2;
	private String mailingAddressAptSuiteNo;
	private String mailingAddressCity;
	private String mailingAddressState;
	private String mailingAddressZipCode;
	private String mailingAddressCountry;
	private Boolean hasBackupWithholding;
	private String exemptPayeeCode;
	private Boolean isSubscriptionAdvisorOrERA;
	private String email;
	private String taxId;
	private String passportNumber;
	private String citizenship;
	private QualifiedStatus qualifiedStatus;
}
