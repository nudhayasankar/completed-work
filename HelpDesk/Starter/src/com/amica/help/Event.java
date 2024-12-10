package com.amica.help;

import com.amica.help.Clock;
import com.amica.help.Ticket.Status;

public class Event {
    private long timestamp;
    private String note;
    private Status newStatus;

    public Event(String note){
        this.note = note;
        this.timestamp = Clock.getTime();
    }

    public Event(String note, Status status){
        this(note);
        this.newStatus = status;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", Clock.format(timestamp), note);
    }

    public String getNote() {
        return note;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Status getNewStatus() {
        return newStatus;
    }
}
