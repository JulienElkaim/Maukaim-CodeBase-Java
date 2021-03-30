package com.maukaim.org.mqconnector;

import javax.jms.JMSException;

public class MQException extends JMSException{
    public MQException(String reason, String errorCode) {
        super(reason, errorCode);
    }

    public MQException(String reason) {
        super(reason);
    }

    public MQException(JMSException ex){
        super(ex.getMessage(), ex.getErrorCode());
    }
}
