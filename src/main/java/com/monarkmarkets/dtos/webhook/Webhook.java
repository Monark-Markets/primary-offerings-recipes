package com.monarkmarkets.dtos.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents a Webhook configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Webhook {

	/**
	 * Unique ID associated with a Webhook.
	 */
	private UUID id;

	/**
	 * ID of the Partner associated with an Investor.
	 */
	private UUID partnerId;

	/**
	 * The delivery URL for the webhook.
	 */
	private String url;

	/**
	 * Flag to dictate if the webhook should be sent events.
	 */
	private Boolean isActive;

	/**
	 * Token value used to create a signature on webhook delivery.
	 */
	private String token;

	/**
	 * Created at the specified date.
	 */
	private OffsetDateTime createdAt;

	/**
	 * Updated at the specified date.
	 */
	private OffsetDateTime updatedAt;
}
