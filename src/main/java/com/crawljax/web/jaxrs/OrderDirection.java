/**
 * 
 */
package com.crawljax.web.jaxrs;

/**
 * @author mgimenez
 *
 */
public enum OrderDirection {
	
	/**
	 * The asceding order.
	 */
	ASC("ASC"),
	/**
	 * The descending attribute.
	 */
	DESC("DESC");

	private final String value;

	private OrderDirection(final String value) {
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
