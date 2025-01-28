package com.amica.help;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a technician ont he staff of the help desk.
 *
 * @author Will Provost
 */
@Getter
@EqualsAndHashCode(of="ID")
public class Technician implements Comparable<Technician>  {

	public static final Comparator<Technician> COMPARE_BY_LOAD =
			Comparator.comparing(Technician::getLoad);
	
	private String ID;
	private String name;
	private int extension;
	private SortedSet<Ticket> activeTickets = new TreeSet<>();

	public Technician(String ID, String name, int extension) {
		this.ID = ID;
		this.name = name;
		this.extension = extension;
	}
  
	public Stream<Ticket> getActiveTickets() {
		return activeTickets.stream();
	}
  
	public int getLoad() {
		return activeTickets.size();
	}
	
	public boolean addActiveTicket(Ticket ticket) {
		return activeTickets.add(ticket);
	}
	
	public boolean removeActiveTicket(Ticket ticket) {
		return activeTickets.remove(ticket);
	}
	
	@Override
	public String toString() {
		return String.format("Technician %s, %s", ID, name);
	}
  
	public int compareTo(Technician other) {
		return ID.compareTo(other.getID());
	}
}
