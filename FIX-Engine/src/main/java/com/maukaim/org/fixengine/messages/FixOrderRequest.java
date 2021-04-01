package com.maukaim.org.fixengine.messages;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class FixOrderRequest {
    private String orderIdentifier;
    private String way;
    private double nominalValue;
    private String cusip;
    private LocalDate settlementDate;
    private String customerAc;
    private String traderWebUser;

    private int partyRole;
    private char partyIDSource;
    private String senderCompID;
    private String targetComID;
    private String senderLocationID;
    private char timeInForce;
    private String symbol;
    private String securityIDSource;
}
