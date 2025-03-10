package com.amica.billing.db;

import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import com.amica.billing.db.mongo.MongoPersistence;
import com.amica.billing.parse.ParserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class Migration {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    ParserPersistence source;
    @Autowired
    MongoPersistence target;

    public void migrate() {
        invoiceRepository.deleteAll();
        customerRepository.deleteAll();
        source.load();
        target.load();
        Map<String, Customer> customers = source.getCustomers();
        customers.values().forEach(c -> {
            target.saveCustomer(c);
        });
        Map<Integer, Invoice> invoices = source.getInvoices();
        invoices.values().forEach(i -> {
            target.saveInvoice(i);
        });
    }
}
