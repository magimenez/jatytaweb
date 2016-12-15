/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase.db.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Beto
 *
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface JatytaAnnotation {
	String keyName();
}