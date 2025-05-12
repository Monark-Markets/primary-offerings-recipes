package com.monarkmarkets.dtos.webhook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Represents a Webhook Event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEvent {

	/**
	 * Unique ID associated with a WebhookEvent.
	 */
	private UUID id;

	/**
	 * The Webhook ID that generated this event.
	 */
	private UUID webhookId;

	/**
	 * Delivery status of the event.
	 */
	private DeliveryStatus deliveryStatus;

	/**
	 * Type of the webhook event.
	 */
	private EventType eventType;

	/**
	 * The Webhook Event data.
	 */
	private String data;

	/**
	 * Created at the specified date.
	 */
	private OffsetDateTime createdAt;

	/**
	 * Delivered at the specified date.
	 */
	private OffsetDateTime deliveredAt;
}
