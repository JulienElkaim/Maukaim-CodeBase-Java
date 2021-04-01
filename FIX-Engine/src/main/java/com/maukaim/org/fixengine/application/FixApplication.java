package com.maukaim.org.fixengine.application;

import lombok.Getter;
import quickfix.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.MessageCracker;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Logging done with stdout, waiting for a real Logger to be added (lombok?)
 */
public class FixApplication extends MessageCracker implements Application {
    @Getter
    private static volatile SessionID sessionID;

    private FixEngineEventListener fixEngineEventListener;
    AtomicBoolean isLoggedOn = new AtomicBoolean(false);

    public FixApplication(FixEngineEventListener fixEngineEventListener){
        this.fixEngineEventListener = fixEngineEventListener;
    }

    public boolean isLoggedOn(){
        return isLoggedOn.get();
    }


    @Override
    public void onCreate(SessionID sessionId) {
        if(FixApplication.sessionID == null){
            System.out.println(
                    String.format("INFO::: Creating new session: %s", sessionID));
        } else {
            System.out.println("WARN::: Connecting with a new session while an previous is still active.");
        }
        FixApplication.sessionID = sessionID;
    }

    @Override
    public void onLogon(SessionID sessionId) {
        if(FixApplication.sessionID == null){
            System.out.println("WARN::: Log on tentative but no session existing.");
        }else if(FixApplication.sessionID.equals(sessionID)){
            isLoggedOn.set(true);
            System.out.println("INFO::: LOG on on active session" + sessionID);
        } else {
            System.out.println("WARN::: Log on tentative on another session than the active one.");
        }

    }

    @Override
    public void onLogout(SessionID sessionId) {
        if(FixApplication.sessionID.equals(sessionId)){
            isLoggedOn.set(false);
            System.out.println("INFO::: Logging out.");
        }else{
            System.out.println("WARN::: Log out tentative on session which is not the active one.");
        }
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        System.out.println("INFO::: [ADMIN] sending : " + message.toString());

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("INFO::: [ADMIN] receiving : " + message.toString());
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        System.out.println("INFO::: Sending : " + message.toString());
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        System.out.println("INFO::: Receiving " + message.toString());
        crack(message, sessionId);
    }

    @Override
    public void onMessage(ExecutionReport message, SessionID sessionID) throws FieldNotFound{
        System.out.println("INFO::: ExecutionReport -> " + message.toString());
        this.fixEngineEventListener.onExecutionReport(message);
    }

}
