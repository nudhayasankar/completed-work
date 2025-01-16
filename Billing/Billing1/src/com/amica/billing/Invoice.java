package com.amica.billing;

import com.amica.billing.parse.FlatParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.amica.billing.Customer.*;

@Data
@AllArgsConstructor
public class Invoice {
    int number;
    double amount;
    LocalDate invoiceDate;
    LocalDate paidDate;
    Customer customer;
    static final int DATE_LENGTH = 8;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public boolean isOverdue(LocalDate asOf) {
        Terms paymentTerm = customer.paymentTerm;
        int days = paymentTerm.getDays();
        if(paidDate == null) {
            return asOf.isBefore(invoiceDate.plusDays(days));
        } else {
            return paidDate.isBefore(invoiceDate.plusDays(days));
        }
    }

    public String formatInvoice() {
        String flattenedInvoice = String.format("%%%dd  %%-%ds  %%-%ds  %%%d.2f  %%%ds  %%%ds",
                FlatParser.INVOICE_NUMBER_LENGTH,
                FlatParser.CUSTOMER_FIRST_NAME_LENGTH,
                FlatParser.CUSTOMER_LAST_NAME_LENGTH,
                FlatParser.INVOICE_AMOUNT_LENGTH,
                DATE_LENGTH,
                DATE_LENGTH);
        return String.format(flattenedInvoice, number, customer.getFirstName(), customer.getLastName(), amount
                , formatter.format(invoiceDate), paidDate == null ? "" : formatter.format(paidDate));
    }

}
