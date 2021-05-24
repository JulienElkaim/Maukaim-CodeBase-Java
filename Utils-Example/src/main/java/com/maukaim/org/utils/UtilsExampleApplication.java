package com.maukaim.org.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.maukaim.org")
@EnableConfigurationProperties
public class UtilsExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilsExampleApplication.class, args);
    }

}
