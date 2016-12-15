/**
 * 
 */
package com.crawljax.web.jatyta.comparator;

import java.io.IOException;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.oraclecomparator.AbstractComparator;
import com.crawljax.util.DomUtils;
import com.crawljax.util.XPathHelper;

/**
 * Comparator with title name as criteria for state equivalence.
 * 
 * @author mgimenez
 * @since 28/09/2016
 *
 */
public class JatytaStateComparator extends AbstractComparator {

	private static final Logger LOG = LoggerFactory.getLogger(JatytaStateComparator.class);

	private String titleXpathExpression;

	/**
	 * Constructor with the XpathExpression to find the title name for the DOM.
	 * 
	 * @param titleXpathExpression
	 */
	public JatytaStateComparator(String titleXpathExpression) {
		this.titleXpathExpression = titleXpathExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.oraclecomparator.Comparator#isEquivalent(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean isEquivalent(String oldDom, String newDom) {

		String titleOldDom = getDOMTitleName(normalize(oldDom));
		String titleNewDom = getDOMTitleName(normalize(newDom));

		LOG.debug("old DOM title = " + titleOldDom);
		LOG.debug("new DOM title = " + titleNewDom);

		if (titleNewDom != null && titleOldDom != null && titleNewDom.equals(titleOldDom)) {
			LOG.debug("DOM are equals");
			return true;
		}
		LOG.debug("DOM are different");
		return false;
	}

	/**
	 * Obtain the title name from de DOM parameter.
	 * 
	 * @param dom
	 *            The DOM parameter.
	 * @return The Title name or null.
	 */
	private  String getDOMTitleName(String dom) {
		LOG.debug("Title xpath = " + this.titleXpathExpression);
		LOG.debug("DOM character length = " + dom.length());
		Document doc = null;
		String titleName = "";
		try {
			doc = DomUtils.asDocument(dom);

			NodeList nodeListStr = XPathHelper.evaluateXpathExpression(dom, this.titleXpathExpression);
			LOG.debug("NodeListStr size = " + nodeListStr.getLength());

			NodeList nodeList = XPathHelper.evaluateXpathExpression(doc, this.titleXpathExpression);
			LOG.debug("NodeList size = " + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				String textTitle = node.getTextContent();
				LOG.debug("textTitle = " + textTitle);
				if (textTitle != null && !textTitle.trim().isEmpty()) {
					titleName = titleName + " " + textTitle.trim();
				}
			}
		} catch (XPathExpressionException | DOMException | IOException e) {
			LOG.error("Exception with stripping XPath expression: " + titleXpathExpression, e);
		}
		if (titleName.isEmpty()) {
			return null;
		} else {
			return titleName;
		}

	}

	@Override
	public String normalize(String string) {
		String strippedStr;

		// remove linebreaks
		strippedStr = string.replaceAll("[\\t\\n\\x0B\\f\\r]", "");

		// remove just before and after elements spaces
		strippedStr = strippedStr.replaceAll(">[ ]*", ">");
		strippedStr = strippedStr.replaceAll("[ ]*<", "<");

		return strippedStr;
	}

	/**
	 * Check if the stripped DOM of the browser is equivalent with empty.
	 * @param browser
	 * @return
	 */
	public boolean isEquivalent(EmbeddedBrowser browser) {

		String titleOldDom = "";
		String titleNewDom = getDOMTitleName(browser);

		LOG.debug("old DOM title = " + titleOldDom);
		LOG.debug("new DOM title = " + titleNewDom);

		if (titleNewDom != null && titleOldDom != null && titleNewDom.equals(titleOldDom)) {
			LOG.debug("DOM are equals");
			return true;
		}
		LOG.debug("DOM are different");
		return false;
	}

	/**
	 * Obtain the title name from the actual DOM of the browser.
	 * 
	 * @param dom
	 *            The DOM parameter.
	 * @return The Title name or null.
	 */
	private String getDOMTitleName(EmbeddedBrowser browser) {
		String result = "";
		Identification body = new Identification(How.xpath, "//body");
		if (titleXpathExpression != null && browser.elementExists(body)) {

			WebElement root = browser.getWebElement(body);

			Identification identification = new Identification(How.xpath, titleXpathExpression);

			List<WebElement> titleTagList = root.findElements(By.xpath(titleXpathExpression));

			for (WebElement titleElement : titleTagList) {
				if (browser.elementExists(identification)) {

					String textTitle = titleElement.getText();
					if (textTitle != null && !textTitle.trim().isEmpty()) {
						result = result + " " + textTitle.trim();
					}
				}
			}
		}

		if (result.isEmpty()) {
			result = null;
		}

		return result;

	}
	
	/**
	 * Obtains the state name from the DOM parameter, with the xpath expression.
	 * @param dom The Dom paramater
	 * @param titleXpathExpression The xpath expression to get the page name.
	 * @return The page name, null if the xpath dont finded.
	 */
	public static String getDOMTitleName(Document dom, String titleXpathExpression){
		String titleName = "";
		try {
			NodeList nodeList = XPathHelper.evaluateXpathExpression(dom,
					titleXpathExpression);
			LOG.debug("NodeList size = " + nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				String textTitle = node.getTextContent();
				LOG.debug("textTitle = " + textTitle);
				if (textTitle != null && !textTitle.trim().isEmpty()) {
					titleName = titleName + " " + textTitle.trim();
				}
			}
		} catch (XPathExpressionException | DOMException e) {
			LOG.error("Exception with stripping XPath expression: " + 
					titleXpathExpression, e);
		}
		if (titleName.isEmpty()) {
			return null;
		} else {
			return titleName;
		}
	}
	
	
	public static String getDOMTitleName(EmbeddedBrowser browser, String titleXpathExpression) {
		String result = "";
		Identification body = new Identification(How.xpath, "//body");
		if (titleXpathExpression != null && !titleXpathExpression.isEmpty() && browser.elementExists(body)) {

			WebElement root = browser.getWebElement(body);

			Identification identification = new Identification(How.xpath, titleXpathExpression);

			List<WebElement> titleTagList = root.findElements(By.xpath(titleXpathExpression));

			for (WebElement titleElement : titleTagList) {
				if (browser.elementExists(identification)) {

					String textTitle = titleElement.getText();
					if (textTitle != null && !textTitle.trim().isEmpty()) {
						result = result + " " + textTitle.trim();
					}
				}
			}
		}

		if (result.isEmpty()) {
			result = null;
		}

		return result;

	}

}
