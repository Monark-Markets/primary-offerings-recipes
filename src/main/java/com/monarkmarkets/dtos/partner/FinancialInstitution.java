package com.monarkmarkets.dtos.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a Financial Institution entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialInstitution {

	/**
	 * Unique ID associated with a FinancialInstitution.
	 */
	private UUID id;

	/**
	 * The partner associated with this FinancialInstitution.
	 */
	private UUID partnerId;

	/**
	 * Legal name of the financial institution.
	 */
	private String legalName;

	/**
	 * CRD number associated with this financial institution.
	 */
	private String crdNumber;

	/**
	 * The type of Financial Institution.
	 */
	private FinancialInstitutionType financialInstitutionType;

	/**
	 * Indicates if this is the default Financial Institution for the Partner.
	 */
	private boolean isDefault;

	/**
	 * City where the FinancialInstitution resides.
	 */
	private String city;

	/**
	 * State of the FinancialInstitution.
	 */
	private String state;

	/**
	 * Zip code of the FinancialInstitution.
	 */
	private String zipCode;

	/**
	 * Country code of the FinancialInstitution.
	 */
	private String countryCode;

	/**
	 * Date the FinancialInstitution was created.
	 */
	private String createdAt;

	/**
	 * Date the FinancialInstitution was last updated.
	 */
	private String updatedAt;

	/**
	 * Linked Partner entity.
	 */
	private Partner partner;
}
