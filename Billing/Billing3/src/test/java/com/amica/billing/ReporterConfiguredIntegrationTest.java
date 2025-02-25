package com.amica.billing;

import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static com.amica.billing.BillingIntegrationTest.SOURCE_FOLDER;
import static com.amica.billing.TestUtility.*;
import static com.amica.billing.TestUtility.AS_OF_DATE;

public class ReporterConfiguredIntegrationTest extends ReporterIntegrationTest {
    @BeforeAll
    public static void setUpClass() {
        System.setProperty("server.env", "Quoted");
        ComponentConfigurationManager manager = ComponentConfigurationManager.getInstance();
        manager.initialize();
    }

    @Override
    protected String getOutputFolder() {
        return "alternate";
    }

    @BeforeEach
    @Override
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEMP_FOLDER));
        Files.copy(Paths.get(SOURCE_FOLDER, "customers_quoted.csv"),
                Paths.get(TEMP_FOLDER, "customers_quoted.csv"),
                StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(SOURCE_FOLDER, "invoices_quoted.csv"),
                Paths.get(TEMP_FOLDER, "invoices_quoted.csv"),
                StandardCopyOption.REPLACE_EXISTING);

        Files.createDirectories(Paths.get(getOutputFolder()));
        Stream.of(Reporter.FILENAME_INVOICES_BY_NUMBER,
                Reporter.FILENAME_INVOICES_BY_CUSTOMER,
                Reporter.FILENAME_OVERDUE_INVOICES,
                Reporter.FILENAME_CUSTOMERS_AND_VOLUME)
                .forEach(f -> new File(getOutputFolder(), f).delete());
        reporter = new Reporter();
        billing = reporter.getBilling();
    }
}
