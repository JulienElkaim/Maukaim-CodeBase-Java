package com.maukaim.org.utils.service;

import com.maukaim.org.mqconnector.MQExceptionListener;
import com.maukaim.org.mqconnector.MQMessageListener;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;

public interface SwiftService extends MQExceptionListener, MQMessageListener<MQSwiftMessage> {
}
