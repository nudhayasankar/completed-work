package com.amica.billing.report;

import com.amica.billing.Billing;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class Reporter {
    final static String VOLUME_SUFFIX = "_customer_and_volume.txt";
    final static String CUSTOMER_SUFFIX = "_invoices_by_customer.txt";
    final static String OVERDUE_SUFFIX = "_overdue_invoices.txt";
    final static String NUMBER_SUFFIX = "_invoices_by_number.txt";
    Billing billing;
    LocalDate asOf;
    String reportsPath;


}
