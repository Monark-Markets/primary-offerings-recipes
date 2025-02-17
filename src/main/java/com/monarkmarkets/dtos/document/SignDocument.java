package com.monarkmarkets.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Model for signing a document.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignDocument {

	/**
	 * The investorId associated with the document.
	 */
	private UUID investorId;

	/**
	 * Metadata about the signing.
	 */
	private String metadata;
}