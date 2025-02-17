package com.monarkmarkets.dtos.investorsubscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSubscriptionAction {

	private UUID id;
	private UUID investorSubscriptionId;

	@NonNull
	private String name;

	private String description;
	private String dataId;

	@NonNull
	private Status status;

	@NonNull
	private Type type;

	@NonNull
	private ResponsibleParty responsibleParty;

	public enum Status {
		Pending,
		Complete
	}

	public enum Type {
		Other,
		DocumentSign,
		DocumentAcknowledge,
		CashMovement,
		ApiCall,
		KYCAML
	}

	public enum ResponsibleParty {
		Partner,
		Monark
	}
}