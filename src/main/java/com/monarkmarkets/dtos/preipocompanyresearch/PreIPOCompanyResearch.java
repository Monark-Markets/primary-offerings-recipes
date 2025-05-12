package com.monarkmarkets.dtos.preipocompanyresearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a research document associated with a PreIPOCompany.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyResearch {

	/**
	 * The unique ID attached to this research document.
	 */
	private UUID id;

	/**
	 * The ID of the PreIPOCompany associated with this research document.
	 */
	private UUID preIPOCompanyId;

	/**
	 * The research document id.
	 */
	private String researchDocumentId;

	/**
	 * The title of the research document.
	 */
	private String title;

	/**
	 * Link to the research document.
	 */
	private String link;

	/**
	 * Date the document was published.
	 */
	private String publishedDate;

	/**
	 * Type of research document.
	 */
	private ResearchType type;

	/**
	 * Relation of entity of the research document.
	 */
	private ResearchRelation relation;

	/**
	 * Metadata of the research document.
	 */
	private String previewMeta;

	/**
	 * Third party data service providing access to this research document.
	 */
	private String source;

	/**
	 * A detailed description of a document, including all of its contents.
	 */
	private String documentJson;
}
