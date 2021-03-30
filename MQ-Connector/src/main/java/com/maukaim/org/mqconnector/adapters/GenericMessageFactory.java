package com.maukaim.org.mqconnector.adapters;

import com.maukaim.org.mqconnector.MQException;

import javax.jms.Message;

public interface GenericMessageFactory<T> {
    T build(Message message) throws MQException;
}
