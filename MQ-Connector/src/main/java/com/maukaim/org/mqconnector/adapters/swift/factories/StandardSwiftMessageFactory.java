package com.maukaim.org.mqconnector.adapters.swift.factories;

import com.maukaim.org.mqconnector.MQException;
import com.maukaim.org.mqconnector.adapters.GenericMessageFactory;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.field.Field28C;

import javax.jms.JMSException;
import javax.jms.Message;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Implements GenericMessageFactory, so can be used with DefaultMQListenersManager.
 * It does NOT support splitted Swift messages (Continuation Swift messages), for that please use ComplexSwiftMessageFactory.
 */
public class StandardSwiftMessageFactory extends SwiftMessageFactory implements GenericMessageFactory<MQSwiftMessage> {

    public MQSwiftMessage build(Message message) throws MQException {
        try{
            String rawMessage = this.translateMessage(message);

            MtSwiftMessage messageNotTypedYet = MtSwiftMessage.parse(rawMessage);
            LocalDateTime receptionTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSTimestamp()),
                    TimeZone.getDefault().toZoneId());
            return this.build(messageNotTypedYet, receptionTime);

        }catch (MQException mqEx){
            throw  mqEx;
        } catch (JMSException jmsEx){
            throw new MQException(jmsEx);
        }catch (Exception e){
            throw new MQException(e.getMessage());
        }
    }

}
