package com.maukaim.org.fixengineSBC.service;

import com.maukaim.org.fixengine.application.FixEngineEventListener;

public interface FixService extends FixEngineEventListener {
    void postOrderExample() throws Exception;
}
