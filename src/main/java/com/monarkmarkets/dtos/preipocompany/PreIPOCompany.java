package com.monarkmarkets.dtos.preipocompany;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreIPOCompany {
	private UUID id;
	private String name;
	private String domain;
	private String linkedIn;
	private String x;
	private String facebook;
	private String country;
	private String address;
	private String contactName;
	private String contactEmail;
	private CompanyType type;
	private LocalDate yearEst;
	private long numJobs;
	private String description;
	private String founder;
	private String logoURL;
	private long totalFunding;
	private String lastFundingSeries;
	private long lastFundingTotal;
	private long totalFundingRounds;
	private LocalDate lastFundingDate;
	private String lastFundingType;
	private long numInvestors;
	private long revenueRange;
	private long lastValuation;
	private double lastSharePrice;
	private long numCustomers;
	private List<String> executiveTeam;
	private long numSharesOutstanding;
	private List<String> notableInvestors;
	private List<String> notableInvestorsLogos;
	private double twelveMonthTrailingRevenue;

	// Newly added fields
	private String phoneNumber;
	private String phoneCountryCode;
	private String legalName;
	private String jurisdiction;
	private LocalDate createdAt;
	private LocalDate updatedAt;
}
