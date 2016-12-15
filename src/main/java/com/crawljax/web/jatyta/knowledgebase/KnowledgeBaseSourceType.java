/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase;

/**
 * The jatyta knowledge base source type supported.
 * 
 * @author mgimenez
 * 
 */
public enum KnowledgeBaseSourceType {

	/**
	 * The properties file source type.
	 */
	FILE("file"),
	/**
	 * The XML source type.
	 */
	XML("xml"),
	/**
	 * The database source type
	 */
	DATABASE("db");

	private final String value;

	private KnowledgeBaseSourceType(final String value) {
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
