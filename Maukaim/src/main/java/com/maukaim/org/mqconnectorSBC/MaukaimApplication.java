package com.maukaim.org.mqconnectorSBC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.maukaim.org")
@EnableConfigurationProperties
public class MaukaimApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaukaimApplication.class, args);
    }

}