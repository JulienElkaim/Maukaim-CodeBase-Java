package com.maukaim.org.fixengine.messages;

import quickfix.StringField;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;

public class NewOrderSingleBuilder {

    private NewOrderSingle newOrderSingle;
    private NewOrderSingle.NoPartyIDs noPartyIDs;

    public NewOrderSingleBuilder() {
        this.newOrderSingle = new NewOrderSingle();
        this.noPartyIDs = new NewOrderSingle.NoPartyIDs();
    }

    public NewOrderSingle build() {
        this.newOrderSingle.addGroup(this.noPartyIDs);
        return this.newOrderSingle;
    }

    public NewOrderSingleBuilder partyID(String partyID) {
        this.noPartyIDs.set(new PartyID(partyID));
        return this;
    }

    public NewOrderSingleBuilder partyRole(int partyRole) {
        this.noPartyIDs.set(new PartyRole(partyRole));
        return this;
    }

    public NewOrderSingleBuilder partyIDSource(char partyIDSource) {
        this.noPartyIDs.set(new PartyIDSource(partyIDSource));
        return this;
    }

    public NewOrderSingleBuilder stringField(int field, String data) {
        this.newOrderSingle.getHeader().setField(new StringField(field, data));
        return this;
    }

    public NewOrderSingleBuilder msgType(String data) {
        this.newOrderSingle.getHeader().setField(new MsgType(data));
        return this;
    }

    public NewOrderSingleBuilder senderCompID(String data) {
        this.newOrderSingle.getHeader().setField(new SenderCompID(data));
        return this;
    }

    public NewOrderSingleBuilder targetCompID(String data) {
        this.newOrderSingle.getHeader().setField(new TargetCompID(data));
        return this;
    }

    public NewOrderSingleBuilder senderLocationID(String data) {
        this.newOrderSingle.getHeader().setField(new SenderLocationID(data));
        return this;
    }

    public NewOrderSingleBuilder clOrdID(String data) {
        this.newOrderSingle.set(new ClOrdID(data));
        return this;
    }

    public NewOrderSingleBuilder transactTime(TransactTime transactTime) {
        this.newOrderSingle.set(transactTime);
        return this;
    }

    public NewOrderSingleBuilder timeInForce(char data) {
        this.newOrderSingle.set(new TimeInForce(data));
        return this;
    }

    public NewOrderSingleBuilder symbol(String data) {
        this.newOrderSingle.set(new Symbol(data));
        return this;
    }

    public NewOrderSingleBuilder side(char data) {
        this.newOrderSingle.set(new Side(data));
        return this;
    }

    public NewOrderSingleBuilder orderQty(double data) {
        this.newOrderSingle.set(new OrderQty(data));
        return this;
    }

    public NewOrderSingleBuilder securityID(String data) {
        this.newOrderSingle.set(new SecurityID(data));
        return this;
    }

    public NewOrderSingleBuilder settlDate(String data) {
        this.newOrderSingle.set(new SettlDate(data));
        return this;
    }

    public NewOrderSingleBuilder securityIDSource(String data) {
        this.newOrderSingle.set(new SecurityIDSource(data));
        return this;
    }

    public NewOrderSingleBuilder ordType(char data) {
        this.newOrderSingle.set(new OrdType(data));
        return this;
    }

    public NewOrderSingleBuilder account(String data) {
        this.newOrderSingle.set(new Account(data));
        return this;
    }
}
