package com.amica.help;

import com.amica.help.Clock;

public class Event {
    private long timestamp;
    private String note;

    public Event(String note){
        this.note = note;
        this.timestamp = Clock.getTime();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", Clock.format(timestamp), note);
    }

    public String getNote() {
        return note;
    }
}
