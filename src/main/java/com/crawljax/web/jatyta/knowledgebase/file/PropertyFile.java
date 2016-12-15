package com.crawljax.web.jatyta.knowledgebase.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.knowledgebase.KnowledgeBase;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseFactory;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseSourceType;

/**
 * 
 * @author betopindu
 */
public class PropertyFile extends KnowledgeBase {
	
	public PropertyFile(Properties configProperties) {
		super(configProperties);
	}

	protected static final Logger logger = LoggerFactory
			.getLogger(PropertyFile.class);

	public void loadPropertyFile() {
		try {
			FileInputStream fileInput = getFileInputStream();
			if (fileInput != null) {
				setProperties(new Properties());
				if (getSourceType().compareToIgnoreCase(
						KnowledgeBaseSourceType.XML.getValue()) == 0) {
					getProperties().loadFromXML(fileInput);
				} else {
					getProperties().load(fileInput);
				}

				fileInput.close();
			}
		} catch (IOException e) {
			logger.error("No se pudo procesar el archivo de propiedades "
					+ e.getMessage());
		}
	}

	private FileInputStream getFileInputStream() {

		String absolutePath = System.getProperty("user.dir") + File.separator
				+ KnowledgeBaseFactory.RESOURCES_PATH + File.separator
				+ getSourceName();

		try {
			logger.info("Leyendo archivo de propiedades...");
			return new FileInputStream(absolutePath);
		} catch (FileNotFoundException fnfEx) {
			logger.error("Archivo de propiedades no encontrado: '"
					+ absolutePath + "'");
		}

		return null;
	}

	public void storePropertyFile() {
		try {
			FileOutputStream fileOutput = getFileOutputStream();
			if (fileOutput != null) {
				getProperties().store(fileOutput, null);
				if (getSourceType().compareToIgnoreCase("xml") == 0) {
					getProperties().storeToXML(fileOutput, null);
				} else {
					getProperties().store(fileOutput, null);
				}

				fileOutput.close();

			}
		} catch (IOException e) {
			logger.error("No se pudo guardar el archivo de propiedades "
					+ e.getMessage());
		}
	}

	private FileOutputStream getFileOutputStream() {

		String absolutePath = System.getProperty("user.dir") + File.separator
				+ KnowledgeBaseFactory.RESOURCES_PATH + File.separator
				+ getSourceName();

		try {
			logger.info("Abriendo archivo de propiedades...");
			return new FileOutputStream(absolutePath);
		} catch (FileNotFoundException fnfEx) {
			logger.error("Archivo de propiedades no encontrado: '"
					+ absolutePath + "'");
		}

		return null;
	}

}
