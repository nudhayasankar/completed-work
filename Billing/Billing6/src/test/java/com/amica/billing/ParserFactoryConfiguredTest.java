package com.amica.billing;


import static com.amica.billing.ParserFactory.createParser;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.amica.billing.parse.FlatParser;
import com.amica.billing.parse.QuotedCSVParser;
import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;

/**
 * Test of the {@link ParserFactory} component in a 
 * configuration-manager environment.
 *
 * @author Will Provost
 */
public class ParserFactoryConfiguredTest {

	@BeforeAll
	public static void setUpBeforeAll() {
		System.setProperty("server.env", "Quoted");
		ComponentConfigurationManager.getInstance().initialize();
		ParserFactory.resetParsers();
	}
	
	@Test
	public void testDefaultParser() {
		assertThat(createParser("any.xxx")).isInstanceOf(FlatParser.class);
	}
	
	@Test
	public void testCSVParser() {
		assertThat(createParser("any.csv")).isInstanceOf(QuotedCSVParser.class);
	}
}

