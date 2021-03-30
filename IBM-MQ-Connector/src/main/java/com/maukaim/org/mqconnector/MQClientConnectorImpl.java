package com.maukaim.org.mqconnector;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.maukaim.org.mqconnector.adapters.GenericMessageFactory;
import com.maukaim.org.mqconnector.model.MQConnectionItems;
import com.maukaim.org.mqconnector.model.MQConnectionParameters;
import lombok.NonNull;

import javax.annotation.PreDestroy;
import javax.jms.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MQClientConnectorImpl<T> implements MQClientConnector<T>, MQExceptionListener {
    private MQConnectionParameters mqConnectionParameters;
    private MQConnectionItems connectionItems;
    private MQListenersManager<T> mqListenersManager;
    private boolean autoDisconnectOnException;
    private final Object connectionManagementLock = new Object();
    private volatile boolean connected;

    //Main entry point, the simplest one to use.
    public MQClientConnectorImpl(@NonNull MQConnectionParameters parameters, @NonNull GenericMessageFactory<T> factory) {
        this(parameters, factory, false);
    }

    //If you want to specify the disconnecting behavior onException, default is false.
    public MQClientConnectorImpl(@NonNull MQConnectionParameters parameters, @NonNull GenericMessageFactory<T> factory, @NonNull boolean autoDisconnectOnException) {
        this(parameters, new DefaultMQListenersManagerImpl<>(factory), autoDisconnectOnException);
    }

    // If you have built a custom MQListenerManager implementation
    public MQClientConnectorImpl(@NonNull MQConnectionParameters parameters, @NonNull MQListenersManager<T> listenersManager) {
        this(parameters, listenersManager, false);
    }

    // The final constructor, all are converging to this one.
    public MQClientConnectorImpl(@NonNull MQConnectionParameters parameters, @NonNull MQListenersManager<T> listenersManager, @NonNull boolean autoDisconnectOnException) {
        this.mqConnectionParameters = parameters;
        this.mqListenersManager = listenersManager;
        this.mqListenersManager.addExceptionListener(this);
        this.autoDisconnectOnException = autoDisconnectOnException;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public synchronized void connect() throws MQException {
        synchronized (connectionManagementLock) {
            if (!this.isConnected()) {
                try {
                    // Connecting to MQ server: Create connection factory...
                    JmsConnectionFactory connectionCreator = this.buildConnectionFactory();

                    // Pinging MQ server: Initiate the connection with the specified MQ
                    Connection connection = connectionCreator.createConnection();

                    // Creating the client, initiate the session
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                    // Creating the client, create the queue object we will listen for message
                    Queue queue = session.createQueue(this.mqConnectionParameters.getQueue());

                    // Creating the client, create the consumer object
                    MessageConsumer consumer = session.createConsumer(queue);

                    // Starting connection
                    connection.start();

                    // Add listeners
                    connection.setExceptionListener(this.mqListenersManager);
                    consumer.setMessageListener(this.mqListenersManager);

                    // Store connection items for life management
                    this.connectionItems = MQConnectionItems.builder()
                            .connection(connection)
                            .queue(queue)
                            .session(session)
                            .messageConsumer(consumer)
                            .build();

                    this.connected = true;
                } catch (JMSException ex) {
                    throw new MQException(ex);
                }
            }
        }

    }

    private JmsConnectionFactory buildConnectionFactory() throws JMSException {
        JmsConnectionFactory factory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER).createConnectionFactory();
        factory.setStringProperty(WMQConstants.WMQ_HOST_NAME, this.mqConnectionParameters.getHost());
        factory.setStringProperty(WMQConstants.WMQ_PORT, this.mqConnectionParameters.getPort());
        factory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        factory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, this.mqConnectionParameters.getQueueManager());
        factory.setStringProperty(WMQConstants.WMQ_CHANNEL, this.mqConnectionParameters.getChannel());
        factory.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT);
        return factory;
    }

    @Override
    @PreDestroy
    public synchronized void disconnect() throws MQException {
        synchronized (connectionManagementLock) {
            if (this.isConnected()) {
                try {
                    this.connectionItems.stopAll();
                    this.connected = false;
                } catch (JMSException e) {
                    throw new MQException(e);
                }
            }
        }
    }

    @Override
    public boolean addMessageListener(MQMessageListener<T> m1) {

        return this.mqListenersManager.addMessageListener(m1);
    }

    @Override
    public boolean addExceptionListener(MQExceptionListener e1) {
        return this.mqListenersManager.addExceptionListener(e1);
    }

    @Override
    public void onException(MQException obj1) {
        if (this.autoDisconnectOnException) {
            try {
                this.disconnect();
            } catch (MQException e) {
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> this.onException(obj1), 5, TimeUnit.SECONDS);
            }
        }

    }
}
