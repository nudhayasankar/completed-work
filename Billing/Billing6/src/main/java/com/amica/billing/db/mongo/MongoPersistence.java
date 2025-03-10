package com.amica.billing.db.mongo;

import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import com.amica.billing.db.CachingPersistence;
import com.amica.billing.db.CustomerRepository;
import com.amica.billing.db.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Primary
public class MongoPersistence extends CachingPersistence {
    CustomerRepository customerRepository;
    InvoiceRepository invoiceRepository;

    public MongoPersistence(CustomerRepository cr, InvoiceRepository ir) {
        customerRepository = cr;
        invoiceRepository = ir;
    }

    @Override
    @PostConstruct
    public void load() {
        super.load();
    }

    @Override
    protected Stream<Customer> readCustomers() {
        return customerRepository.streamAllBy();
    }

    @Override
    protected Stream<Invoice> readInvoices() {
        return invoiceRepository.streamAllBy();
    }

    @Override
    protected void writeCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    protected void writeInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }
}
