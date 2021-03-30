package com.maukaim.org.mqconnector;

public interface MQClientConnector<T> {
    void connect() throws MQException;
    void disconnect() throws MQException;
    boolean addMessageListener(MQMessageListener<T> e1);
    boolean addExceptionListener(MQExceptionListener e1);

    boolean isConnected();
}
