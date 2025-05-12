package com.monarkmarkets.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private DocumentType type;

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
}
