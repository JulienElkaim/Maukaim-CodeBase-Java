package com.maukaim.org.fixengine;

public class FixEngineException extends Exception {
    FixEngineException(String message) {super(message);}
    FixEngineException(Exception exc) {super(exc);}
    FixEngineException(Throwable throwable) {super(throwable);}
}
