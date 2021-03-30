package com.maukaim.org.mqconnector.adapters.swift.factories;

import com.maukaim.org.mqconnector.MQException;
import com.maukaim.org.mqconnector.adapters.UtilsObjectMessageFactory;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessage;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT900;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT910;
import com.maukaim.org.mqconnector.adapters.swift.model.MQSwiftMessageMT940;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.field.Field13D;
import com.prowidesoftware.swift.model.field.Field86;
import com.prowidesoftware.swift.model.mt.mt9xx.MT900;
import com.prowidesoftware.swift.model.mt.mt9xx.MT910;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public abstract class SwiftMessageFactory extends UtilsObjectMessageFactory {

    public MQSwiftMessage build(String rawMessage, LocalDateTime receptionTime) throws MQException {
        MtSwiftMessage notYetTyped = MtSwiftMessage.parse(rawMessage);
        return this.build(notYetTyped, receptionTime);
    }

    public MQSwiftMessage build(MtSwiftMessage messageNotTyped, LocalDateTime receptionTime) throws MQException {
        switch (messageNotTyped.getMessageTypeInt()) {
            case 940:
                return this.buildMT940(messageNotTyped.getMessage(), MT940.parse(messageNotTyped), receptionTime);
            case 910:
                return this.buildMT910(messageNotTyped.getMessage(), MT910.parse(messageNotTyped), receptionTime);
            case 900:
                return this.buildMT900(messageNotTyped.getMessage(), MT900.parse(messageNotTyped), receptionTime);
            default:
                throw new MQException("SwiftMessage MT type not expected ! Message:\n" + messageNotTyped.getMessage());
        }
    }

    public MQSwiftMessageMT900 buildMT900(String message, MT900 parse, LocalDateTime receptionTime) {
        return MQSwiftMessageMT900.builder()
                .rawMessage(message)
                .receptionTime(receptionTime)
                .swiftMessageId(parse.getField20().getReference())
                .accountId(parse.getField25().getAccount())
                .currency(parse.getField32A().getCurrency())
                .movementDateTime(toLocalDateTime(parse.getField13D()))
                .movementAmount(parse.getField32A().amount().doubleValue())
                .build();

    }

    public MQSwiftMessageMT910 buildMT910(String message, MT910 parse, LocalDateTime receptionTime) {
        return MQSwiftMessageMT910.builder()
                .rawMessage(message)
                .receptionTime(receptionTime)
                .swiftMessageId(parse.getField20().getReference())
                .accountId(parse.getField25().getAccount())
                .currency(parse.getField32A().getCurrency())
                .movementDateTime(toLocalDateTime(parse.getField13D()))
                .movementAmount(parse.getField32A().amount().doubleValue())
                .build();
    }

    public MQSwiftMessageMT940 buildMT940(String rawMessage, MT940 mt940, LocalDateTime receptionTime) {
        MQSwiftMessageMT940.MQSwiftMessageMT940Builder<?, ?> builder = MQSwiftMessageMT940.builder();
        builder.rawMessage(rawMessage)
                .receptionTime(receptionTime)
                .swiftMessageId(mt940.getField20().getReference())
                .accountId(mt940.getField25().getAccount());

        // Opening
        try{
           Double opBalance = this.signBalance(
                   mt940.getField60F().getDCMark(),
                   mt940.getField60F().amount().doubleValue());
           builder.openingBalance(opBalance);
        }catch (Exception e){
            Double opBalance = this.signBalance(
                    mt940.getField60M().getDCMark(),
                    mt940.getField60M().amount().doubleValue());
            builder.openingBalance(opBalance);
        }

        // Closing
        try{
            Double cloBalance = this.signBalance(
                    mt940.getField62F().getDCMark(),
                    mt940.getField62F().amount().doubleValue());
            String currency = mt940.getField62F().getCurrency();
            LocalDate valueDate = toLocalDate(mt940.getField62F().getDateAsCalendar());

            builder.openingBalance(cloBalance)
                    .currency(currency)
                    .valueDate(valueDate);

        }catch (Exception e){
            Double cloBalance = this.signBalance(
                    mt940.getField62M().getDCMark(),
                    mt940.getField62M().amount().doubleValue());
            String currency = mt940.getField62M().getCurrency();
            LocalDate valueDate = toLocalDate(mt940.getField62M().getDateAsCalendar());
            builder.openingBalance(cloBalance)
                    .currency(currency)
                    .valueDate(valueDate);
        }
        List<Field86> field86 = mt940.getField86();
        String additionalInformation = Objects.isNull(field86) ? "No details provided." : field86.stream()
                .reduce("", (acc, field) -> String.format("%s ; %s", acc, field.getValue()), String::concat);
        builder.additionalInformations(additionalInformation);
        MQSwiftMessageMT940 mt940Translate = builder.build();

        try{
            mt940Translate.setClosingAvailableBalance(this.signBalance(
                    mt940.getField64().getDCMark(),
                    mt940.getField64().amount().doubleValue()
            ));
        } catch (Exception e){
           mt940Translate.setClosingAvailableBalance(mt940Translate.getClosingBalance());
        }

        return mt940Translate;
    }

    private Double signBalance(String dcMarker, Double value) {
        String DEBIT_MARKER = "D";
        return (dcMarker.equals(DEBIT_MARKER)) ? -1 * value : value;
    }

    private LocalDateTime toLocalDateTime(Field13D field13D) {
        LocalDate ld = LocalDate.ofInstant(field13D.getDateAsCalendar().toInstant(), ZoneId.systemDefault());
        LocalTime lt = LocalTime.ofInstant(field13D.getTimeAsCalendar().toInstant(), ZoneId.systemDefault());
        return LocalDateTime.of(ld, lt);
    }
}
