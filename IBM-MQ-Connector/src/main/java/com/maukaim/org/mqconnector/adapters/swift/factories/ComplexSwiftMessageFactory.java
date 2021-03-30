package com.maukaim.org.mqconnector.adapters.swift.factories;

import com.maukaim.org.mqconnector.MQException;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.field.Field28C;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * To be used only by SwiftMQListenersManagerImpl.
 * Support continuation Swift messages.
 */
public class ComplexSwiftMessageFactory extends SwiftMessageFactory {

    public MtSwiftMessage buildGenericSwiftMessage(Message message) throws MQException {
        try{
            String rawMessage = this.translateMessage(message);
            return MtSwiftMessage.parse(rawMessage);
        }catch (MQException mqEx){
            throw  mqEx;
        } catch (JMSException jmsEx){
            throw new MQException(jmsEx);
        }catch (Exception e){
            throw new MQException(e.getMessage());
        }
    }

    public boolean isFinalMessage(Field28C field28C){
        int FINAL_SEQUENCE_NUMBER = 1;
        return field28C.getSequenceNumberAsNumber().intValue() == FINAL_SEQUENCE_NUMBER;
    }
}
