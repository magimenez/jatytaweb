/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util;

/**
 * Methods that support the schema.org behaviour
 * 
 * @author mgimenez
 *
 */
public class SchemaUtils {

	/**
	 * Return the URL of the schema associated to the itemtype name parameter.
	 * 
	 * @param itemtype
	 *            The itemtype value. Example: http://schema.org/Person
	 * @return The Schema URL. Example: http://schema.org
	 */
	public static String getSchemaName(String itemtype) {
		String itemname = getLastBitFromUrl(itemtype);
		return itemtype.substring(0, itemtype.indexOf(itemname));
	}

	/**
	 * Return the itemtype name from URL parameter.
	 * 
	 * @param itemtype
	 *            The itemtype value. Example: http://schema.org/Person
	 * @return The Schema URL. Example: http://schema.org
	 */
	public static String getItemTypeName(String url) {
		return getLastBitFromUrl(url);
	}

	/**
	 * @author mgimenez Return the last resource on the url parameter.
	 * 
	 * @param url
	 *            The URL parameter.
	 * @return The last resource on the URL.
	 */
	public static String getLastBitFromUrl(final String url) {
		return url.replaceFirst(".*/([^/?]+).*", "$1");
	}
}
