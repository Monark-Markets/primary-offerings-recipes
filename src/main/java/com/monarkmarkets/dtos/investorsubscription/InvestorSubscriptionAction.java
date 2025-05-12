package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for tracking actions associated with an InvestorSubscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscriptionAction {

	/**
	 * Unique ID of the action.
	 */
	private UUID id;

	/**
	 * ID of the associated InvestorSubscription.
	 */
	private UUID investorSubscriptionId;

	/**
	 * Name of this action.
	 */
	private String name;

	/**
	 * Description of the action.
	 */
	private String description;

	/**
	 * Optional Data ID related to the action.
	 */
	private String dataId;

	/**
	 * Current status of the action.
	 */
	private InvestorSubscriptionActionStatus status;

	/**
	 * Type of action to be performed.
	 */
	private InvestorSubscriptionActionType type;

	/**
	 * Who is responsible for acknowledging completion.
	 */
	private ResponsibleParty responsibleParty;

	/**
	 * Text content if this is a text acknowledgement.
	 */
	private String actionText;
}
