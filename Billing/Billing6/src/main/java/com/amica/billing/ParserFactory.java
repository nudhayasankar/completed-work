package com.amica.billing;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.StreamSupport;

import com.amica.billing.parse.CSVParser;
import com.amica.billing.parse.FlatParser;
import com.amica.billing.parse.Parser;
import com.amica.esa.componentconfiguration.manager.ComponentConfigurationManager;
import com.amica.escm.configuration.api.Configuration;

import lombok.extern.java.Log;

/**
 * A factory for parsers that determines which type of parser to create
 * based on the extension of given filenames.
 * 
 * @author Will Provost
 */
 @Log
public class ParserFactory {

	public static final String CONFIGURAIONT_NAME = "Billing";
	public static final String BASE_PROPERTY_NAME = 
			ParserFactory.class.getSimpleName();

	private static Map<String,Supplier<Parser>> parsers = new HashMap<>();
	static {
		resetParsers();
	}
	
	/**
	 * Helper function to use the Java Reflection API to create a class
	 * of the requested name, and to handle any exceptions.
	 */
	public static Parser createParserWithClassName(String className) {
		try {
			return (Parser) Class.forName(className)
					.getConstructor(new Class<?>[0])
					.newInstance();
		} catch (Exception ex) {
			log.log(Level.WARNING, 
					"Couldn't create parser factory as configured", ex);
		}
		
		return null;
	}
	
	/**
	 * Get our configuration from the configuration manager.
	 * Look for the base property name and configure the default parser if found.
	 * Loop through any keys of the form base.ext, and configure a parser
	 * for each extension.
	 */
	private static void addConfiguredParsers() {
		
		Configuration configuration = ComponentConfigurationManager.getInstance()
				.getConfiguration(CONFIGURAIONT_NAME);

		// Look for a default parser
		if (configuration.containsKey(BASE_PROPERTY_NAME)) {
			String cls = configuration.getString(BASE_PROPERTY_NAME);
			parsers.put(null, () -> createParserWithClassName(cls));
			log.info(String.format("Configured default parser %s", cls));
		}
		
		// Look for extension-specific parsers
		Iterable<String> keys = () -> configuration.getKeys();
		StreamSupport.stream(keys.spliterator(), false)
			.filter(key -> key.matches(BASE_PROPERTY_NAME + "\\..+"))
			.forEach(key -> {
				String ext = key.substring(BASE_PROPERTY_NAME.length() + 1);
				parsers.put(ext, () -> createParserWithClassName
						(configuration.getString(key)));
				log.info(String.format("Configured parser %s=%s",
						ext, configuration.getString(key)));
			});
	}
	
	public static void resetParsers() {
		parsers.put("csv", CSVParser::new);
		parsers.put("flat", FlatParser::new);
		parsers.put(null, CSVParser::new);

		if (System.getProperty("server.env") != null) {
			addConfiguredParsers();
		}
	}

	public static void addParser(String extension, Supplier<Parser> factory) {
		if (!parsers.containsKey(extension)) {
			parsers.put(extension.toLowerCase(), factory);
		} else {
			throw new IllegalArgumentException
				("There is already a parser for extension " + extension + 
					"; use replaceParser() to replace it.");
		}
	}
	
	public static void replaceParser(String extension, Supplier<Parser> factory) {
		if (parsers.containsKey(extension)) {
			parsers.put(extension.toLowerCase(), factory);
		} else {
			throw new IllegalArgumentException
				("There is no parser for extension " + extension + 
					"; use addParser() to add one.");
		}
	}
	
	public static void replaceDefaultParser(Supplier<Parser> factory) {
		parsers.put(null, factory);
	}
	
	/**
	 * Looks up the file extension to find a 
	 * <code>Supplier&lt;Parser&gt;</code>, invokes it, and returns the result. 
	 */
	public static Parser createParser(String filename) {
		if (filename != null) {
			int index = filename.indexOf(".");
			if (index  != -1 && index != filename.length() - 1) {
				String extension = filename.substring(index + 1).toLowerCase();
				if (parsers.containsKey(extension)) {
					return parsers.get(extension).get();
				}
			}
		}
		return parsers.get(null).get();
	}
}
