/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.form;

/**
 * Defines the type of form values, VALID, INVALID, UNKNOWN and BOTH, that 
 * represents VALID & INVALID.
 * 
 * @author mgimenez
 * 
 */
public enum FormValueType {
	/**
	 * The valid value type.
	 */
	VALID("VALID"),
	/**
	 * The invalid value type.
	 */
	INVALID("INVALID"),
	/**
	 * The both types, valid and invalid.
	 */
	BOTH("BOTH"),
	/**
	 * The unknown value type.
	 */
	UNKNOWN("UNKNOWN"),
	/**
	 * The empty value type.
	 */
	EMPTY("EMPTY"),
	/**
	 * The overflow value type.
	 */
	OVERFLOW("OVERFLOW");

	private final String value;

	private FormValueType(final String value) {
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
