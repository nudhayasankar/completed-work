package com.amica.billing.report;

import com.amica.billing.Billing;
import com.amica.billing.Invoice;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Stream;
import com.amica.billing.parse.*;

public class Reporter {
    final static String VOLUME_SUFFIX = "_customer_and_volume.txt";
    final static String CUSTOMER_SUFFIX = "_invoices_by_customer.txt";
    final static String OVERDUE_SUFFIX = "_overdue_invoices.txt";
    final static String NUMBER_SUFFIX = "_invoices_by_number.txt";
    final static String NUMBER_TITLE = "All invoices, ordered by invoice number\n";
    final static String LINE_BREAK_1 = "==================================================================\n";
    final static String LINE_BREAK_2 = "==============================================================================\n";
    final static String HEADING1 = "       Customer                     Issued      Amount        Paid\n";
    final static String SEPARATOR1 = "----  ------------------------  ----------  ----------  ----------\n";

    Billing billing;
    String outputPath;
    LocalDate asOf;

    public Reporter(Billing billing, String outputPath, LocalDate asOf) {
        this.billing = billing;
        this.outputPath = outputPath;
        this.asOf = asOf;
        this.billing.addListener(invoice -> onInvoiceChanged(invoice));
    }

    @SneakyThrows
    public void reportInvoicesOrderedByNumber() {
        String fileName = generateFilePath(NUMBER_SUFFIX);
        Stream<Invoice> invoicesByNumber = billing.getInvoicesOrderedByNumber();
        try (PrintWriter pw = new PrintWriter(new File(fileName)))
             {
                 pw.write(NUMBER_TITLE);
                 pw.write(LINE_BREAK_1);
                 pw.write(HEADING1);
                 pw.write(SEPARATOR1);
                invoicesByNumber.forEach((i) -> {
                    pw.write(i.formatInvoice());
                    pw.write("\n");
                });
        }
        System.out.println("Done");
    }

    private String generateFilePath(String suffix) {
        String[] delimitedPath = outputPath.split("/");
        String prefix = delimitedPath[1];
        StringBuilder sb = new StringBuilder();
        sb.append(outputPath).append("/").append(prefix).append(suffix);
        return sb.toString();
    }

    public void onInvoiceChanged(Invoice invoice) {
        reportInvoicesOrderedByNumber();
    }
}
