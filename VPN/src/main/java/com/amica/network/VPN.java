package com.amica.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import({JMSConfiguration.class})
public class VPN {
    public static void main(String[] args) {
        System.setProperty("server.env", "developerworkstation");
        SpringApplication.run(VPN.class, args);
    }
}
