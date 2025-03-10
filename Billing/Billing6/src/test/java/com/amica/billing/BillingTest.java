package com.amica.billing;

import static com.amica.billing.TestUtility.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import com.amica.billing.db.Persistence;

/**
 * Unit test for the {@link Billing} class.
 * This test focuses on the test data set defined in {@link TestUtillity}
 * and prepared data files that reflect that data. We make a copy of the
 * data files at the start of each test case, create the Billing object
 * to load them, and check its getters and query methods.
 * A few more test cases drive updates to the object, and assure that
 * they are reflected in updates to the staged data files.
 * 
 * @author Will Provost
 */
public class BillingTest {

	private Billing billing;
	private Persistence mockPersistence;
	private Consumer<Customer> customerListener;
	private Consumer<Invoice> invoiceListener;

	/**
	 * Assure that the necessary folders are in place, and make a copy
	 * of the source data files. Install mock objects as listeners for changes.
	 */
	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setUp() {
		mockPersistence = mock(Persistence.class);
		when(mockPersistence.getCustomers()).thenReturn(GOOD_CUSTOMERS_MAP);
		when(mockPersistence.getInvoices()).thenReturn(GOOD_INVOICES_MAP);
		
		billing = new Billing(mockPersistence);
		
		billing.addCustomerListener(customerListener = mock(Consumer.class));
		billing.addInvoiceListener(invoiceListener = mock(Consumer.class));
	}
	
	@Test
	public void testGetCustomers() {
		Map<String,Customer> customers = billing.getCustomers(); 
		assertThat(customers.keySet()).hasSize(GOOD_CUSTOMERS.size());
		for (String name : customers.keySet()) {
			assertThat(customers.get(name))
					.usingRecursiveComparison()
					.isEqualTo(GOOD_CUSTOMERS_MAP.get(name));
		}
	}
	
	@Test
	public void testGetInvoices() {
		Map<Integer,Invoice> invoices =billing.getInvoices();
		assertThat(invoices.keySet()).hasSize(GOOD_INVOICES.size());
		for (int number : invoices.keySet()) {
			assertThat(invoices.get(number))
					.usingRecursiveComparison()
					.isEqualTo(GOOD_INVOICES_MAP.get(number));
		}
	}
	
	@Test
	public void testGetInvoicesOrderedByNumber() {
		assertThat(billing.getInvoicesOrderedByNumber())
				.extracting(Invoice::getNumber)
				.contains(1, 2, 3, 4, 5, 6);
	}
	
	@Test
	public void testGetInvoicesOrderedByDate() {
		assertThat(billing.getInvoicesOrderedByDate())
				.extracting(Invoice::getNumber)
				.contains(4, 6, 1, 2, 5, 3);
	}
	
	@Test
	public void testGetInvoicesGroupedByCustomer() {
		Map<Customer,List<Invoice>> map = billing.getInvoicesGroupedByCustomer();
		assertThat(map.get(GOOD_CUSTOMERS.get(0)).stream())
				.extracting(Invoice::getNumber)
				.contains(1);
		assertThat(map.get(GOOD_CUSTOMERS.get(1)).stream())
				.extracting(Invoice::getNumber)
				.contains(2, 3, 4);
		assertThat(map.get(GOOD_CUSTOMERS.get(2)).stream())
				.extracting(Invoice::getNumber)
				.contains(5, 6);
	}

	@Test
	public void testGetOverdueInvoices() {
		assertThat(billing.getOverdueInvoices(AS_OF_DATE))
				.extracting(Invoice::getNumber)
				.contains(4, 6, 1);
	}
	
	@Test
	public void testGetCustomersAndVolume() {
		List<Billing.CustomerAndVolume> list = 
				billing.getCustomersAndVolumeStream().toList();
		assertThat(list.get(0).getCustomer()).isEqualTo(GOOD_CUSTOMERS.get(2));
		assertThat(list.get(0).getVolume()).isCloseTo(1100.0, within(.0001));
		assertThat(list.get(1).getCustomer()).isEqualTo(GOOD_CUSTOMERS.get(1));
		assertThat(list.get(1).getVolume()).isCloseTo(900.0, within(.0001));
		assertThat(list.get(2).getCustomer()).isEqualTo(GOOD_CUSTOMERS.get(0));
		assertThat(list.get(2).getVolume()).isCloseTo(100.0, within(.0001));
	}
	
	/**
	 * After adding a customer, assure that there is one new line in the
	 * customers data file. We also verify that the object makes the required 
	 * outbound call to the registered customer listener.
	 */
	@Test
	public void testCreateCustomer() {
		final String FIRST_NAME = "Customer";
		final String LAST_NAME = "Four";
		final Terms TERMS = Terms.CASH;
		
		billing.createCustomer(FIRST_NAME, LAST_NAME, TERMS);
		
		verify(mockPersistence).saveCustomer(argThat
			(c -> c.getFirstName().equals(FIRST_NAME) &&
				c.getLastName().equals(LAST_NAME) &&
				c.getTerms() == TERMS));		

		verify(customerListener).accept(any(Customer.class));
	}
	
	@Test
	public void testCreateCustomer_Existing() {
		assertThrows(IllegalArgumentException.class,
				() -> billing.createCustomer("Customer", "One", Terms.CASH));
	}
	
	/**
	 * After adding an invoice, assure that there is one new line in the
	 * invoices data file. We also verify that the object makes the required 
	 * outbound call to the registered invoice listener.
	 */
	@Test
	public void testCreateInvoice() {

		final int NEXT_INVOICE_NUMBER = GOOD_INVOICES.stream()
				.mapToInt(Invoice::getNumber)
				.max().getAsInt() + 1;
		final Customer CUSTOMER = GOOD_CUSTOMERS.get(0);
		final double AMOUNT = 999.0;
		billing.createInvoice(CUSTOMER.getName(), AMOUNT);

		verify(mockPersistence).saveInvoice(argThat
			(i -> i.getNumber() == NEXT_INVOICE_NUMBER &&
				i.getCustomer().equals(CUSTOMER) &&
				Math.abs(i.getAmount() - AMOUNT) < .0001));		
		
		verify(invoiceListener).accept(any(Invoice.class));
	}
	
	@Test
	public void testCreateInvoice_NoSuchCustomer() {
		assertThrows(IllegalArgumentException.class, 
				() -> billing.createInvoice("Customer Five", 999));
	}
	
	/**
	 * After paying an invoice, assure that the line for that invoice in the
	 * data file now bears the correct paid date. We also verify that the
	 * object makes the required outbound call to the registered invoice listener.
	 */
	@Test
	public void testPayInvoice() {
	
		billing.payInvoice(1);
		
		ArgumentMatcher<Invoice> isPaidInvoice =
			i -> i.getNumber() == 1 && i.getPaidDate().isPresent();
		verify(mockPersistence).saveInvoice(argThat(isPaidInvoice));
		verify(invoiceListener).accept(argThat(isPaidInvoice));
	}
	
	@Test
	public void testPayInvoice_NoSuchInvoice() {
		assertThrows(IllegalArgumentException.class, () -> billing.payInvoice(11));
	}
	
	
	@Test
	public void testPayInvoice_AlreadyPaid() {
		assertThrows(IllegalStateException.class, () -> billing.payInvoice(2));
	}
}
