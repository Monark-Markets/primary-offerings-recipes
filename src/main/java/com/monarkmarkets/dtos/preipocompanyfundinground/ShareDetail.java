package com.monarkmarkets.dtos.preipocompanyfundinground;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ShareDetail for the PreIPOCompanyFundingRound.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareDetail {

	/**
	 * The class of the shares issued.
	 */
	private String shareClass;

	/**
	 * The type of shares (e.g., Preferred, Common).
	 */
	private String shareType;

	/**
	 * Number of shares issued in this detail.
	 */
	private BigDecimal numberOfShares;

	/**
	 * Price per share.
	 */
	private BigDecimal pricePerShare;
}
