package com.maukaim.org.mqconnector.adapters.swift;

import com.maukaim.org.mqconnector.MQException;
import com.maukaim.org.mqconnector.MQExceptionListener;
import com.maukaim.org.mqconnector.MQListenersManager;
import com.maukaim.org.mqconnector.MQMessageListener;
import com.maukaim.org.mqconnector.adapters.swift.factories.ComplexSwiftMessageFactory;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT900;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT910;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT940;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt9xx.MT900;
import com.prowidesoftware.swift.model.mt.mt9xx.MT910;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import javax.jms.JMSException;
import javax.jms.Message;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This ListenersManager use ComplexSwiftMessageFactory,
 * which means it can handle SWIFT messages that are splitted
 * into multiple MQ messages. It caches not final messages until the final one arrives.
 */
public class SwiftMQListenersManagerImpl implements MQListenersManager<MQSwiftMessage> {
    private List<MQMessageListener<MQSwiftMessage>> messageListeners = new ArrayList<>();
    private List<MQExceptionListener> exceptionListeners = new ArrayList<>();
    private ComplexSwiftMessageFactory messageFactory;

    private ConcurrentHashMap<String, Set<MT940>> mappedToBeContinuedMT940 = new ConcurrentHashMap<>();

    public SwiftMQListenersManagerImpl(){
        this.messageFactory = new ComplexSwiftMessageFactory();
    }

    @Override
    public boolean addMessageListener(MQMessageListener<MQSwiftMessage> m1) {
        return this.messageListeners.add(m1);
    }

    @Override
    public boolean addExceptionListener(MQExceptionListener e1) {
        return this.exceptionListeners.add(e1);
    }

    @Override
    public void onException(JMSException exception) {
        this.exceptionListeners.forEach(el->el.onException(new MQException(exception)));
    }

    @Override
    public void onMessage(Message message) {
        try{
            LocalDateTime receptionTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSTimestamp()), TimeZone.getDefault().toZoneId());
            MtSwiftMessage genericSwiftMessage = messageFactory.buildGenericSwiftMessage(message);

            switch (genericSwiftMessage.getMessageTypeInt()){
                case 940:
                    this.onMT940(genericSwiftMessage, receptionTime);
                    break;
                case 910:
                    this.onMT910(genericSwiftMessage, receptionTime);
                    break;
                case 900:
                    this.onMT900(genericSwiftMessage, receptionTime);
                    break;
                default:
                    throw new MQException("SwiftMessage MT type not expected ! Raw message was:\n" + genericSwiftMessage.getMessage());
            }

        }catch (JMSException e){
            String errMessage = String.format("Error [%s] when trying to translate following message with factory:\n%s",
                    e.getMessage(), message.toString());
            this.onException(new MQException(errMessage, e.getErrorCode()));
        }catch (Exception e){
            String errMessage = String.format("Unexpected error [%s] during process on message:\n%s",
                    e.getMessage(), message.toString());
            this.onException(new MQException(errMessage));
        }
    }

    void onMT910(MtSwiftMessage genericSwiftMessage, LocalDateTime receptionTime){
        MQSwiftMessageMT910 mqSwiftMessageMT910 = this.messageFactory.buildMT910(genericSwiftMessage.getMessage(), MT910.parse(genericSwiftMessage), receptionTime);
        this.spreadTheNews(mqSwiftMessageMT910);
    }
    void onMT900(MtSwiftMessage genericSwiftMessage, LocalDateTime receptionTime){
        MQSwiftMessageMT900 mqSwiftMessageMT900 = this.messageFactory.buildMT900(genericSwiftMessage.getMessage(), MT900.parse(genericSwiftMessage), receptionTime);
        this.spreadTheNews(mqSwiftMessageMT900);
    }
    void onMT940(MtSwiftMessage genericSwiftMessage, LocalDateTime receptionTime){
        MT940 mt940 = MT940.parse(genericSwiftMessage);
        String key = mt940.getField28C().getStatementNumber();

        if(this.messageFactory.isFinalMessage(mt940.getField28C())){
            Set<MT940> mt940SetMatching = this.mappedToBeContinuedMT940.get(key);
            if(mt940SetMatching != null && !mt940SetMatching.isEmpty()){
                mt940SetMatching.stream()
                        .map(AbstractMT::getFields)
                        .flatMap(Collection::stream)
                        .forEach(mt940::addField);
            }
            MQSwiftMessageMT940 mqSwiftMessageMT940 = this.messageFactory.buildMT940(mt940.message(), mt940, receptionTime);
            this.mappedToBeContinuedMT940.remove(key);
            this.spreadTheNews(mqSwiftMessageMT940);

        }else{
            this.mappedToBeContinuedMT940.putIfAbsent(key, new HashSet<MT940>());
            this.mappedToBeContinuedMT940.get(key).add(mt940);
        }
    }

    private void spreadTheNews(MQSwiftMessage mqSwiftMessage){
        messageListeners.forEach(ml -> ml.onMessage(mqSwiftMessage));
    }
}
