package com.amica.billing.db;

import com.amica.billing.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    public Stream<Customer> streamAllBy();
    public Customer findByFirstNameAndLastName(String firstName, String lastName);

}
