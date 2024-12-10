package com.amica.help;

import java.util.*;
import java.util.stream.Collectors;

public class Technician{
    private String id;
    private String name;
    private int extension;
    private int ticketsAssigned;
    private HelpDesk helpDesk;

    public Technician(String id, String name, int extension, HelpDesk helpDesk){
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.ticketsAssigned = 0;
        this.helpDesk = helpDesk;
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

    public List<Ticket> getActiveTickets() {
        SortedSet<Ticket> activeTickets = new TreeSet<>(Collections.reverseOrder());
        for(Ticket t : helpDesk.tickets.get(this)){
            if(t.getStatus() != Ticket.Status.RESOLVED){
                activeTickets.add(t);
            }
        }
        return new ArrayList<>(activeTickets);
    }
}
