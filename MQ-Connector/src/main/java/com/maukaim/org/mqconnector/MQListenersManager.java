package com.maukaim.org.mqconnector;

import javax.jms.ExceptionListener;
import javax.jms.MessageListener;

public interface MQListenersManager<T> extends MessageListener, ExceptionListener {
    boolean addMessageListener(MQMessageListener<T> m1);
    boolean addExceptionListener(MQExceptionListener e1);
}
