package com.maukaim.org.mqconnector;

import com.maukaim.org.mqconnector.adapters.GenericMessageFactory;
import lombok.NonNull;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * Passed by default to the MQClientConnectorImpl but,
 * can be replaced by any class implementing MQListenersManager.
 *
 * @param <T> is the type to which we have to translate the MQ message.
 */
public class DefaultMQListenersManagerImpl<T> implements MQListenersManager<T> {
    private List<MQMessageListener<T>> messageListeners = new ArrayList<>();
    private List<MQExceptionListener> exceptionListeners = new ArrayList<>();
    private GenericMessageFactory<T> messageFactory;

    public DefaultMQListenersManagerImpl(@NonNull GenericMessageFactory<T> messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Override
    public boolean addMessageListener(MQMessageListener<T> m1) {
        return this.messageListeners.add(m1);
    }

    @Override
    public boolean addExceptionListener(MQExceptionListener e1) {
        return this.exceptionListeners.add(e1);
    }

    @Override
    public void onMessage(Message message) {
        try {
            T translatedMessage = messageFactory.build(message);
            messageListeners.forEach(ml -> ml.onMessage(translatedMessage));
        } catch (MQException e) {
            MQException wrappedException = new MQException(
                    String.format("Error [%s] on the message :\n%s",
                            e.getMessage(), message.toString()),
                    e.getErrorCode()
            );
            this.onException(wrappedException);
        }
    }

    @Override
    public void onException(JMSException exception) {
        exceptionListeners.forEach(el -> el.onException(new MQException(new MQException(exception))));
    }
}
