package com.maukaim.org.mqconnector.adapters.swift.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MQSwiftMessageMT900 extends MQSwiftMessageMT9XX implements MQSwiftMessage{
    private LocalDateTime movementDateTime;
    private Double movementAmount;

    @Override
    public SwiftMessageType getMessageType() {
        return SwiftMessageType.MT900;
    }
}
