/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.configuration.JatytaConfiguration;
import com.crawljax.web.jatyta.knowledgebase.db.PropertyDataBase;
import com.crawljax.web.jatyta.knowledgebase.file.PropertyFile;

/**
 * @author mgimenez
 * 
 */
public class KnowledgeBaseFactory {
	private static KnowledgeBase instance;
	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeBase.class);
	public static final String CONFIGFILE = "configuration.properties";
	public static final String RESOURCES_PATH = "out";

	public static KnowledgeBase getInstance() {

		if (instance == null) {
			try {
				Properties configProperties = JatytaConfiguration.loadConfiguration();
				String sourceType = configProperties
						.getProperty(KnowledgeBaseProperties.SOURCE_TYPE
								.getValue());
				if (configProperties != null) {
					if (KnowledgeBaseSourceType.DATABASE.getValue().equals(sourceType)) {
						instance = new PropertyDataBase(configProperties);
					} else {
						// La clase PropertyFile puede procesar tanto
						// KnowledgeBaseSourceType.FILE y KnowledgeBaseSourceType.XML
						instance = new PropertyFile(configProperties);
					}
				}
			} catch (FileNotFoundException fnfE) {
				logger.error("Archivo de configuracion no encontrado: '"
						+ CONFIGFILE + "'");
			} catch (IOException ioE) {
				logger.error("No se pudo procesar el archivo de configuracion "
						+ ioE.getMessage());
			}

		}
		return instance;
	}

}
