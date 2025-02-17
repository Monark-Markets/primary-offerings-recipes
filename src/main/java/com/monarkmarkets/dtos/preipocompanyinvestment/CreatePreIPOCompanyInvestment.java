package com.monarkmarkets.dtos.preipocompanyinvestment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents an investment in a Pre-IPO company.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePreIPOCompanyInvestment {

    /**
     * The pre-IPO company entity ID attached to this investment.
     * (Required, format: UUID)
     */
    private UUID preIPOCompanyID;

    /**
     * Indicates the type of shares for the Pre-IPOCompanyInvestment.
     * (Required, minLength: 1)
     */
    private String shareType;

    /**
     * Share class of PreIPOCompany.
     * (Required, minLength: 1)
     */
    private String shareClass;

    /**
     * The total number of shares in the Pre-IPO company associated with this investment.
     * (Nullable, format: double)
     */
    private Double numberOfShares;

    /**
     * The net amount of capital, including all commissions and fees associated with this investment.
     * (Nullable, format: double)
     */
    private Double totalCapitalRaise;

    /**
     * This is the total amount that will be sent to the seller or issuer, after all commissions and fees have been paid.
     * (Minimum: 0, format: double)
     */
    private Double investmentSize;

    /**
     * The price per share of the PreIPOCompany that will be paid to acquire the shares.
     * (Nullable, format: double)
     */
    private Double pricePerShare;

    /**
     * The type of transaction (Primary, Secondary, TenderOffer).
     * (Enum, Required)
     */
    private TransactionType transactionType;

    /**
     * Describes whether or not the PreIPOCompanyInvestment is taking place through a Layered SPV.
     * (Nullable)
     */
    private LayeredSPV layeredSPV;
}
