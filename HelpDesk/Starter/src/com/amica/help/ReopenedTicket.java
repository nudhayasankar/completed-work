package com.amica.help;

import java.util.ArrayList;
import java.util.List;

public class ReopenedTicket extends Ticket{
    private Ticket priorTicket;
    private final String TICKET_CREATED = "Created ticket.";
    private final String TICKET_ASSIGNED = "Assigned to Technician %s, %s.";

    public ReopenedTicket(int id, Ticket priorTicket, String description, Priority priority, Technician assignedTo)
    {
        super(id, priorTicket.getOriginator(), description, priority, assignedTo);
        this.priorTicket = priorTicket;
    }

    public Ticket getPriorTicket() {
        return priorTicket;
    }

    @Override
    public List<Event> getHistory(){
        List<Event> history = new ArrayList<>();
        if(priorTicket != null){
            history.addAll(priorTicket.getHistory());
        }
        history.addAll(super.getHistory());
        return history;
    }

    @Override
    public boolean includesText(String keyword){
        return priorTicket.includesText(keyword) || super.includesText(keyword);
    }
}
