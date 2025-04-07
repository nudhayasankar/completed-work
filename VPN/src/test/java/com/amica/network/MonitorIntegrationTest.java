package com.amica.network;

import jakarta.jms.Message;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Log
@ActiveProfiles("test")
@SpringBootTest(classes = {MonitorIntegrationTest.Config.class})
public class MonitorIntegrationTest {

    @Autowired
    private Subscriber subscriber;

    public void setLatestStatus(Monitor.Status status) {
        try(FileWriter fw = new FileWriter("TestStatus.txt")) {
            fw.write(status.name());
        } catch(FileNotFoundException fnf) {
            log.warning(fnf.getMessage());
        } catch (IOException ioe) {
            log.warning(ioe.getMessage());
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    @ComponentScan(basePackageClasses=JMSConfiguration.class)
    public static class Config {
        @Bean
        public Subscriber subscriber() {
            return new Subscriber();
        }
    }

    @Test
    @SneakyThrows
    public void testIntegration() {
         Monitor monitor = new Monitor();
         monitor.scheduledMethod();
         Thread.sleep(5000);
         setLatestStatus(Monitor.Status.RUNNING);
         Thread.sleep(5000);
         setLatestStatus(Monitor.Status.STARTING);
         Thread.sleep(5000);
         setLatestStatus(Monitor.Status.STOPPED);
         Thread.sleep(5000);
         List<Message> messages = subscriber.getReceived();
         Assertions.assertEquals(messages.size(), 4);
    }

    @BeforeAll
    public static void setUp() {
        System.setProperty("server.env", "Connected");

    }
}
