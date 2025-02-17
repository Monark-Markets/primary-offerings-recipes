package com.monarkmarkets.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

/**
 * Represents a document associated with the fund admin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

	/**
	 * Unique identifier for the document.
	 */
	private UUID id;

	/**
	 * The name of the Document.
	 */
	private String name;

	/**
	 * URL link to view the document.
	 */
	@NonNull
	private String url;

	/**
	 * Unique identifier of the associated investor, if any.
	 */
	private UUID investorId;

	/**
	 * Unique identifier of the associated PreIPOCompany, if any.
	 */
	private UUID preIPOCompanyId;

	/**
	 * Unique identifier of the associated PreIPOCompanyInvestment, if any.
	 */
	private UUID preIPOCompanyInvestmentId;

	/**
	 * Unique identifier of the associated PreIPOCompanySPV, if any.
	 */
	private UUID preIPOCompanySPVId;

	/**
	 * Unique identifier of the associated InvestorSubscription, if any.
	 */
	private UUID investorSubscriptionId;

	/**
	 * Unique identifier of the associated partner, if any.
	 */
	private UUID partnerId;

	/**
	 * The type of Document.
	 */
	@NonNull
	private Type type;

	/**
	 * Boolean value to dictate if this is a template.
	 */
	private Boolean templateDocument;

	/**
	 * Optional tax year to tag the document with.
	 */
	private Integer taxYear;

	/**
	 * Description of the Document.
	 */
	private String description;

	public enum Type {
		BUSINESS_FORMATION,
		BANK_STATEMENT,
		CAPITAL_ACCT_STATEMENT,
		CAPITAL_CALL_NOTICE,
		CARRY_SPLIT_AGREEMENT,
		CERTIFICATE_OF_FORMATION,
		COMMITMENT_AGREEMENT,
		COMPLIANCE,
		DRIVER_LICENSE,
		DRAFT_INVESTMENT,
		EIN_FORMATION,
		FINAL_INVESTMENT,
		FINANCIAL_STATEMENT,
		INVESTOR_WIRE_INSTRUCTIONS,
		INVOICE,
		K1,
		LOCAL_OR_TRIBE_ID,
		MASTER_AGREEMENT,
		MSA,
		OTHER,
		PASSPORT,
		PASSPORT_FOREIGN,
		PASSPORT_US,
		PITCH_DECK,
		POST_CLOSING_DOCUMENTS,
		PROOF_OF_SOURCE_OF_FUNDS,
		PX_FILE,
		SCFUND_AGREEMENT,
		SERIES_AGREEMENT,
		SIDE_LETTER,
		SIGNABLE_AGREEMENT,
		SOW,
		STATEID,
		SUBSCRIPTION_AGREEMENT,
		SUBSCRIPTION_AGREEMENT_COUNTER_SIGNED,
		SUBSCRIPTION_INCREASE_AMOUNT_ADDENDUM,
		TARGET_CAPTABLE,
		TERM_SHEET,
		TRANSFER_CONSENT_FORM,
		TRANSFER_OF_INTEREST_AGREEMENT,
		W8,
		W8_BEN,
		W8_BENE,
		W9,
		LayeredSPVInvestmentDeck_Monark,
		InvestmentMemo_Monark,
		DealMemo_Monark,
		RiskFactors_Monark,
		PrivatePlacementMemorandum_Monark,
		InvestorLogo_Monark
	}
}