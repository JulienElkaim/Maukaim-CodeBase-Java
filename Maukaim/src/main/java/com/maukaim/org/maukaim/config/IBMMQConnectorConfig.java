package com.maukaim.org.maukaim.config;

import com.maukaim.org.mqconnector.MQClientConnector;
import com.maukaim.org.mqconnector.MQClientConnectorImpl;
import com.maukaim.org.mqconnector.adapters.swift.SwiftMQListenersManagerImpl;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;
import com.maukaim.org.mqconnector.model.MQConnectionParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IBMMQConnectorConfig {
    @Bean
    public MQClientConnector<MQSwiftMessage> clientConnector(
            @Value("${IBM-MQ.config.host}") String host,
            @Value("${IBM-MQ.config.port}") String port,
            @Value("${IBM-MQ.config.channel}") String channel,
            @Value("${IBM-MQ.config.queue}") String queue,
            @Value("${IBM-MQ.config.queueManager}") String queueManager){
        return new MQClientConnectorImpl<MQSwiftMessage>(MQConnectionParameters.builder()
                .host(host)
                .port(port)
                .channel(channel)
                .queue(queue)
                .queueManager(queueManager)
                .build(), new SwiftMQListenersManagerImpl());
    }
}
