package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents an Individual Investor.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndividualInvestor {

	/**
	 * Id of the individual investor.
	 */
	private UUID id;

	/**
	 * First name of the Investor.
	 */
	private String firstName;

	/**
	 * Last name of the Investor.
	 */
	private String lastName;

	/**
	 * Is the Investor based in the United States?
	 */
	private Boolean isUSBased;

	/**
	 * Investor’s date of birth.
	 */
	private String dateOfBirth;

	/**
	 * Investor’s primary address.
	 */
	private String street1;

	/**
	 * Investor’s secondary address.
	 */
	private String street2;

	/**
	 * City where the Investor resides.
	 */
	private String city;

	/**
	 * 2/3 letter state code of the investor.
	 * Check https://github.com/country-regions/country-region-data/blob/master/data.json for valid codes.
	 */
	private String state;

	/**
	 * Zip code of the Investor.
	 */
	private String zipCode;

	/**
	 * Country code of the investor.
	 * See: https://github.com/country-regions/country-region-data/blob/master/data.json
	 */
	private String countryCode;

	/**
	 * Phone country code (no '+' sign).
	 * Must match one in: https://github.com/mukeshsoni/country-telephone-data
	 */
	private String phoneCountryCode;

	/**
	 * An unformatted phone number excluding country code.
	 * For phone_country_code "1", must be 10 digits.
	 */
	private String phoneNumber;

	/**
	 * Investor’s primary mailing address.
	 */
	private String mailingStreet1;

	/**
	 * Investor’s secondary mailing address.
	 */
	private String mailingStreet2;

	/**
	 * Investor's primary mailing Apt or Suite Number.
	 */
	private String mailingAddressAptSuiteNo;

	/**
	 * Investor's primary mailing address city.
	 */
	private String mailingAddressCity;

	/**
	 * Investor's primary mailing address state.
	 */
	private String mailingAddressState;

	/**
	 * Investor's primary mailing address zip code.
	 */
	private String mailingAddressZipCode;

	/**
	 * Investor's primary mailing address country.
	 * See: https://github.com/country-regions/country-region-data/blob/master/data.json
	 */
	private String mailingAddressCountry;

	/**
	 * Describes if the Investor is subject to backup withholding.
	 */
	private Boolean hasBackupWithholding;

	/**
	 * Investor’s exempt payee code if exempt from backup withholding.
	 */
	private String exemptPayeeCode;

	/**
	 * Is the investor registered as an adviser or exempt reporting adviser?
	 */
	private Boolean isSubscriptionAdvisorOrERA;

	/**
	 * Email address associated with an Investor.
	 */
	private String email;

	/**
	 * Investor’s Social Security Number.
	 */
	private String taxId;

	/**
	 * Investor Passport Number. Required for non-US investors.
	 */
	private String passportNumber;

	/**
	 * Country code of the Investor’s citizenship.
	 * See: https://github.com/country-regions/country-region-data/blob/master/data.json
	 */
	private String citizenship;

	/**
	 * The qualification status of an Investor.
	 */
	private QualifiedStatus qualifiedStatus;

	/**
	 * Investor’s KYC status.
	 */
	private KYCStatus kycStatus;

	/**
	 * Date that KYC expires.
	 */
	private String kycUpdatedAt;
}
