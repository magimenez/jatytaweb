/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.jsf;

import java.util.StringTokenizer;

/**
 * Utils for Java Server Faces framework.
 * 
 * @author mgimenez
 * 
 */
public class JSFUtils {

	public static final String JSF_ELEMENT_DELIMITER = ":";

	/**
	 * Remove the autogenerated id or name with the parent data. Example :
	 * 'j_id123:user' to 'user'.
	 * 
	 * @param value
	 *            the value to remove the parent data.
	 * @return the String without the parent data.
	 */
	public static String removeAutoGeneratedParentIdOrName(String value) {
		if(value!=null){
			StringTokenizer stk = new StringTokenizer(value, JSF_ELEMENT_DELIMITER);
			String result = "";
			while (stk.hasMoreTokens()) {
				result =  stk.nextToken();
			}
			return result;
		}else{
			return "";
		}
	}
}
