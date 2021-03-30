package com.maukaim.org.mqconnector.adapters.swift.model;

import java.time.LocalDateTime;

public interface MQSwiftMessage {
    SwiftMessageType getMessageType();
    String getRawMessage();
    LocalDateTime getReceptionTime();
    String getSwiftMessageId();
}
