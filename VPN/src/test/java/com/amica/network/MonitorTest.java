package com.amica.network;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@Log
public class MonitorTest {

    private Monitor monitor;
    private JmsTemplate mockTemplate;
    @Test
    public void testStatus() {
        monitor.setFileName("TestStatus.txt");
        monitor.scheduledMethod();
        setLatestStatus(Monitor.Status.STARTING);
        monitor.scheduledMethod();
        Mockito.verify(mockTemplate, Mockito.times(1))
                .convertAndSend(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @BeforeAll
    public static void setUpEnv() {
        System.setProperty("server.env", "Connected");
    }

    @BeforeEach
    public void setup() {
        monitor = new Monitor();
        mockTemplate = Mockito.mock(JmsTemplate.class);
        monitor.setJmsTemplate(mockTemplate);
        monitor.setTopicName("TestTopic");
    }

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
}
