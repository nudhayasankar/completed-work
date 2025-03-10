package com.amica.billing.parse;

import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import com.amica.billing.ParserFactory;
import com.amica.billing.db.CachingPersistence;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.stream.Stream;

@Component
@Log
public class ParserPersistence extends CachingPersistence {
    @Value("${ParserPersistence.customersFile}")
    @Setter
    String customersFile;

    @Value("${ParserPersistence.invoicesFile}")
    @Setter
    String invoicesFile;

    Parser parser;

    @Override
    @PostConstruct
    public void load() {
        parser = ParserFactory.createParser(invoicesFile);
        super.load();
    }

    @Override
    protected Stream<Customer> readCustomers() {
        try {
            Stream<String> customerLines = Files.lines(Paths.get(customersFile));
            return parser.parseCustomers(customerLines);
        } catch(IOException ioException) {
            log.warning(ioException.getMessage());
            return Stream.of(null);
        }
    }

    @Override
    protected Stream<Invoice> readInvoices() {
        try {
            Stream<String> invoiceLines = Files.lines(Paths.get(invoicesFile));
            return parser.parseInvoices(invoiceLines, customers);
        } catch(IOException ioException) {
            log.warning(ioException.getMessage());
            return Stream.of(null);
        }
    }

    @Override
    protected void writeCustomer(Customer customer) {
        try (PrintWriter out = new PrintWriter(new FileWriter(customersFile)); ) {
            parser.produceCustomers(customers.values().stream())
                    .forEach(out::println);
        } catch (Exception ex) {
            log.log(Level.WARNING, ex,
                    () -> "Couldn't open " + customersFile + " in write mode.");
        }
    }

    @Override
    protected void writeInvoice(Invoice invoice) {
        try ( PrintWriter out = new PrintWriter(new FileWriter(invoicesFile)); ) {
            parser.produceInvoices(invoices.values().stream())
                    .forEach(out::println);
        } catch (Exception ex) {
            log.log(Level.WARNING, ex,
                    () -> "Couldn't open " + invoicesFile + " in write mode.");
        }
    }
}
