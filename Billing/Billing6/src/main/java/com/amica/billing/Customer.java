package com.amica.billing;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Simple JavaBean representing a customer.
 *
 * @author Will Provost
 */
@Getter
@EqualsAndHashCode(of={"firstName", "lastName"})
@NoArgsConstructor
public class Customer {
    private String firstName;
    private String lastName;
    private Terms terms;
    private String _id;

    public Customer(String firstName, String lastName, Terms terms) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.terms = terms;
    }
    
    public String getName() {
    	return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
    	return "Customer: " + getName();
    }
}
