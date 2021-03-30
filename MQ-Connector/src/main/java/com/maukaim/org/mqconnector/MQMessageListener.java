package com.maukaim.org.mqconnector;

public interface MQMessageListener<T> {
    void onMessage(T obj1);
}
