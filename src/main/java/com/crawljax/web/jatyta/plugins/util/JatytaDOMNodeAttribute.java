/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util;

import com.crawljax.web.jatyta.plugins.AutomationFormInputValuesPlugin;

/**
 * @author mgimenez
 * 
 *         The DOM node attribute to set the behavior of
 *         {@link AutomationFormInputValuesPlugin}
 */
@Deprecated
public enum JatytaDOMNodeAttribute {

	/**
	 * The "data-jatyta-type" attribute to set the text, number, boolean, date
	 * and email behavior.
	 */
	TYPE("data-jatyta-type"),
	/**
	 * The "data-jatyta-custom-type" attribute to set the custom behavior
	 * defined in the Knowledge Base.
	 */
	CUSTOM_TYPE("data-jatyta-custom-type"),
	/**
	 * The "data-jatyta-min" attribute to set the minimum size of input data
	 * behavior.
	 */
	MIN("data-jatyta-min"),

	/**
	 * The "data-jatyta-max" attribute to set the maximum size of input data
	 * behavior.
	 */
	MAX("data-jatyta-max"),

	/**
	 * The "data-jatyta-value" attribute to set default value of input data
	 * behavior.
	 */
	VALUE("data-jatyta-value"),
	/**
	 * The "data-jatyta-multiple-values" attribute to set default multiples
	 * values of input data behavior.
	 */
	MULTIPLE_VALUES("data-jatyta-multiple-values");

	private final String value;

	private JatytaDOMNodeAttribute(final String value) {
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
