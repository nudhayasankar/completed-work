package com.amica.help;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

	public Ticket(int id, String originator, String description, Priority priority, Technician assignedTo){
		this.id = id;
		this.originator = originator;
		this.description = description;
		this.priority = priority;
		this.history = new ArrayList<>();
		this.history.add(new Event(TICKET_CREATED, Status.CREATED));
		this.assignedTo = assignedTo;
		this.history.add(new Event(String.format(TICKET_ASSIGNED, assignedTo.getId(), assignedTo.getName()), Status.ASSIGNED));
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
}
