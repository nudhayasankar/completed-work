package com.amica.mars;

import java.util.ArrayList;
import java.util.List;

public class Recorder implements Telemetry {

    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public Recorder() {
        messages = new ArrayList<>();
    }

    public void sendMessage(String message) {
        messages.add(message);
    }

    public void printMessages() {
        messages.forEach(message -> {
            System.out.println(message);
        });
    }
}
