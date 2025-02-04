package com.amica.billing;

import static com.amica.billing.TestUtility.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amica.billing.parse.CSVParser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

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

	public static final String SOURCE_FOLDER = "src/test/resources/data";
	Billing billing;
	public static final String CUSTOMER_FIRSTNAME = "James";
	public static final String CUSTOMER_LASTNAME = "Weber";
	public static final Terms CUSTOMER_TERM = Terms.CREDIT_30;
	public static final int INVOICE_ID = 999;
	public static final double INVOICE_AMT = 999.00;
	Consumer<Customer> mockCustomerListener;
	Consumer<Invoice> mockInvoiceListener;

	/**
	 * Assure that the necessary folders are in place, and make a copy
	 * of the source data files. Install mock objects as listeners for changes.
	 */
	@BeforeEach
	public void setUp() throws IOException {
		Files.createDirectories(Paths.get(TEMP_FOLDER));
		Files.createDirectories(Paths.get(OUTPUT_FOLDER));
		Files.copy(Paths.get(SOURCE_FOLDER, CUSTOMERS_FILENAME), 
				Paths.get(TEMP_FOLDER, CUSTOMERS_FILENAME),
				StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(SOURCE_FOLDER, INVOICES_FILENAME), 
				Paths.get(TEMP_FOLDER, INVOICES_FILENAME),
				StandardCopyOption.REPLACE_EXISTING);

		billing = new Billing(TEMP_FOLDER+"/"+CUSTOMERS_FILENAME, TEMP_FOLDER+"/"+INVOICES_FILENAME);
		mockCustomerListener = Mockito.mock(Consumer.class);
		billing.addCustomerListener(mockCustomerListener);
		mockInvoiceListener = Mockito.mock(Consumer.class);
		billing.addInvoiceListener(mockInvoiceListener);
	}

	@Test
	public void testInvoicesOrderedByNumber() {
		Stream<Invoice> invoices = billing.getInvoicesOrderedByNumber();
		assertThat(invoices).extracting(Invoice::getNumber).containsExactly(1, 2, 3, 4, 5, 6);
	}

	@Test
	public void testInvoicesOrderedByDate() {
		Stream<Invoice> invoices = billing.getInvoicesOrderedByDate();
		assertThat(invoices).extracting(Invoice::getNumber).containsExactly(4, 6, 1, 2, 5, 3);
	}

	@Test
	public void testInvoicesGroupedByCustomer() {
		Map<Customer, List<Invoice>> invoices = billing.getInvoicesGroupedByCustomer();
		Customer customer1 = GOOD_CUSTOMERS.get(0);
		Customer customer2 = GOOD_CUSTOMERS.get(1);
		Customer customer3 = GOOD_CUSTOMERS.get(2);
		assertThat(invoices.get(customer1)).extracting(Invoice::getNumber).containsExactly(1);
		assertThat(invoices.get(customer2)).extracting(Invoice::getNumber).containsExactly(2, 3, 4);
		assertThat(invoices.get(customer3)).extracting(Invoice::getNumber).containsExactly(5, 6);
	}

	@Test
	public void testGetOverdueInvoices() {
		Stream<Invoice> invoices = billing.getOverdueInvoices(AS_OF_DATE);
		assertThat(invoices).extracting(Invoice::getNumber).containsExactly(4, 6, 1);
	}

	@Test
	@SneakyThrows
	public void testCreateCustomer() {
		Customer customer = new Customer(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_TERM);
		billing.createCustomer(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_TERM);
		CSVParser parser = new CSVParser();
		try(Stream<String> lines = Files.lines(Paths.get(TEMP_FOLDER+"/"+CUSTOMERS_FILENAME))) {
			assertThat(lines).anyMatch(line -> line.equalsIgnoreCase(parser.formatCustomer(customer)));
		}
		Mockito.verify(mockCustomerListener);
	}

	@Test
	@SneakyThrows
	public void testCreateInvoice() {
		billing.createCustomer(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_TERM);
		Customer customer = billing.getCustomers().get(CUSTOMER_FIRSTNAME + " " + CUSTOMER_LASTNAME);
		Invoice invoice = billing.createInvoice(customer.getName(), INVOICE_AMT);
		CSVParser parser = new CSVParser();
		try(Stream<String> lines = Files.lines(Paths.get(TEMP_FOLDER+"/"+INVOICES_FILENAME))) {
			assertThat(lines).anyMatch(line -> line.equalsIgnoreCase(parser.formatInvoice(invoice)));
		}
		Mockito.verify(mockInvoiceListener);
	}

	@Test
	@SneakyThrows
	public void testPayInvoice() {
		billing.createCustomer(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_TERM);
		Customer customer = billing.getCustomers().get(CUSTOMER_FIRSTNAME + " " + CUSTOMER_LASTNAME);
		Invoice invoice = billing.createInvoice(customer.getName(), INVOICE_AMT);
		billing.payInvoice(invoice.getNumber());
		CSVParser parser = new CSVParser();
		try(Stream<String> lines = Files.lines(Paths.get(TEMP_FOLDER+"/"+INVOICES_FILENAME))) {
			assertThat(lines).anyMatch(line -> line.equalsIgnoreCase(parser.formatInvoice(invoice)));
		}
		Mockito.verify(mockInvoiceListener);
	}
}
