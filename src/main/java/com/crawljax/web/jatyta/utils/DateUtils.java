/**
 * 
 */
package com.crawljax.web.jatyta.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Support Class for Date functions
 * 
 * @author mgimenez
 * 
 */
public class DateUtils {

	/**
	 * Returns the date with the format yyyy/MM/dd HH:mm:ss. If the parameter is
	 * null, return a empty string.
	 * 
	 * @author mgimenez
	 * @param date
	 *            The date to format.
	 * @return The String of the date with the format yyyy/MM/dd HH:mm:ss
	 */
	public static String dateFormatted(Date date) {
		String result = "";
		if (date != null) {
			result = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
		}
		return result;
	}
}
