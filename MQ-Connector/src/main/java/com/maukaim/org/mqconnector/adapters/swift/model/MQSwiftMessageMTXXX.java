package com.maukaim.org.mqconnector.adapters.swift.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public abstract class MQSwiftMessageMTXXX {
    public String rawMessage;
    public LocalDateTime receptionTime;
    public String swiftMessageId;
}
