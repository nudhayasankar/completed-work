package com.amica.billing;

import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;
import com.amica.escm.configuration.properties.PropertiesConfiguration;
import com.amica.test.ConfigMgrDiagnostics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.Properties;

import static com.amica.billing.TestUtility.TEMP_FOLDER;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BillingConfiguredTest extends BillingTest{

    @Override
    protected String getCustomersFilename() {
        return "customers_configured.csv";
    }

    @Override
    protected String getInvoicesFilename() {
        return "invoices_configured.csv";
    }

    @Override
    protected Billing createTestTarget() {
        ConfigMgrDiagnostics.diagnose("Billing", "component.name");
        return new Billing();
    }

    @BeforeAll
    public void setUpClass() {
        System.setProperty("server.env", "Configured");
        ComponentConfigurationManager.getInstance().initialize();
    }
}
