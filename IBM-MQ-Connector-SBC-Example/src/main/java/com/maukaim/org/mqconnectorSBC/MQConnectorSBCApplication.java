package com.maukaim.org.mqconnectorSBC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MQConnectorSBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQConnectorSBCApplication.class, args);
    }

}
