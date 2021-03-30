package com.maukaim.org.mqconnector.adapters;

import com.maukaim.org.mqconnector.MQException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

public abstract class UtilsObjectMessageFactory {
    /**
     * Common method to every factories that will have to handle JMS Messages.
     */
    protected String translateMessage(Message message) throws JMSException {
        String result = "Message was null...";
        if(message != null){
            if(message instanceof TextMessage){
                result = ((TextMessage) message).getText();
            }else if(message instanceof BytesMessage){
                BytesMessage bm = (BytesMessage) message;
                byte[] byteArr = new byte[(int) bm.getBodyLength()];
                for (int i = 0; i < (int) bm.getBodyLength(); i++){
                    byteArr[i] = bm.readByte();
                }
                result = new String(byteArr);
            }else{
                String errMsg = String.format("Unable to find Instance Type for message :%s", message.toString());
                throw new MQException(errMsg);
            }
        }
        return result;
    }

    protected LocalDate toLocalDate(Calendar dateAsCalendar){
        return LocalDate.ofInstant(dateAsCalendar.toInstant(), ZoneId.systemDefault());
    }

}
