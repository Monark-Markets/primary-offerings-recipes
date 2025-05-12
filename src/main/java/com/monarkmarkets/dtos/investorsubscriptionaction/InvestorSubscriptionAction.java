package com.monarkmarkets.dtos.investorsubscriptionaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents an action that needs to be performed as part of an Investor Subscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscriptionAction {

	/**
	 * Unique ID associated with an InvestorSubscriptionAction.
	 */
	private UUID id;

	/**
	 * Investor SubscriptionId for the InvestorSubscriptionAction record.
	 */
	private UUID investorSubscriptionId;

	/**
	 * The name of this InvestorSubscriptionAction.
	 */
	private String name;

	/**
	 * A description for this action.
	 */
	private String description;

	/**
	 * Optional DataId for related object.
	 */
	private String dataId;

	/**
	 * The current status of this InvestorSubscriptionAction.
	 */
	private ActionStatus status;

	/**
	 * The type of action required to be performed by the Investor.
	 */
	private ActionType type;

	/**
	 * This describes the entity responsible for acknowledging that the Action has been completed.
	 */
	private ResponsibleParty responsibleParty;

	/**
	 * Text for an action that is a text acknowledgement.
	 */
	private String actionText;
}
