package com.crawljax.web;

import java.io.File;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.configuration.JatytaConfiguration;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		Properties configProperties = JatytaConfiguration.loadConfiguration();
		String driver = configProperties
						.getProperty(JatytaConfiguration.CHROME_DRIVER);
		System.setProperty(JatytaConfiguration.CHROME_DRIVER,driver);
		driver = configProperties
						.getProperty(JatytaConfiguration.IEXPLORER_DRIVER);
		System.setProperty(JatytaConfiguration.IEXPLORER_DRIVER,driver);

		final ParameterInterpeter options = new ParameterInterpeter(args);

		String outFolder = options.specifiesOutputDir() ? options.getSpecifiedOutputDir() : "out";

		int port = options.specifiesPort() ? options.getSpecifiedPort() : 8080;

		final CrawljaxServer server = new CrawljaxServer(
				new CrawljaxServerConfigurationBuilder().setPort(port).setOutputDir(new File(outFolder)));

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				LOG.info("Shutdown hook initiated");
				try {
					server.stop();
				} catch (Exception e) {
					LOG.warn("Could not stop the server in properly {}", e.getMessage());
					LOG.debug("Stop error was ", e);
				}
			}
		});

		server.start(true);
	}

	public static String getCrawljaxVersion() {
		try {
			String[] lines = Resources.toString(Main.class.getResource("/crawljax.version"), Charsets.UTF_8)
					.split(System.getProperty("line.separator"));
			for (String line : lines) {
				String[] keyValue = line.split("=");
				if (keyValue[0].trim().toLowerCase().equals("version")) {
					return keyValue[1].trim();
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
