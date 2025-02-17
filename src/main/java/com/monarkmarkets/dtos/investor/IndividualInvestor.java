package com.monarkmarkets.dtos.investor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndividualInvestor {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String phoneCountryCode;
	private String taxId;

	@JsonProperty("isUSBased")
	private Boolean usBased;

	@JsonProperty("isSubscriptionAdvisorOrERA")
	private Boolean subscriptionAdvisorOrERA;
	private Boolean hasBackupWithholding;
	private String passportNumber;
	private String citizenship;
	private String countryCode;
	private LocalDate dateOfBirth;
	private String street1;
	private String street2;
	private String mailingStreet1;
	private String mailingStreet2;
	private String city;
	private String state;
	private String zipCode;
	private String exemptPayeeCode;
}