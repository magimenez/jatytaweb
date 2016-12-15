/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util;

/**
 * @author mgimenez The values of the MicroData global attributes.
 */
public enum MicroDataNodeAttribute {
	/**
	 * The itemscope attribute.
	 */
	ITEMSCOPE("itemscope"),
	/**
	 * The itemtype attribute.
	 */
	ITEMTYPE("itemtype"),
	/**
	 * The itemid attribute.
	 */
	ITEMID("itemid"),
	/**
	 * The itemprop attribute.
	 */
	ITEMPROP("itemprop"),
	/**
	 * The itemref attribute.
	 */
	ITEMREF("itemref");

	private final String value;

	private MicroDataNodeAttribute(final String value) {
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
