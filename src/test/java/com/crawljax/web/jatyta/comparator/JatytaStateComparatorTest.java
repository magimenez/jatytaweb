/**
 * 
 */
package com.crawljax.web.jatyta.comparator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mgimenez
 *
 */
public class JatytaStateComparatorTest {

	Logger logger = LoggerFactory.getLogger(getClass());

	String xpathTitle = "//div[@id='page']//h1" + " | //div[@id='titulo'] |"
			+ " //div[@class='rich-mpnl-text rich-mpnl-header ']";

	String listaDepartamentos = "Lista Departamentos";

	String listaLocalidades = "Lista Localidades";

	String header = "&lt;!DOCTYPE html PUBLIC &quot;-//W3C//DTD XHTML 1.0 Transitional//EN&quot; &quot;http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd&quot;&gt;&lt;html xmlns=&quot;http://www.w3.org/1999/xhtml&quot;&gt;&lt;head&gt;"
			+"	&lt;/head&gt;&lt;body class=&quot;body&quot;&gt;";
	
	String div1 = "&lt;div id=&quot;contenttitulo&quot;&gt;" + "&lt;div id=&quot;titulo&quot;&gt;"
			+ "&lt;h3 style=&quot;border-bottom: 2px solid #acc6ed;&quot;&gt;" + "&lt;table&gt;" + "&lt;tbody&gt;"
			+ "&lt;tr&gt;" + "&lt;td&gt;"
			+ "&lt;img class=&quot;imgtitulo&quot; src=&quot;/gfv/images/contents.png&quot; /&gt;" + "&lt;/td&gt;"
			+ "&lt;td&gt;";
	
	String div2 = "&lt;/td&gt;" + "&lt;/tr&gt;" + "&lt;/tbody&gt;" + "&lt;/table&gt;"
			+ "&lt;/h3&gt;" + "&lt;/div&gt;" + "&lt;/div&gt;";
	
	String footer = "&lt;/body&gt;&lt;/html&gt;";
	
	String dom1 = header +div1+ listaDepartamentos + div2+footer; 

	String dom2 = header +div1+ listaLocalidades + div2+footer;
	

	@Test
	public void comparatorTest() {
		JatytaStateComparator comp = new JatytaStateComparator(xpathTitle);
		/*
		assertTrue("isEquivalent", comp.isEquivalent(dom1, dom1));
		
		assertTrue("isEquivalent", comp.isEquivalent(dom2, dom2));
		
		assertFalse("not Equivalent", comp.isEquivalent(dom1, dom2));*/
	
	}

}
