package com.amica.help;

public class Event {
    private long timestamp;
    private String note;

    public Event(String note){
        this.note = note;
        this.timestamp = Clock.getTime();
    }
}
