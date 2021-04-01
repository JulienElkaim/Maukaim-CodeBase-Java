package com.maukaim.org.fixengine;

import com.maukaim.org.fixengine.messages.FixOrderRequest;
import quickfix.ConfigError;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface FixEngineConnector {
    boolean isConnected();
    boolean connect() throws InterruptedException, ConfigError, TimeoutException, ExecutionException, FileNotFoundException;
    void disconnect();
    String postOrder(FixOrderRequest orderRequest) throws Exception;
}
