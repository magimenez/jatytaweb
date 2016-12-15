package com.crawljax.web.jatyta.knowledgebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

//import nl.flotsam.xeger.Xeger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author betopindu
 */
public abstract class KnowledgeBase implements KnowledgeBaseInterface {

	public static final String PROPERTY_SEPARATOR = "\u241F";
	public static final String VALUE_SEPARATOR =  "\u241E";

	private Properties configProperties;
	private Properties properties;
	// protected static KnowledgeBase instance;
	private static final Logger logger = LoggerFactory
			.getLogger(KnowledgeBase.class);
	protected static final String ALFANUMREX = "[\\u0020-\\u00FF]*";

	public KnowledgeBase(Properties configProperties) {
		this.configProperties = configProperties;
		loadPropertyFile();

	}

	public String getSourceType() {
		return configProperties.getProperty(KnowledgeBaseProperties.SOURCE_TYPE
				.getValue());
	}

	public String getSourceName() {
		return configProperties.getProperty(KnowledgeBaseProperties.SOURCE_NAME
				.getValue());
	}

	public String getValue(String key) {
		return properties.getProperty(key);
	}

	public List<String> getValueList(String key) {
		if (properties != null) {
			String property = properties.getProperty(key);
			if (property != null) {
				return Arrays
						.asList(property.split(VALUE_SEPARATOR));
			} else {
				logger.error("Propiedad no encontrada: '" + key + "'");
			}
		}

		return null;
	}

	public List<KnowledgeBaseItem> getValueList(
			KnowledgeBaseSearchCriteria searchCriteria) {

		List<KnowledgeBaseItem> list = new ArrayList<KnowledgeBaseItem>();
		String criterio = searchCriteria.build();
		logger.debug("Criterio de busqueda = "+criterio);
		if (properties != null) {
			String property = properties.getProperty(criterio);
			if (property != null) {
				List<String> values = Arrays.asList(property
						.split(VALUE_SEPARATOR.toString()));
				KnowledgeBaseItem item = new KnowledgeBaseItem(
						searchCriteria.getSchema(),
						searchCriteria.getItemType(),
						searchCriteria.getItemProp(), searchCriteria.getType(),
						searchCriteria.getValid(), values);
				list.add(item);

				return list;
			} else {
				// TODO ver si es necesario escapar el separador
				//criterio = criterio.replace(PROPERTY_SEPARATOR, "\\u241F");
				criterio = criterio.replace("null", ALFANUMREX);

				logger.debug("Criterio de busqueda = "+criterio);
				
				Iterator<Object> iterator = properties.keySet().iterator();
				while (iterator.hasNext()) {
					String keyElement = (String) iterator.next();
					logger.debug("Elemento de busqueda = "+keyElement);
					if (keyElement.matches(criterio)) {
						property = properties.getProperty((String) keyElement);
						String[] keyNameArray = keyElement.split(
								PROPERTY_SEPARATOR.toString(), 5);
						KnowledgeBaseItem item = new KnowledgeBaseItem(
								keyNameArray[0], keyNameArray[1],
								keyNameArray[2], keyNameArray[3],
								Boolean.parseBoolean(keyNameArray[4]),
								Arrays.asList(property.split(VALUE_SEPARATOR
										.toString())));
						list.add(item);
					}
				}
			}
		}

		return list;
	}

	public void setValueList(KnowledgeBaseItem item) {
		String key = item.getSchema() + PROPERTY_SEPARATOR + item.getId()
				+ PROPERTY_SEPARATOR + item.getName() + PROPERTY_SEPARATOR
				+ item.getType() + PROPERTY_SEPARATOR + item.getValid();

		String values = join(item.getValues(), VALUE_SEPARATOR.toString());

		properties.setProperty(key, values);

	}

	private String join(List<String> values, String delim) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = values.iterator();
		if (iter.hasNext())
			sb.append(iter.next());
		while (iter.hasNext()) {
			sb.append(delim);
			sb.append(iter.next());
		}
		return sb.toString();
	}

	public String getTextFromRegex(String regex) {
		if (regex == null) {
			regex = ALFANUMREX; // Por defecto Alfanumerico de al menos una
								// letra o digito
		}

		// Xeger by Wilfred Springer [wilfredspringer@gmail.com]
		/*
		 * Xeger generator = new Xeger(regex); String result =
		 * generator.generate(); if (!result.matches(regex)) {
		 * logger.warn("El texto: '" + result +
		 * "' generado no matchea con la expresion regular"); }
		 * 
		 * return result;
		 */
		return null;
	}

	/**
	 * @return the configProperties
	 */
	public Properties getConfigProperties() {
		return configProperties;
	}

	/**
	 * @param configProperties
	 *            the configProperties to set
	 */
	public void setConfigProperties(Properties configProperties) {
		this.configProperties = configProperties;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}