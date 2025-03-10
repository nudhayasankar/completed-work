package com.amica.billing.db;

import com.amica.billing.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
    public Stream<Invoice> streamAllBy();
}
