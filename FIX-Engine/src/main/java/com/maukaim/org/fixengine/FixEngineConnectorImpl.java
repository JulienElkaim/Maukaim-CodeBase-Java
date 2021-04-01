package com.maukaim.org.fixengine;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.maukaim.org.fixengine.application.FixApplication;
import com.maukaim.org.fixengine.application.FixEngineEventListener;
import com.maukaim.org.fixengine.messages.FixOrderRequest;
import com.maukaim.org.fixengine.messages.NewOrderSingleBuilder;
import quickfix.*;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

public class FixEngineConnectorImpl implements FixEngineConnector {

    private FixApplication fixApplication;
    private Initiator fixSessionInitiator;

    private String configurationFile;

    public FixEngineConnectorImpl(FixEngineEventListener fixEngineEventListener, String conf) throws RuntimeException {
        this.configurationFile = conf;
        this.fixApplication = new FixApplication(fixEngineEventListener);
    }


    @Override
    public boolean isConnected() {
        if (this.fixSessionInitiator == null) {
            return false;
        }
        return !this.fixSessionInitiator.getSessions().isEmpty()
                && this.fixApplication.isLoggedOn();
    }

    private void waitForConnection() throws InterruptedException, ExecutionException, TimeoutException {
        Future isConnectedFuture = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat("FixConnectorThread")
                .build())
                .submit(() -> {
                    while (!this.isConnected()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("ERROR::: Interrupted while connecting to proxy" + e.getMessage());
                        }
                        System.out.println("INFO::: Session with proxy not creaed yet");
                    }
                });

        isConnectedFuture.get(10, TimeUnit.SECONDS);
        System.out.println("Connection established ! Session ID : " + FixApplication.getSessionID().toString());
    }

    @Override
    public boolean connect() throws InterruptedException, ConfigError, TimeoutException, ExecutionException, FileNotFoundException {
        if (!this.isConnected()) {
            File privateKeyFile = new File(this.configurationFile);
            InputStream inputStream = new FileInputStream(privateKeyFile.getAbsolutePath());
            SessionSettings settings = new SessionSettings(inputStream);
            FileStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            FileLogFactory logFactory = new FileLogFactory(settings);
            MessageFactory messageFactory = new DefaultMessageFactory();
            this.fixSessionInitiator = new SocketInitiator(this.fixApplication, messageStoreFactory, settings, logFactory, messageFactory);
            this.fixSessionInitiator.start();
            this.waitForConnection();
        } else {
            System.out.println("WARN::: Connection tentative while already connected.");
        }
        return this.isConnected();
    }

    @Override
    @PreDestroy
    public synchronized void disconnect() {
        if (!this.fixSessionInitiator.getSessions().isEmpty()) {
            System.out.println("WARN::: disconnection from proxy");
            this.fixSessionInitiator.stop();
        } else {
            System.out.println("WARN::: Disconnection attempt, but no connexion established.");
        }
    }

    @Override
    public String postOrder(FixOrderRequest orderRequest) throws Exception {
        if (this.isConnected()) {
            NewOrderSingle newOrder = this.createNewOrderSingle(orderRequest);
            boolean isSent = Session.sendToTarget(newOrder, FixApplication.getSessionID());
            if (isSent) {
                return newOrder.toString();
            } else {
                throw new FixEngineException(String.format(
                        "Unable to send the following message for Session(%s): %s", FixApplication.getSessionID(),
                        newOrder));
            }
        } else {
            throw new FixEngineException("Trying to post an order, without loggedOn or active session.");
        }
    }

    private NewOrderSingle createNewOrderSingle(FixOrderRequest orderRequest) {
        return new NewOrderSingleBuilder()
                .partyID(orderRequest.getTraderWebUser())
                .partyRole(orderRequest.getPartyRole())
                .partyIDSource(orderRequest.getPartyIDSource())
                .stringField(8, "FIX.4.4")
                .msgType(NewOrderSingle.MSGTYPE)
                .senderCompID(orderRequest.getSenderCompID())
                .targetCompID(orderRequest.getTargetComID())
                .senderLocationID(orderRequest.getSenderLocationID())
                .clOrdID(orderRequest.getOrderIdentifier())
                .transactTime(new TransactTime())
                .timeInForce(orderRequest.getTimeInForce())
                .symbol(orderRequest.getSymbol())
                .side(orderRequest.getWay().equals("BUY") ? Side.BUY : Side.SELL)
                .orderQty(orderRequest.getNominalValue())
                .securityID(orderRequest.getCusip())
                .settlDate(orderRequest.getSettlementDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .securityIDSource(orderRequest.getSecurityIDSource())
                .ordType(OrdType.MARKET)
                .account(orderRequest.getCustomerAc())
                .build();
    }
}
