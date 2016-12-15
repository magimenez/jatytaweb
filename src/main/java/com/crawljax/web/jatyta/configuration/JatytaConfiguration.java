package com.crawljax.web.jatyta.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class to obtain the configuration properties.
 */
public class JatytaConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JatytaConfiguration.class);

	public static final String CONFIGFILE = "configuration.properties";
	public static final String RESOURCES_PATH = "out";
	public static final String CHROME_DRIVER = "webdriver.chrome.driver";
	public static final String IEXPLORER_DRIVER = "webdriver.ie.driver";

	/**
	 * Return the properties of the configuration file.
	 * 
	 * @return The {@link Properties} for JatytaConfiguration.
	 * @throws IOException
	 *             If the file have errors.
	 * @throws FileNotFoundException
	 *             If the file its not found.
	 */
	public static Properties loadConfiguration() throws IOException, FileNotFoundException {
		Properties configProperties = null;
		String absolutePath = System.getProperty("user.dir") + File.separator + RESOURCES_PATH + File.separator
				+ CONFIGFILE;

		logger.info("Leyendo archivo de configuracion...");
		FileInputStream fileInput = new FileInputStream(absolutePath);
		if (fileInput != null) {
			configProperties = new Properties();
			configProperties.load(fileInput);
			fileInput.close();
		}
		return configProperties;
	}
}