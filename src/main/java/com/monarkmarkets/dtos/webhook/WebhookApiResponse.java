package com.monarkmarkets.dtos.webhook;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for Webhook.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookApiResponse {

	/**
	 * List of results returned.
	 */
	private List<Webhook> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
