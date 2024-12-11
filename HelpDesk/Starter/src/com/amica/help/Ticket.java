package com.amica.help;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class representing a problem ticket for a help desk.
 *
 * @author Will Provost
 */
public class Ticket implements Comparable<Ticket>{

	public enum Status { CREATED, ASSIGNED, RESOLVED }
	public enum Priority { LOW, MEDIUM, HIGH, URGENT }
	private int id;
	private String originator;
	private String description;
	private Priority priority;
	private List<Event> history;
	private final String TICKET_CREATED = "Created ticket.";
	private final String TICKET_ASSIGNED = "Assigned to Technician %s, %s.";
	private Technician assignedTo;
	private SortedSet<Tag> ticketTags;

	public Ticket(int id, String originator, String description, Priority priority, Technician assignedTo){
		this.id = id;
		this.originator = originator;
		this.description = description;
		this.priority = priority;
		this.history = new ArrayList<>();
		this.history.add(new Event(TICKET_CREATED, Status.CREATED));
		this.assignedTo = assignedTo;
		this.history.add(new Event(String.format(TICKET_ASSIGNED, assignedTo.getId(), assignedTo.getName()), Status.ASSIGNED));
		this.ticketTags = new TreeSet<>();
	}

	public String getOriginator() {
		return originator;
	}

	public int getID() {
		return id;
	}

	public Technician getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Technician assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void addEvent(String message){
		Event event = new Event(message);
		this.history.add(event);
	}

	public void addEvent(String message, Status status){
		Event event = new Event(message, status);
		this.history.add(event);
	}

	public List<Event> getHistory(){
		return this.history;
	}

	public String getEventsWithMessage(String message){
		StringBuilder sb = new StringBuilder();
		history.forEach(h -> {
			if(h.getNote().contains(message)){
				sb.append(h.toString());
				sb.append("\n");
			}
		});
		return sb.toString();
	}

	public Status getStatus(){
		Event latestEventWithStatus = this.history.stream().filter(h -> h.getNewStatus() != null)
				.sorted(Comparator.comparing(Event::getTimestamp)).reduce(($, elem) -> elem).get();
		return latestEventWithStatus.getNewStatus();
	}

	@Override
	public int compareTo(Ticket o) {
		if(this.priority != o.priority){
			return Integer.compare(this.priority.ordinal(), o.priority.ordinal());
		}
		return Integer.compare(-this.id, -o.id);
	}

	public void addTagToTicket(Tag t){
		this.ticketTags.add(t);
	}

	public SortedSet<Tag> getTicketTags() {
		return ticketTags;
	}

	public int getMinutesToResolve(){
		if(getStatus() != Status.RESOLVED) {
			return 0;
		}
		Event creationEvent = this.history.stream().filter(h -> h.getNewStatus() == Status.CREATED)
				.reduce(($, event) -> event).get();
		Event resolutionEvent = this.history.stream().filter(h -> h.getNewStatus() == Status.RESOLVED)
				.reduce(($, event) -> event).get();
		long resolutionTime = TimeUnit.MILLISECONDS.toMinutes(resolutionEvent.getTimestamp() - creationEvent.getTimestamp());
		return (int) resolutionTime;
	}

	public boolean includesText(String searchText) {
		if(description.toLowerCase().contains(searchText.toLowerCase())){
			return true;
		}
		for(Event e : history){
			if(e.getNote().toLowerCase().contains(searchText.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	public boolean hasTag(Tag tag){
		return ticketTags.contains(tag);
	}
}
