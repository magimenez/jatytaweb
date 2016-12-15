/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.dom;


/**
 * @author mgimenez
 * 
 *         The HTML Input Types supported by Jatyta.
 */
public enum HtmlElementInputType {
	/**
	 * The text input type.
	 */
	TEXT("text"),
	/**
	 * The password input type.
	 */
	PASSWORD("password"),
	/**
	 * The checkbox input type.
	 */
	CHECKBOX("checkbox"),
	/**
	 * The radio input type.
	 */
	RADIO("radio"); 

	private final String value;

	private HtmlElementInputType(final String value) {
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
