package com.monarkmarkets.dtos.preipocompany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents the information required to create a PreIPOCompany for a Primary Offering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePreIPOCompany {

    /**
     * The name of the PreIPOCompany.
     * (Required, minLength: 1, maxLength: 100)
     */
    private String name;

    /**
     * Domain URL of the PreIPOCompany.
     * (Required, minLength: 1, maxLength: 200)
     */
    private String domain;

    /**
     * The LinkedIn profile URL associated with the PreIPOCompany.
     * (Nullable, maxLength: 200)
     */
    private String linkedIn;

    /**
     * The PreIPOCompany’s X profile URL.
     * (Nullable, maxLength: 200)
     */
    private String x;

    /**
     * The Facebook profile URL of the PreIPOCompany.
     * (Nullable, maxLength: 200)
     */
    private String facebook;

    /**
     * Country where the PreIPOCompany is located.
     * (Nullable, maxLength: 50)
     */
    private String country;

    /**
     * PreIPOCompany’s address (headquarters).
     * (Nullable, maxLength: 500)
     */
    private String address;

    /**
     * The telephone number of the PreIPOCompany.
     * (Nullable)
     */
    private String phoneNumber;

    /**
     * The phone country code of the PreIPOCompany.
     * (Nullable)
     */
    private String phoneCountryCode;

    /**
     * Name of the contact for the PreIPOCompany.
     * (Required, minLength: 1, maxLength: 225)
     */
    private String contactName;

    /**
     * Email address for the contact person at the PreIPOCompany.
     * (Required, maxLength: 225, pattern: email format)
     */
    private String contactEmail;

    /**
     * Type of entity of the PreIPOCompany.
     * (Enum, Required)
     */
    private CompanyType type;

    /**
     * The year the PreIPOCompany was established.
     * (Nullable, format: date)
     */
    private LocalDate yearEst;

    /**
     * Short description of the PreIPOCompany.
     * (Required, minLength: 1, maxLength: 1000)
     */
    private String description;

    /**
     * The founder of the PreIPOCompany.
     * (Nullable, maxLength: 200)
     */
    private String founder;

    /**
     * A URL link associated with the PreIPOCompany’s logo.
     * (Nullable, maxLength: 200)
     */
    private String logoURL;

    /**
     * PreIPOCompany total funding, expressed in USD.
     * (Nullable, format: double)
     */
    private Double totalFunding;

    /**
     * The most recent funding series.
     * (Nullable, maxLength: 200)
     */
    private String lastFundingSeries;

    /**
     * The total size of the last funding round, expressed in USD.
     * (Nullable, format: double)
     */
    private Double lastFundingTotal;

    /**
     * PreIPOCompany total number of funding rounds.
     * (Nullable, format: double)
     */
    private Double totalFundingRounds;

    /**
     * The date of the PreIPOCompany’s most recent funding.
     * (Nullable, format: date)
     */
    private LocalDate lastFundingDate;

    /**
     * The type (Equity or Debt) of the last funding round.
     * (Nullable, maxLength: 50)
     */
    private String lastFundingType;

    /**
     * The number of investors in a PreIPOCompany.
     * (Nullable, format: double)
     */
    private Double numInvestors;

    /**
     * The number of jobs in the PreIPOCompany.
     * (Nullable, format: double)
     */
    private Double numJobs;

    /**
     * Revenue generated in the previous 12 months by this PreIPOCompany.
     * (Nullable, format: double)
     */
    private Double twelveMonthTrailingRevenue;

    /**
     * Most recent valuation of the PreIPOCompany, based on the most recent funding round (in USD).
     * (Nullable, format: double)
     */
    private Double lastValuation;

    /**
     * The PreIPOCompany’s share price at the most recent funding round (in USD).
     * (Nullable, format: double)
     */
    private Double lastSharePrice;

    /**
     * The number of customers serviced by the PreIPOCompany.
     * (Nullable, format: double)
     */
    private Double numCustomers;

    /**
     * The legal name of the PreIPOCompany.
     * (Nullable, maxLength: 100)
     */
    private String legalName;

    /**
     * The executive team members of the PreIPOCompany.
     * (Nullable, list of strings)
     */
    private List<String> executiveTeam;

    /**
     * The number of shares that have been issued by the PreIPOCompany.
     * (Nullable, format: double)
     */
    private Double numSharesOutstanding;

    /**
     * A list of notable investors in a PreIPOCompany.
     * (Nullable, list of strings)
     */
    private List<String> notableInvestors;

    /**
     * A list of the notable investors’ logos associated with the PreIPOCompany.
     * (Nullable, list of strings)
     */
    private List<String> notableInvestorsLogos;

    /**
     * 1-3 letter state/region code of the PreIPOCompany.
     * (Nullable)
     */
    private String jurisdiction;
}
