package com.amica.billing.parse;

import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import org.junit.jupiter.api.Test;

import static com.amica.billing.TestUtility.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Unit test for the {@link CSVParser}. Relies on data sets in the 
 * {@link TestUtility} and its own CSV representations of those data sets,
 * help in memory as lists of strings, to drive the parsing and producing
 * methods and expect clean translations between string and object forms.
 * 
 * @author Will Provost
 */
public class CSVParserTest {
	Parser parser = new CSVParser();

	public static final List<String> GOOD_CUSTOMER_DATA = Stream.of
			("Customer,One,CASH",
			 "Customer,Two,45",
			 "Customer,Three,30").toList();
	
	public static final List<String> BAD_CUSTOMER_DATA = Stream.of 
			("Customer,One,CASHY_MONEY", 
			 "Customer,Two",
			 "Customer,Three,30").toList();

	public static final List<String> GOOD_INVOICE_DATA = Stream.of 
			("1,Customer,One,100,2022-01-04",
			 "2,Customer,Two,200,2022-01-04,2022-01-05",
			 "3,Customer,Two,300,2022-01-06",
			 "4,Customer,Two,400,2021-11-11",
			 "5,Customer,Three,500,2022-01-04,2022-01-08",
			 "6,Customer,Three,600,2021-12-04").toList();
	
	public static final List<String> BAD_INVOICE_DATA = Stream.of
			("1,Customer,One,100,2022-01-04",
			 "2,Customer,Two,200,2022-01-04,2022-01-05",
			 "3,Customer,Two,300",
			 "4,Customer,Four,400,2021-11-11",
			 "5,Customer,Three,500,2022-01-04,20220108",
			 "6,Customer,Three,600,2021-12-04").toList();

	@Test
	void testGoodCustomers() {
		Stream<Customer> goodCustomers = parser.parseCustomers(GOOD_CUSTOMER_DATA.stream());
		List<String> goodCustomerNames = GOOD_CUSTOMERS.stream().map(Customer::getName).collect(Collectors.toList());
		assertThat(goodCustomers).extracting(Customer::getName)
				.containsAll(goodCustomerNames);
	}

	@Test
	void testBadCustomers() {
		Stream<Customer> badCustomers = parser.parseCustomers(BAD_CUSTOMER_DATA.stream());
		List<String> badCustomerNames = BAD_CUSTOMERS.stream().map(Customer::getName).collect(Collectors.toList());
		assertThat(badCustomers).extracting(Customer::getName)
				.containsAll(badCustomerNames);
	}

	@Test
	void testGoodInvoices() {
		Stream<Invoice> goodInvoices = parser.parseInvoices(GOOD_INVOICE_DATA.stream(), GOOD_CUSTOMERS_MAP);
		List<Integer> goodInvoiceNumbers = GOOD_INVOICES.stream().map(Invoice::getNumber).collect(Collectors.toList());
		assertThat(goodInvoices).extracting(Invoice::getNumber)
				.containsAll(goodInvoiceNumbers);
	}

	@Test
	void testBadInvoices() {
		Stream<Invoice> badInvoices = parser.parseInvoices(BAD_INVOICE_DATA.stream(), GOOD_CUSTOMERS_MAP);
		List<Integer> badInvoiceNumbers = BAD_INVOICES.stream().map(Invoice::getNumber).collect(Collectors.toList());
		assertThat(badInvoices).extracting(Invoice::getNumber)
				.containsAll(badInvoiceNumbers);
	}

	@Test
	void testProduceCustomers() {
		Stream<Customer> goodCustomers = GOOD_CUSTOMERS.stream();
		assertThat(parser.produceCustomers(goodCustomers)).containsAll(GOOD_CUSTOMER_DATA);
	}

	@Test
	void testProduceInvoices() {
		Stream<Invoice> goodInvoices = GOOD_INVOICES.stream();
		Stream<String> invoices = parser.produceInvoices(goodInvoices).map(s -> s.replace(".00", ""));
		assertThat(invoices).containsAll(GOOD_INVOICE_DATA);
	}
}
