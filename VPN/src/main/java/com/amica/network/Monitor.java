package com.amica.network;

import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log
public class Monitor {

    enum Status {
        RUNNING,
        STOPPED,
        STARTING
    }

    @Autowired
    @Setter
    private JmsTemplate jmsTemplate;

    @Value("${VPN.TOPIC}")
    @Setter
    private String topicName;

    private Status latestStatus = null;

    @Value("${Monitor.filename}")
    @Setter
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void scheduledMethod() {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            if(lines.size() != 1) {
                throw new Exception("The file is empty or has more than one line");
            }
            Status status = Status.valueOf(lines.get(0));
            if(status != latestStatus) {
                logMessage("Updating status");
                logMessage(status.name());
                logMessage("Sending message");
                jmsTemplate.convertAndSend(topicName,status);
                latestStatus = status;
            }
        } catch(FileNotFoundException fnf) {
            log.warning(fnf.getMessage());
        } catch (IOException ioe) {
            log.warning(ioe.getMessage());
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    private void logMessage(String message) {
        log.info(String.format("********** %s **********", message));
    }
}
