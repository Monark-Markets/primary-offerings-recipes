package com.monarkmarkets.dtos.preipocompanynews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO representing a single article within PreIPOCompanyNews.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreIPOCompanyNewsArticle {

	/**
	 * The entity ID attached to this article.
	 */
	private UUID id;

	/**
	 * The pre-IPO company news ID this article is linked to.
	 */
	private UUID preIPOCompanyNewsId;

	/**
	 * The article ID.
	 */
	private String articleId;

	/**
	 * Link to the article.
	 */
	private String link;

	/**
	 * The headline of the article.
	 */
	private String headline;

	/**
	 * Metadata of the article.
	 */
	private String shortSummary;

	/**
	 * Publication of the article.
	 */
	private String publication;

	/**
	 * The source of the article.
	 */
	private String source;

	/**
	 * The date the article was published.
	 */
	private String datePublished;
}
