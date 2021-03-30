package com.maukaim.org.mqconnector.model;

import lombok.*;

import javax.jms.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MQConnectionItems {
    private Connection connection;
    private Session session;
    private Queue queue;
    private MessageConsumer messageConsumer;

    public void stopAll() throws JMSException{
        //Order important to close connection properly;
        messageConsumer.close();
        session.close();
        connection.close(); //This also close Session and consumers / Producers
    }
}
