package com.maukaim.org.mqconnector.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MQConnectionParameters {
    private String channel;
    private String host;
    private String port;
    private String queue;
    private String queueManager;
}
