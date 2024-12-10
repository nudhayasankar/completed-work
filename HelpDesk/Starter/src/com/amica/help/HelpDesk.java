package com.amica.help;

import java.util.*;

import com.amica.help.Ticket.Priority;
import com.amica.help.Ticket.Status;

public class HelpDesk implements HelpDeskAPI{
    public Map<Technician, SortedSet<Ticket>> tickets = new HashMap<>();
    private static int ticketId = 0;

    private final TreeSet<Technician> assignmentQueue = new TreeSet<>(
            Comparator.comparingInt(Technician::getTicketsAssigned).thenComparing(Technician::getName));

    public HelpDesk(){
    }

    @Override
    public void createTechnician(String ID, String name, int extension) {
        Technician technician = new Technician(ID, name, extension, this);
        tickets.put(technician, new TreeSet<>());
        assignmentQueue.add(technician);
    }

    @Override
    public void addNoteToTicket(int ticketID, String note) {
        Ticket toAddNote = getTicketByID(ticketID);
        toAddNote.addEvent(note);
    }

    @Override
    public void resolveTicket(int ticketID, String note) {
        Ticket toResolve = getTicketByID(ticketID);
        if(toResolve != null){
            toResolve.addEvent(note, Status.RESOLVED);
            Technician assignedTech = toResolve.getAssignedTo();
            assignmentQueue.remove(assignedTech);
            assignedTech.decrementTickets();
            assignmentQueue.add(assignedTech);
        }
    }

    @Override
    public void addTags(int ticketID, String... tags) {

    }

    @Override
    public Ticket getTicketByID(int ID) {
        Ticket toFind = null;
        for (Technician tech : tickets.keySet()) {
            for (Ticket t : tickets.get(tech)) {
                if (t.getID() == ID) {
                    toFind = t;
                    break;
                }
            }
            if (toFind != null) break;
        }
        return toFind;
    }

    @Override
    public SortedSet<Ticket> getTickets() {
        SortedSet<Ticket> allTickets = new TreeSet<>(Collections.reverseOrder());
        for(SortedSet<Ticket> technicianTickets : tickets.values()){
            for(Ticket t : technicianTickets){
                allTickets.add(t);
            }
        }
        return allTickets;
    }

    @Override
    public List<Ticket> getTicketsByTechnician(String techID) {
        Technician tech = getTechnicians().stream().filter(t -> t.getId().equals(techID)).reduce(($,t)-> t).get();
        SortedSet<Ticket> filteredTickets = tickets.get(tech);
        return new ArrayList<>(filteredTickets);
    }

    @Override
    public List<Ticket> getTicketsWithAnyTag(String... tags) {
        return null;
    }

    @Override
    public int getAverageMinutesToResolve() {
        return 0;
    }

    @Override
    public Map<String, Integer> getAverageMinutesToResolvePerTechnician() {
        return null;
    }

    @Override
    public List<Ticket> getTicketsByText(String text) {
        return null;
    }

    @Override
    public List<Ticket> getTicketsByNotStatus(Status status) {
        return null;
    }

    @Override
    public List<Ticket> getTicketsByStatus(Status status) {
        Set<Ticket> filteredTickets = new TreeSet<>(Collections.reverseOrder());
        for(SortedSet<Ticket> technicianTickets : tickets.values()){
            for(Ticket t : technicianTickets){
                if(t.getStatus() == status){
                    filteredTickets.add(t);
                }
            }
        }
        return new ArrayList<>(filteredTickets);
    }

    @Override
    public int reopenTicket(int priorTicketID, String reason, Priority priority) {
        return 0;
    }

    @Override
    public int createTicket(String originator, String description, Priority priority) {
        ticketId++;
        Technician assignTo = assignmentQueue.first();
        assignmentQueue.remove(assignTo);
        Ticket ticket = new Ticket(ticketId, originator, description, priority, assignTo);
        tickets.get(assignTo).add(ticket);
        assignTo.incrementTickets();
        assignmentQueue.add(assignTo);
        return ticketId;
    }

    public List<Technician> getTechnicians() {
        SortedSet<Technician> allTechs = new TreeSet<>(Comparator.comparing(Technician::getName));
        allTechs.addAll(tickets.keySet());
        return new ArrayList<>(allTechs);
    }
}
