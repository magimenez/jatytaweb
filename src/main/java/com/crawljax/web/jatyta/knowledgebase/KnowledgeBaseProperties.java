/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase;

/**
 * @author mgimenez
 * 
 */
public enum KnowledgeBaseProperties {
	/**
	 * The property {@link KnowledgeBaseSourceType} in the configuration file.
	 */
	SOURCE_TYPE("sourceType"),
	/**
	 * The property soruce name in th configuration file.
	 */
	SOURCE_NAME("sourceName");

	private final String value;

	private KnowledgeBaseProperties(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
