package com.monarkmarkets.dtos.preipocompanyfundinground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Citation associated with a funding round.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Citation {

	/**
	 * Description or summary of the citation.
	 */
	private String description;

	/**
	 * Date and time the citation was recorded or published.
	 */
	private String timestamp;

	/**
	 * The type/source of the citation (e.g. news, filing, etc.).
	 */
	private String source;
}
