package com.maukaim.org.fixengine.application;

import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;

public interface FixEngineEventListener {
    void onExecutionReport(ExecutionReport executionReport) throws FieldNotFound;
}
