/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.dom;

/**
 * @author mgimenez
 * 
 *         The HTML Element attributes supported by Jatyta.
 */
public enum HtmlElementAttribute {

	/**
	 * The id attribute.
	 */
	ID("id"),
	/**
	 * The name attribute.
	 */
	NAME("name"),
	/**
	 * The class attribute.
	 */
	CLASS("class"),
	/**
	 * The type attribute.
	 */
	TYPE("type");

	private final String value;

	private HtmlElementAttribute(final String value) {
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
