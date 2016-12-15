/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util;

/**
 * @author mgimenez
 * 
 *         The available values of {@link JatytaDOMNodeAttribute} TYPE.
 */
@Deprecated
public enum JatytaDOMNodeAttributeType {

	/**
	 * The TEXT behavior.
	 */
	TEXT("text"),
	/**
	 * The NUMBER behavior.
	 */
	NUMBER("number"),
	/**
	 * The BOOLEAN behavior.
	 */
	BOOLEAN("boolean"),
	/**
	 * The DATE behavior.
	 */
	DATE("date"),
	/**
	 * The DATETIME behavior.
	 */
	DATETIME("datetime"),
	/**
	 * The TIME behavior.
	 */
	TIME("time"),
	/**
	 * The EMAIL behavior.
	 */
	EMAIL("email");

	private final String value;

	private JatytaDOMNodeAttributeType(final String value) {
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
