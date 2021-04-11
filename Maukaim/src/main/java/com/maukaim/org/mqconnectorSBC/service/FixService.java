package com.maukaim.org.mqconnectorSBC.service;

import com.maukaim.org.fixengine.application.FixEngineEventListener;

public interface FixService extends FixEngineEventListener {
    void postOrderExample() throws Exception;
}
