package com.maukaim.org.maukaim.service;

import com.maukaim.org.mqconnector.MQExceptionListener;
import com.maukaim.org.mqconnector.MQMessageListener;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;

public interface SwiftService extends MQExceptionListener, MQMessageListener<MQSwiftMessage> {
}
