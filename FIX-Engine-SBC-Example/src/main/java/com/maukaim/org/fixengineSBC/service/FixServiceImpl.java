package com.maukaim.org.fixengineSBC.service;

import com.maukaim.org.fixengine.FixEngineConnector;
import com.maukaim.org.fixengine.FixEngineConnectorImpl;
import com.maukaim.org.fixengine.messages.FixOrderRequest;
import com.maukaim.org.fixengineSBC.config.FixEngineProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDate;

@Slf4j
@Service
public class FixServiceImpl implements FixService {

    private final FixEngineProperties fixEngineProperties;

    @Value("${fix-engine.auto-reconnect:true}")
    private Boolean isAutoReconnectActivated;

    private final FixEngineConnector fixEngineConnector;

    FixServiceImpl(@Autowired FixEngineProperties properties) {
        this.fixEngineProperties = properties;
        log.info("Creating FIX Engine with config: {}", properties.getConf());
        this.fixEngineConnector = new FixEngineConnectorImpl(this, this.fixEngineProperties.getConf());
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Connecting to FIX message producer...");
            this.fixEngineConnector.connect();
            log.info("Connected to FIX message producer!");
        } catch (Exception e) { // OK to be as large because we only have one call in this try
            log.error("Error while connecting to producer with Fix Engine -> {}", e.getMessage(),e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Disconnecting from FIX message producer...");
        this.fixEngineConnector.disconnect();
        log.info("Disconnected from FIX message producer !");
    }

    // Re try for connection is already a functionality of the module
    @Scheduled(cron = "0 0/10 * 1/1 * *")
    public void autoReconnect() {
        log.info("[AUTO-RECONNECT] Check if reconnection needed...");
        if (this.isAutoReconnectActivated) {
            if (this.fixEngineConnector.isConnected()) {
                log.info("[AUTO-RECONNECT] Already connected.");
            } else {
                try {
                    log.info("[AUTO-RECONNECT] Connecting to FIX message producer...");
                    this.fixEngineConnector.connect();
                    log.info("[AUTO-RECONNECT] Connected to FIX message producer!");
                } catch (Exception e) { // OK to be as large because we only have one call in this try
                    log.error("[AUTO-RECONNECT] Error while connecting to producer with Fix Engine -> {}", e.getMessage(),e);
                }
            }
        } else {
            log.info("[AUTO-RECONNECT] functionality off.");
        }
    }


    @Override
    public void onExecutionReport(ExecutionReport executionReport) throws FieldNotFound {
        log.info("ExecutionReport received :{}", executionReport.toXML());
    }

    @Override
    public void postOrderExample() throws Exception {
        this.fixEngineConnector.postOrder(
                FixOrderRequest.builder()
                        .orderIdentifier("UUID_GENERATED")
                        .way("[BUY]/[SELL]")
                        .nominalValue(100)
                        .cusip("UNDERLYING_CUSIP")
                        .settlementDate(LocalDate.now())
                        .customerAc("ACCOUNT")
                        .traderWebUser("USER_SPECIFIC_IDENTIFIER")
                        .partyIDSource(this.fixEngineProperties.getPartyIDSource())
                        .partyRole(this.fixEngineProperties.getPartyRole())
                        .senderCompID(this.fixEngineProperties.getSenderCompId())
                        .senderLocationID(this.fixEngineProperties.getSenderLocationId())
                        .timeInForce(this.fixEngineProperties.getTimeInForce())
                        .symbol(this.fixEngineProperties.getSymbol())
                        .securityIDSource(this.fixEngineProperties.getSecurityIdSource())
                        .targetComID(this.fixEngineProperties.getTargetCompId())
                        .build()
        );

    }
}
