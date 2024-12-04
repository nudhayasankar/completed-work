package com.amica.mars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ReportingRover extends Rover {
    private Telemetry subscriber;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String separator = "-";

    public Telemetry getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Telemetry subscriber) {
        this.subscriber = subscriber;
    }

    public ReportingRover(int x, int y, Rover.Direction direction, int id) {
        super(x, y, direction, id);
    }

    public ReportingRover(int id) {
        super(id);
    }

    protected void report(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getId());
        sb.append(separator);
        LocalDateTime now = LocalDateTime.now();
        sb.append(formatter.format(now));
        sb.append(separator);
        sb.append(this.getStatus());
        sb.append(separator);
        sb.append(message);
        subscriber.sendMessage(sb.toString());
    }
}
