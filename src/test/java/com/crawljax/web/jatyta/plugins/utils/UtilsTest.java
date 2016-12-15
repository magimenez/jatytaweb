/**
 * 
 */
package com.crawljax.web.jatyta.plugins.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.plugins.util.SchemaUtils;

/**
 * Class for Test the Utils support.
 * 
 * @author mgimenez
 *
 */
public class UtilsTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void schemaUtilsTest() {
		String url = "http://schema.org/Person";
		logger.info("Test URL : "+url);
		logger.info("Schema URL : "+SchemaUtils.getSchemaName(url));
		logger.info("ItemType : "+SchemaUtils.getItemTypeName(url));
		assertTrue("http://schema.org/".equals(SchemaUtils.getSchemaName(url)));
		assertTrue("Person".equals(SchemaUtils.getItemTypeName(url)));
	}

}
