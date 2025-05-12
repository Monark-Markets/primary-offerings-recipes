package com.monarkmarkets.dtos.investorsubscriptionaction;

/**
 * Type of action required for the subscription.
 */
public enum ActionType {
	Other,
	DocumentSign,
	DocumentAcknowledge,
	CashMovement,
	ApiCall,
	TextAcknowledge,
	KYCAML
}
