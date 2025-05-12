package com.monarkmarkets.dtos.preipocompanyinvestment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details about the layered SPV structure.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LayeredSPV {

	/**
	 * Whether the SPV is layered.
	 */
	private Boolean enabled;

	/**
	 * Description of how the layering is structured.
	 */
	private String structure;

	/**
	 * Additional notes.
	 */
	private String notes;
}
