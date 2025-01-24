package com.amica.billing.report;

import com.amica.billing.Billing;
import com.amica.billing.Customer;
import com.amica.billing.Invoice;
import lombok.SneakyThrows;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.amica.billing.parse.*;
import lombok.extern.java.Log;

@Log
public class Reporter {
    final static String VOLUME_SUFFIX = "_customer_and_volume.txt";
    final static String CUSTOMER_SUFFIX = "_invoices_by_customer.txt";
    final static String OVERDUE_SUFFIX = "_overdue_invoices.txt";
    final static String NUMBER_SUFFIX = "_invoices_by_number.txt";
    final static String NUMBER_TITLE = "All invoices, ordered by invoice number";
    final static String CUSTOMER_GROUPED_TITLE = "All invoices, grouped by customer and ordered by invoice number";
    final static String LINE_BREAK_1 = "=";
    final static int COLUMN_WIDTH_1 = 66;
    final static int COLUMN_WIDTH_2 = 78;
    final static int NUMBER_OFFSET = 6;
    final static int CUSTOMER_NAME_OFFSET = 26;
    final static int DATE_AMT_OFFSET = 12;
    final static int SPACE_OFFSET = 1;
    final static String HEADING1 = String.format("%%%ds%%-%ds%%-%ds%%-%ds%%-%ds", NUMBER_OFFSET, CUSTOMER_NAME_OFFSET, DATE_AMT_OFFSET, DATE_AMT_OFFSET, DATE_AMT_OFFSET);
    //final static String SEPARATOR1 = "----  ------------------------  ----------  ----------  ----------";
    final static String SEPARATOR1 = String.format("-".repeat(4) + "  " + "-".repeat(24) + "  "
            + "-".repeat(10) + "  " + "-".repeat(10) + "  " + "-".repeat(10));

    Billing billing;
    String outputPath;
    LocalDate asOf;

    public Reporter(Billing billing, String outputPath, LocalDate asOf) {
        this.billing = billing;
        this.outputPath = outputPath;
        this.asOf = asOf;
        this.billing.addListener(invoice -> onInvoiceChanged());
    }

    @SneakyThrows
    public void reportInvoicesOrderedByNumber() {
        String fileName = generateFilePath(NUMBER_SUFFIX);
        Stream<Invoice> invoicesByNumber = billing.getInvoicesOrderedByNumber();
        try (PrintWriter pw = new PrintWriter(fileName))
             {
                 pw.println(NUMBER_TITLE);
                 pw.println(LINE_BREAK_1.repeat(COLUMN_WIDTH_1));
                 pw.println();
                 pw.println(String.format(HEADING1, "", "Customer", "Issued", "Amount", "Paid"));
                 pw.println(SEPARATOR1);
                invoicesByNumber.forEach((i) -> pw.println(i.formatInvoice()));
        }
        log.info("Done");
    }

    @SneakyThrows
    public void reportInvoicesGroupedByCustomer() {
        String fileName = generateFilePath(CUSTOMER_SUFFIX);
        Map<Customer, List<Invoice>> invoicesByCustomer = billing.getInvoicesGroupedByCustomer();
        try (PrintWriter pw = new PrintWriter(fileName))
        {
            pw.println(CUSTOMER_GROUPED_TITLE);
            pw.println(LINE_BREAK_1.repeat(COLUMN_WIDTH_1));
            pw.println();
            pw.println(String.format(HEADING1, "", "Customer", "Issued", "Amount", "Paid"));
            pw.println(SEPARATOR1);
            invoicesByCustomer.forEach((customer, invoices) -> {
                pw.println();
                pw.println(customer.getName());
                invoices.forEach((i) -> pw.println(i.formatInvoice()));
            });
        }
        log.info("Done");
    }

    private String generateFilePath(String suffix) {
        String[] delimitedPath = outputPath.split("/");
        String prefix = delimitedPath[1];
        StringBuilder sb = new StringBuilder();
        sb.append(outputPath).append("/").append(prefix).append(suffix);
        return sb.toString();
    }

    public void onInvoiceChanged() {
        reportInvoicesOrderedByNumber();
    }
}
