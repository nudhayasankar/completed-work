package com.amica.mars;
import com.amica.mars.Rover.Direction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {
    private ReportingRover sender;
    private String timestamp;
    private int x;
    private int y;
    private Direction direction;
    private String message;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final String separator = "; ";

    public Report(ReportingRover rover, String message){
        this.sender = rover;
        this.message = message;
        this.x = rover.getX();
        this.y = rover.getY();
        this.direction = rover.getDirection();
        LocalDateTime now = LocalDateTime.now();
        this.timestamp = formatter.format(now);
    }

    public ReportingRover getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.sender.getId());
        sb.append(separator);
        sb.append(this.timestamp);
        sb.append(separator);
        sb.append(String.format("Coordinates (%d, %d)", x, y));
        sb.append(separator);
        sb.append(direction);
        sb.append(separator);
        sb.append(message);
        return sb.toString();
    }
}
