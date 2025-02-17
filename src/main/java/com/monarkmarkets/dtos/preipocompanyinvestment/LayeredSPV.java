package com.monarkmarkets.dtos.preipocompanyinvestment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Describes the details of a Layered SPV.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayeredSPV {

	/**
	 * The legal name of the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (minLength: 1)
	 */
	private String layeredSPVName;

	/**
	 * 1-3 letter state/region code of the PreIPOCompany.
	 * (minLength: 1, maxLength: 3)
	 */
	private String jurisdiction;

	/**
	 * Describes if the manager of the Layered SPV is capable of delivering shares upon an IPO.
	 */
	private Boolean canDeliverShared;

	/**
	 * The management fee charged by the manager of the Layered SPV, expressed as a percentage.
	 * (Format: double, nullable)
	 */
	private Double managementFee;

	/**
	 * Indicates how often the management fee is charged (e.g., "Annual").
	 * (Nullable)
	 */
	private String managementFeeFrequency;

	/**
	 * Carried interest charged by the Layered SPV.
	 * (Format: double, nullable)
	 */
	private Double carriedInterest;

	/**
	 * An array of names of persons or entities managing the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (minItems: 1)
	 */
	private List<String> managerNames;

	/**
	 * Specifies the type of manager, such as "RIA" (Registered Investment Advisor).
	 * This field is not nullable and intended for internal use only.
	 */
	private ManagerType managerType;

	/**
	 * The email address of the manager of the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (minLength: 1, format: email)
	 */
	private String managerEmail;

	/**
	 * The phone number of the manager of the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (minLength: 1, format: tel)
	 */
	private String managerPhone;

	/**
	 * The name of the general partner serving on the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (minLength: 1)
	 */
	private String gpName;

	/**
	 * The date when the Layered SPV was structured.
	 * This field is not nullable.
	 * (Format: date)
	 */
	private LocalDate inceptionDate;

	/**
	 * Describes the fund structure, such as "3(c)(1)" or "3(c)(7)."
	 * This field is not nullable.
	 */
	private FundStructure fundStructure;

	/**
	 * The number of seats already taken in the receiving SPV.
	 * This field is not nullable.
	 * (Format: int32, minimum: 0)
	 */
	private Integer currentBeneficialOwnershipCount;

	/**
	 * The total number of shares issued by the Layered SPV.
	 * This number may be used to calculate the 10% threshold for triggering a look-through provision.
	 * (Format: double, minimum: 0)
	 */
	private Double spvOutstandingShareCount;

	/**
	 * The size, in number of shares in the Layered SPV, of the position involved in this transaction.
	 * (Format: double, minimum: 0)
	 */
	private Double lpStakeShareCount;

	/**
	 * The portion of the whole SPV represented by the LP stake being sold,
	 * represented as a percentage.
	 * (Format: double, minimum: 0, maximum: 100)
	 */
	private Double lpStakePercentage;

	/**
	 * Indicates whether the look-through provision will be triggered.
	 * This field is not nullable.
	 */
	private Boolean lookThroughProvision;

	/**
	 * A URL link to the offering circular or private placement memorandum.
	 * This field is nullable and intended for internal use only.
	 * (Format: uri)
	 */
	private String offeringCircularUrl;

	/**
	 * A URL link to the subscription agreement for the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (Format: uri)
	 */
	private String subscriptionAgreementUrl;

	/**
	 * A URL link to the operating agreement for the Layered SPV.
	 * This field is not nullable and intended for internal use only.
	 * (Format: uri)
	 */
	private String operatingAgreementUrl;

	/**
	 * Specifies the tax form associated with the investment, such as "K-1."
	 * This field is not nullable and intended for internal use only.
	 */
	private TaxForm taxForm;

	/**
	 * Indicates whether the Layered SPV is compliant.
	 */
	private Boolean isCompliant;

	// ========= Enumerated Types =========

	public enum ManagerType {
		RIA("RIA"),
		BROKER_DEALER("BrokerDealer"),
		OTHER("Other");

		private final String value;

		ManagerType(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@JsonCreator
		public static ManagerType fromString(String key) {
			for (ManagerType type : values()) {
				if (type.value.equalsIgnoreCase(key)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Invalid ManagerType: " + key);
		}
	}

	public enum FundStructure {
		THREE_C_ONE("ThreeCOne"),
		THREE_C_SEVEN("ThreeCSeven");

		private final String value;

		FundStructure(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@JsonCreator
		public static FundStructure fromString(String key) {
			for (FundStructure type : values()) {
				if (type.value.equalsIgnoreCase(key)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Invalid FundStructure: " + key);
		}
	}

	public enum TaxForm {
		K1("K1"),
		FORM_1099("Form1099"),
		OTHER("Other");

		private final String value;

		TaxForm(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@JsonCreator
		public static TaxForm fromString(String key) {
			for (TaxForm type : values()) {
				if (type.value.equalsIgnoreCase(key)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Invalid TaxForm: " + key);
		}
	}
}
