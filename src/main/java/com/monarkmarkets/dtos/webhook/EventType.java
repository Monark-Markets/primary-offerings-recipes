package com.monarkmarkets.dtos.webhook;

/**
 * Type of webhook event.
 */
public enum EventType {
	PreIPOCompany,
	PreIPOCompanyInvestment,
	PreIPOCompanySPV,
	Investor,
	Document,
	InvestorSubscriptionAction,
	SubscriptionStatus
}
