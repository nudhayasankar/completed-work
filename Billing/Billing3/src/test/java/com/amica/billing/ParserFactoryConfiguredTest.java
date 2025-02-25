package com.amica.billing;

import com.amica.billing.parse.FlatParser;
import com.amica.billing.parse.QuotedCSVParser;
import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static com.amica.billing.ParserFactory.createParser;

public class ParserFactoryConfiguredTest {

    @BeforeAll
    public static void setupClass() {
        System.setProperty("server.env", "Quoted");
        ComponentConfigurationManager manager = ComponentConfigurationManager.getInstance();
        manager.initialize();
    }

    @Test
    public void testDefaultParser() {
        assertThat(createParser(null)).isInstanceOf(FlatParser.class);
    }

    @Test
    public void testQuotedParser() {
        assertThat(createParser("any.csv")).isInstanceOf(QuotedCSVParser.class);
    }
}
