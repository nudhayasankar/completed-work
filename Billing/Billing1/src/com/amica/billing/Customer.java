package com.amica.billing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

    public enum Terms {
        CASH(0),
        CREDIT_30(30),
        CREDIT_60(60),
        CREDIT_90(90);

        private int days;

        Terms(int days) {
            this.days = days;
        }

        public int getDays() {
            return this.days;
        }
    };

    String firstName;
    String lastName;
    Terms paymentTerm;

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    public Terms getTerms() {
        return this.paymentTerm;
    }

    public static Terms mapTermsByDays(int days) {
        switch (days) {
            case 0:
                return Terms.CASH;
            case 30:
                return Terms.CREDIT_30;
            case 60:
                return Terms.CREDIT_60;
            case 90:
                return Terms.CREDIT_90;
            default:
                return null;
        }
    }
}
