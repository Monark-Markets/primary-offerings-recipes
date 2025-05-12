package com.monarkmarkets.dtos.investor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Update Investor Accreditation information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvestorAccreditation {

	/**
	 * Unique ID of the investor.
	 */
	private UUID investorId;

	/**
	 * Accreditation status.
	 */
	private AccreditationStatus accreditationStatus;
}
