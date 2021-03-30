package com.maukaim.org.mqconnectorSBC.service;

import com.maukaim.org.mqconnector.MQClientConnector;
import com.maukaim.org.mqconnector.MQException;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SwiftServiceImpl implements SwiftService {

    private MQClientConnector<MQSwiftMessage> mqConnector;
    private Boolean enabled;

    public SwiftServiceImpl(@Autowired MQClientConnector<MQSwiftMessage> mqConnector,
                            @Value("${IBM-MQ.enabled:false}") Boolean enabled) {
        this.mqConnector = mqConnector;
        this.enabled = enabled;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing SwiftServiceImpl....");
        this.mqConnector.addExceptionListener(this);
        this.mqConnector.addMessageListener(this);
        this.connectToMQ();

        log.info("SwiftServiceImpl initialized !");
    }

    // If connexion enabled, try to reconnect at regular interval...
    @Scheduled(cron = "0 0/10 * 1/1 * MON-FRI")
    public void connectToMQ() {
        if(this.enabled){
            try{
                this.mqConnector.connect();

            }catch (MQException e){
                log.warn("Connection to MQ failed ! We won't listen for MQ messages until next successful connection...");
            }
        }
    }

    @Scheduled(cron = "0 0/1 * 1/1 * MON-FRI")
    public void connectionSanityCheck(){
        if(this.enabled){
            log.info("[Swift MQConnector] Connexion status: {}.",
                    this.mqConnector.isConnected() ? "ON" : "OFF");
        }
    }

    @Override
    public void onException(MQException obj1) {
        log.warn("SwiftServiceImpl received a MQException:\n",
                obj1);
    }

    @Override
    public void onMessage(MQSwiftMessage obj1) {
        log.info("SwiftServiceImpl received the following message:\n{}\n{}",
                obj1.toString(),
                obj1.getRawMessage());
    }
}
