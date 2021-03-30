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
public abstract class MQSwiftMessageMT9XX extends MQSwiftMessageMTXXX{
    public String accountId;
    public String currency;
}
