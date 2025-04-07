package com.amica.network;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@Log
public class MonitorMQTest {

    private JmsTemplate mockTemplate;
    private Monitor monitor;

    @BeforeEach
    public void setUp() throws IOException {
        mockTemplate = Mockito.mock(JmsTemplate.class);
        monitor = new Monitor();
        monitor.setFileName("TestStatus.txt");
        monitor.setTopicName("TestTopic");
        monitor.setJmsTemplate(mockTemplate);
    }
    @Test
    @SneakyThrows
    public void testStatusChanged() {
        setLatestStatus(Monitor.Status.STARTING);
        monitor.scheduledMethod();
        Mockito.verify(mockTemplate, Mockito.times(1)).convertAndSend("TestTopic", Monitor.Status.STARTING);
    }

    @Test
    @SneakyThrows
    public void testStatusUnchanged() {
        setLatestStatus(Monitor.Status.STARTING);
        monitor.scheduledMethod();
        Mockito.verify(mockTemplate, Mockito.times(0)).convertAndSend("TestTopic", Monitor.Status.RUNNING);
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
