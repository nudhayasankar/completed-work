package com.amica.network;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@Log
public class MonitorTest {

    @Test
    public void testStatus() {
        Monitor monitor = new Monitor();
        monitor.setFileName("TestStatus.txt");
        monitor.scheduledMethod();
        setLatestStatus(Monitor.Status.STARTING);
        monitor.scheduledMethod();
    }

    @BeforeAll
    public static void setUpEnv() {
        System.setProperty("server.env", "Connected");
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
