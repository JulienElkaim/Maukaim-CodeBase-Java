package com.maukaim.org.mqconnectorSBC.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix= "fix-engine")
public class FixEngineProperties {
    private String senderCompId;
    private String targetCompId;
    private Character partyIDSource;
    private Integer partyRole;
    private Character timeInForce;
    private String symbol;
    private String securityIdSource;
    private String senderLocationId;
    private String conf;
}
