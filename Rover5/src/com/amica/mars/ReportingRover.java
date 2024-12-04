package com.amica.mars;

public abstract class ReportingRover extends Rover {
    private Telemetry subscriber;

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
        Report report = new Report(this, message);
        subscriber.sendMessage(report);
    }
}
