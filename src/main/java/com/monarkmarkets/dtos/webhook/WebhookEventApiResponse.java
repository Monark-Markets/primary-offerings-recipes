package com.monarkmarkets.dtos.webhook;

import com.monarkmarkets.dtos.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API Response wrapper for WebhookEvent.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEventApiResponse {

	/**
	 * List of results returned.
	 */
	private List<WebhookEvent> items;

	/**
	 * Pagination metadata.
	 */
	private Pagination pagination;
}
