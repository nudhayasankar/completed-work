package com.amica.billing.db;

import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public abstract class CachingPersistence implements Persistence{
   protected Map<String, Customer> customers;
   protected Map<Integer, Invoice> invoices;

   protected abstract Stream<Customer> readCustomers();
   protected abstract Stream<Invoice> readInvoices();
   protected abstract void writeCustomer(Customer customer);
   protected abstract void writeInvoice(Invoice invoice);

   public void load() {
       try(Stream<Customer> customerStream = readCustomers()) {
           customers = customerStream.collect(Collectors.toMap(Customer::getName, Function.identity()));
       }
       try(Stream<Invoice> invoiceStream = readInvoices()) {
           invoices = invoiceStream.collect(Collectors.toMap(Invoice::getNumber, Function.identity()));
       }
   }

    @Override
    public void saveCustomer(Customer customer) {
        customers.put(customer.getName(), customer);
        writeCustomer(customer);
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        invoices.put(invoice.getNumber(), invoice);
        writeInvoice(invoice);
    }

}
