package com.monarkmarkets.dtos.investorsubscription;

/**
 * The type of action required by the Investor.
 */
public enum InvestorSubscriptionActionType {
	Other,
	DocumentSign,
	DocumentAcknowledge,
	CashMovement,
	ApiCall,
	TextAcknowledge,
	KYCAML
}
