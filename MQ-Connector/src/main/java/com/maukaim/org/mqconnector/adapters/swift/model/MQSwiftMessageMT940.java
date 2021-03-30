package com.maukaim.org.mqconnector.adapters.swift.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MQSwiftMessageMT940 extends MQSwiftMessageMT9XX implements MQSwiftMessage{
    private LocalDate valueDate;
    private String additionalInformations;
    private Double openingBalance;
    private Double closingBalance;
    private Double closingAvailableBalance;

    @Override
    public SwiftMessageType getMessageType() {
        return SwiftMessageType.MT940;
    }
}
