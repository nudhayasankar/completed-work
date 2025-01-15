package com.amica.billing;

import com.amica.billing.parse.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Billing {
    String customerFile;
    String invoiceFile;
    Parser parser;
    Map<String, Customer> customers;
    List<Invoice> invoices;
    Comparator<Invoice> byNumber = Comparator.comparing(Invoice::getNumber);
    Comparator<Invoice> byIssueDate = Comparator.comparing(Invoice::getInvoiceDate);

    public Billing(String customerFile, String invoiceFile) {
        ParserFactory parserFactory = new ParserFactory();
        Parser customerParser = parserFactory.createParser(customerFile);
        Parser invoiceParser = parserFactory.createParser(invoiceFile);
        Stream<String> customerLines = readFile(customerFile);
        Stream<String> invoiceLines = readFile(invoiceFile);
        Stream<Customer> customerStream = customerParser.parseCustomers(customerLines);
        customers = customerStream.collect(Collectors.toMap(Customer::getName, Function.identity()));
        Stream<Invoice> invoiceStream = invoiceParser.parseInvoices(invoiceLines, customers);
        invoices = invoiceStream.collect(Collectors.toList());
    }

    public Map<String, Customer> getCustomers() {
        return Collections.unmodifiableMap(customers);
    }

    public List<Invoice> getInvoices() {
        return Collections.unmodifiableList(invoices);
    }

    @SneakyThrows
    public Stream<String> readFile(String fileName) {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader.lines();
    }

    public Stream<Invoice> getInvoicesOrderedByNumber() {
        Stream<Invoice> invoicesOrderedByNumber = invoices.stream().sorted(byNumber);
        return invoicesOrderedByNumber;
    }

    public Stream<Invoice> getInvoicesOrderedByIssueDate() {
        Stream<Invoice> invoicesOrderedByIssueDate = invoices.stream().sorted(byIssueDate);
        return invoicesOrderedByIssueDate;
    }

    public Map<Customer, List<Invoice>> getInvoicesGroupedByCustomer() {
        Map<Customer, List<Invoice>> invoicesGroupedByCustomer = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getCustomer));
        return invoicesGroupedByCustomer;
    }

    public Stream<Invoice> getOverdueInvoices(LocalDate asOf) {
        Stream<Invoice> overdueInvoices = invoices.stream().filter(i -> i.isOverdue(asOf));
        return overdueInvoices;
    }

    public Map<Customer, Double> getCustomersAndVolume() {
        Map<Customer, Double> customersAndVolume = invoices.stream().
                collect(Collectors.groupingBy(Invoice::getCustomer, Collectors.summingDouble(Invoice::getAmount)));
        return customersAndVolume;
    }
}
