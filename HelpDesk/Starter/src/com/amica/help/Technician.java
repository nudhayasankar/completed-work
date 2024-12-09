package com.amica.help;

public class Technician{
    private String id;
    private String name;
    private int extension;
    private int ticketsAssigned;

    public Technician(String id, String name, int extension){
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.ticketsAssigned = 0;
    }

    public String getId() {
        return id;
    }

    public void incrementTickets(){
        this.ticketsAssigned++;
    }

    public void decrementTickets(){
        this.ticketsAssigned--;
    }

    public int getTicketsAssigned() {
        return ticketsAssigned;
    }

    public String getName() {
        return name;
    }
}
