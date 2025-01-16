package com.amica.billing;

import com.amica.billing.parse.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Billing {
    String customerFilePath;
    String invoiceFilePath;
    Parser customerParser;
    Parser invoiceParser;
    Map<String, Customer> customers;
    List<Invoice> invoices;
    Comparator<Invoice> byNumber = Comparator.comparing(Invoice::getNumber);
    Comparator<Invoice> byIssueDate = Comparator.comparing(Invoice::getInvoiceDate);
    List<Consumer<Invoice>> invoiceListeners;

    public Billing(String customerFile, String invoiceFile) {
        customerFilePath = customerFile;
        invoiceFilePath = invoiceFile;
        ParserFactory parserFactory = new ParserFactory();
        customerParser = parserFactory.createParser(customerFile);
        invoiceParser = parserFactory.createParser(invoiceFile);
        Stream<String> customerLines = readFile(customerFile);
        Stream<String> invoiceLines = readFile(invoiceFile);
        Stream<Customer> customerStream = customerParser.parseCustomers(customerLines);
        customers = customerStream.collect(Collectors.toMap(Customer::getName, Function.identity()));
        Stream<Invoice> invoiceStream = invoiceParser.parseInvoices(invoiceLines, customers);
        invoices = invoiceStream.collect(Collectors.toList());
        invoiceListeners = new ArrayList<>();
    }

    public Parser getCustomerParser() {
        return customerParser;
    }

    public Parser getInvoiceParser() {
        return invoiceParser;
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

    @SneakyThrows
    public void saveCustomers() {
        Stream<Customer> customerStream = customers.values().stream();
        Stream<String> parsedCustomers = customerParser.produceCustomers(customerStream);
        try(PrintWriter pw = new PrintWriter(new File(customerFilePath))) {
            parsedCustomers.forEach(pc -> {
                pw.write(pc);
                pw.write("\n");
            });
        }
    }

    @SneakyThrows
    public void saveInvoices() {
        Stream<Invoice> invoiceStream = invoices.stream();
        Stream<String> parsedInvoices = invoiceParser.produceInvoices(invoiceStream);
        try(PrintWriter pw = new PrintWriter(new File(customerFilePath))) {
            parsedInvoices.forEach(pi -> {
                pw.write(pi);
                pw.write("\n");
            });
        }
    }

    public void payInvoice(int invoiceNumber) {
        Invoice invoice = findInvoice(invoiceNumber);
        if(invoice != null) {
            invoice.paidDate = LocalDate.now();
        }
        saveInvoices();
        invoiceListeners.forEach(listener -> listener.accept(invoice));
    }

    private Invoice findInvoice(int invoiceNumber) {
        Invoice invoice = invoices.stream().filter(i -> i.getNumber() == invoiceNumber).findFirst().orElse(null);
        return invoice;
    }

    public void addListener(Consumer<Invoice> listener) {
        invoiceListeners.add(listener);
    }

    public void removeListener(Consumer<Invoice> listener) {
        invoiceListeners.remove(listener);
    }
}
