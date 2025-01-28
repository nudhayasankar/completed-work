package com.amica.help;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.amica.help.Ticket.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static  org.hamcrest.Matchers.*;

/**
 * Unit test for the {@link HelpDesk} class.
 * 
 * @author Will Provost
 */
public class HelpDeskTest {

	public static final String TECH1 = "TECH1";
	public static final String TECH2 = "TECH2";
	public static final String TECH3 = "TECH3";

	public static final int TICKET1_ID = 1;
	public static final String TICKET1_ORIGINATOR = "TICKET1_ORIGINATOR";
	public static final String TICKET1_DESCRIPTION = "TICKET1_DESCRIPTION";
	public static final Priority TICKET1_PRIORITY = Priority.LOW;
	public static final int TICKET2_ID = 2;
	public static final String TICKET2_ORIGINATOR = "TICKET2_ORIGINATOR";
	public static final String TICKET2_DESCRIPTION = "TICKET2_DESCRIPTION";
	public static final Priority TICKET2_PRIORITY = Priority.HIGH;
	
	public static final String TAG1 = "TAG1";
	public static final String TAG2 = "TAG2";
	public static final String TAG3 = "TAG3";
	
	private HelpDesk helpDesk = new HelpDesk();
	private Technician tech1;
	private Technician tech2;
	private Technician tech3;

	/**
	 * Custom matcher that checks the contents of a stream of tickets
	 * against expected IDs, in exact order;
	 */
	public static class HasIDs extends TypeSafeMatcher<Stream<? extends Ticket>> {

		private String expected;
		private String was;
		
		public HasIDs(int... IDs) {
			int[] expectedIDs = IDs;
			expected = Arrays.stream(expectedIDs)
					.mapToObj(Integer::toString)
					.collect(Collectors.joining(", ", "[ ", " ]"));		
		}
		
		public void describeTo(Description description) {
			
			description.appendText("tickets with IDs ");
			description.appendText(expected);
		}
		
		@Override
		public void describeMismatchSafely
				(Stream<? extends Ticket> tickets, Description description) {
			description.appendText("was: tickets with IDs ");
			description.appendText(was);
		}

		protected boolean matchesSafely(Stream<? extends Ticket> tickets) {
			was = tickets.mapToInt(Ticket::getID)
					.mapToObj(Integer::toString)
					.collect(Collectors.joining(", ", "[ ", " ]"));
			return expected.equals(was);
		}
		
	}

	private void createTicketN(int n) {
		switch (n) {
			case 1:
				helpDesk.createTicket(TICKET1_ORIGINATOR, TICKET1_DESCRIPTION, TICKET1_PRIORITY);
				break;
			case 2:
				helpDesk.createTicket(TICKET2_ORIGINATOR, TICKET2_DESCRIPTION, TICKET2_PRIORITY);
				break;
			default:
				break;
		}
	}

	@Test
	public void testCreateTickets() {
		createTicketN(1);
		createTicketN(2);
		Ticket ticket2 = helpDesk.getTicketByID(TICKET2_ID);
		assertThat(ticket2.getDescription(), is(TICKET2_DESCRIPTION));
	}

	@Test
	public void testNoTickets() {
		Ticket ticket2 = helpDesk.getTicketByID(TICKET2_ID);
		assertThat(ticket2, nullValue());
	}

	@Test
	public void testNoTechnicians() {
		HelpDesk tempHelpDesk = new HelpDesk();
		Exception exception = null;
		try {
			tempHelpDesk.createTicket(TICKET1_ORIGINATOR, TICKET1_DESCRIPTION, TICKET1_PRIORITY);
		} catch(Exception ex) {
			exception = ex;
		}
		assertThat(exception, instanceOf(IllegalStateException.class));
	}

	@BeforeEach
	public void setUp() {
		helpDesk.createTechnician(TECH1, TECH1, 1);
		helpDesk.createTechnician(TECH2, TECH2, 2);
		helpDesk.createTechnician(TECH3, TECH3, 3);
		Iterator<Technician> technicianIterator = helpDesk.getTechnicians().iterator();
		tech1 = technicianIterator.next();
		tech2 = technicianIterator.next();
		tech3 = technicianIterator.next();
		Clock.setTime(100);
	}

	@Test
	public void testTicket1() {
		createTicketN(1);
		Ticket ticket1 = helpDesk.getTicketByID(TICKET1_ID);
		assertThat(ticket1, both(hasProperty("ID", is(TICKET1_ID)))
				.and(hasProperty("technician", is(tech1))));
		assertThat(tech1.getActiveTickets().count(), is(1L));
	}

	@Test
	public void testTicket2() {
		createTicketN(1);
		createTicketN(2);
		Ticket ticket2 = helpDesk.getTicketByID(TICKET2_ID);
		assertThat(ticket2, both(hasProperty("ID", is(TICKET2_ID)))
				.and(hasProperty("technician", is(tech2))));
		assertThat(tech2.getActiveTickets().count(), is(1L));
	}

	@Test
	public void testResolveTicket2() {
		createTicketN(1);
		createTicketN(2);
		Ticket ticket2 = helpDesk.getTicketByID(TICKET2_ID);
		ticket2.resolve("TICKET RESOLVED");
		assertThat(helpDesk.getTicketsByStatus(Ticket.Status.ASSIGNED).count(), is(1L));
		assertThat(helpDesk.getTicketsByStatus(Ticket.Status.RESOLVED).count(), is(1L));
	}

	@Test
	public void testNotByStatus() {
		createTicketN(1);
		createTicketN(2);
		assertThat(helpDesk.getTicketsByNotStatus(Ticket.Status.WAITING), hasIDs(2, 1));
	}

	@Test
	public void testByTags() {
		createTicketN(1);
		createTicketN(2);
		Ticket ticket2 = helpDesk.getTicketByID(TICKET2_ID);
		ticket2.addTag(new Tag(TAG2));
		assertThat(helpDesk.getTicketsWithAnyTag(TAG1, TAG2).count(), is(1L));
	}

	@Test
	public void testByTechnician() {
		createTicketN(1);
		createTicketN(2);
		assertThat(helpDesk.getTicketsByTechnician(TECH1), hasIDs(1));
	}

	@Test
	public void testByText() {
		createTicketN(1);
		createTicketN(2);
		assertThat(helpDesk.getTicketsByText(TICKET2_DESCRIPTION), hasIDs(2));
	}

	public static Matcher<Stream<? extends Ticket>> hasIDs(int... IDs) {
		return new HasIDs(IDs);
	}
// Step5 uses a generic stream matcher:
//	public static Matcher<Stream<? extends Ticket>> hasIDs(Integer... IDs) {
//		return HasKeys.hasKeys(Ticket::getID, IDs);
//	}
}
