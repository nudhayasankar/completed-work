package com.amica.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import com.amica.billing.Customer.*;

@Data
@AllArgsConstructor
public class Invoice {
    int number;
    double amount;
    LocalDate invoiceDate;
    LocalDate paidDate;
    Customer customer;

    public boolean isOverdue(LocalDate asOf) {
        Terms paymentTerm = customer.paymentTerm;
        int days = paymentTerm.getDays();
        if(paidDate == null) {
            return asOf.isBefore(invoiceDate.plusDays(days));
        } else {
            return paidDate.isBefore(invoiceDate.plusDays(days));
        }
    }

}
