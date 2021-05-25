package com.maukaim.org.fixengineSBC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.maukaim.org.fixengineSBC")
@EnableConfigurationProperties
public class FixEngineSBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(FixEngineSBCApplication.class, args);
    }

}
