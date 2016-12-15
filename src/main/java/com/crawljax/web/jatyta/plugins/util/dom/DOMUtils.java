/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.dom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.core.state.StateVertex;

/**
 * @author mgimenez
 * 
 */
public class DOMUtils {
	private static final Logger LOG = LoggerFactory.getLogger(DOMUtils.class);

	/**
	 * Return the xpath expression for the node and tag parameter.
	 * 
	 * @param node
	 *            The {@link Node} parameter.
	 * @param tag
	 *            The {@link HtmlElement} parameter.
	 * @return The string with the xpath format //[@id='id-value'] or
	 *         //[@name='name-value'] or all attributes.
	 */
	public static String generateXPathExpression(Node node, HtmlElement tag) {
		String xpathExpression = "//" + tag.getValue() + "[";
		String and = "";
		Node nodeAttr = node.getAttributes().getNamedItem(
				HtmlElementAttribute.ID.getValue());

		if (nodeAttr != null && nodeAttr.getNodeValue() != null
				&& !nodeAttr.getNodeValue().isEmpty()) {
			xpathExpression = addAttributeToXPathExpression(nodeAttr,
					xpathExpression, and);
		} else {
			// probar name
			nodeAttr = node.getAttributes().getNamedItem(
					HtmlElementAttribute.NAME.getValue());
			if (nodeAttr != null && nodeAttr.getNodeValue() != null
					&& !nodeAttr.getNodeValue().isEmpty()) {
				xpathExpression = addAttributeToXPathExpression(nodeAttr,
						xpathExpression, and);
			} else {
				// agregar todos los atributos.
				for (int i = 0; i < node.getAttributes().getLength(); i++) {
					// se obtiene uno a uno los atributos
					nodeAttr = node.getAttributes().item(i);
					if (nodeAttr != null && nodeAttr.getNodeValue() != null
							&& !nodeAttr.getNodeValue().isEmpty()) {
						xpathExpression = addAttributeToXPathExpression(
								nodeAttr, xpathExpression, and);
						and = " and ";
					}
				}
			}
		}
		xpathExpression = xpathExpression + "]";
		return xpathExpression;
	}
	
	/**
	 * Return the xpath expression for the node and tag parameter.
	 * 
	 * @param node
	 *            The {@link Node} parameter.
	 * @return The string with the xpath format //[@id='id-value'] or
	 *         //[@name='name-value'] or all attributes.
	 */
	public static String generateXPathExpression(Node node) {
		String xpathExpression = "//" + node.getNodeName() + "[";
		String and = "";
		Node nodeAttr = node.getAttributes().getNamedItem(
				HtmlElementAttribute.ID.getValue());

		if (nodeAttr != null && nodeAttr.getNodeValue() != null
				&& !nodeAttr.getNodeValue().isEmpty()) {
			xpathExpression = addAttributeToXPathExpression(nodeAttr,
					xpathExpression, and);
		} else {
			// probar name
			nodeAttr = node.getAttributes().getNamedItem(
					HtmlElementAttribute.NAME.getValue());
			if (nodeAttr != null && nodeAttr.getNodeValue() != null
					&& !nodeAttr.getNodeValue().isEmpty()) {
				xpathExpression = addAttributeToXPathExpression(nodeAttr,
						xpathExpression, and);
			} else {
				// agregar todos los atributos.
				for (int i = 0; i < node.getAttributes().getLength(); i++) {
					// se obtiene uno a uno los atributos
					nodeAttr = node.getAttributes().item(i);
					if (nodeAttr != null && nodeAttr.getNodeValue() != null
							&& !nodeAttr.getNodeValue().isEmpty()) {
						xpathExpression = addAttributeToXPathExpression(
								nodeAttr, xpathExpression, and);
						and = " and ";
					}
				}
			}
		}
		xpathExpression = xpathExpression + "]";
		return xpathExpression;
	}
	
	/**
	 * Return the xpath expression for the {@link WebElement} parameter.
	 * 
	 * @param element
	 *            The {@link Node} parameter.
	 * @return The string with the xpath format //tag[@id='id-value'] or
	 *         //tag[@name='name-value'] or all attributes.
	 */
	public static String generateXPathExpression(WebElement element) {
		String xpathExpression = "//" + element.getTagName() + "[";
		String and = "";
		String idAttr = element.getAttribute(HtmlElementAttribute.ID.getValue());

		if (idAttr!=null && !idAttr.isEmpty()) {
			xpathExpression = addAttributeToXPathExpression(HtmlElementAttribute.ID.getValue(), idAttr,
					xpathExpression, and);
		} else {
			// probar name
			String nameAttr =  element.getAttribute(HtmlElementAttribute.NAME.getValue());
			if (nameAttr != null && !nameAttr.isEmpty()) {
				xpathExpression = addAttributeToXPathExpression(HtmlElementAttribute.NAME.getValue(), nameAttr,
						xpathExpression, and);
			}
		}
		xpathExpression = xpathExpression + "]";
		return xpathExpression;
	}


	/**
	 * 
	 * @param nodeAttr
	 * @param xpathExpression
	 * @param and
	 * @return
	 */
	private static String addAttributeToXPathExpression(Node nodeAttr,
			String xpathExpression, String and) {

		// si no esta vacio el atributo
		String value = nodeAttr.getNodeValue();
		xpathExpression = xpathExpression + and + "@" + nodeAttr.getNodeName()
				+ "='" + value + "'";

		return xpathExpression;
	}
	
	private static String addAttributeToXPathExpression(String attribute, 
			String value, String xpathExpression, String and) {

		// si no esta vacio el atributo
		xpathExpression = xpathExpression + and + "@" + attribute
				+ "='" + value + "'";

		return xpathExpression;
	}

	/**
	 * 
	 * @param xpathExpression
	 * @return
	 */
	public static String getIdFromXPathExpresion(String xpathExpression) {
		String result = "";
		int index = xpathExpression.indexOf("@id='");
		if (index != -1) {
			int indexValue = index + 5;
			result = xpathExpression.substring(indexValue);
			int finalindex = result.indexOf("'");
			result = result.substring(0, finalindex);
			return result;
		} else {
			return "";
		}

	}

	/**
	 * Returns in a String the attributes and values of the node.
	 * 
	 * @param node
	 *            The {@link Node}
	 * @return The String of attributes and values.
	 */
	public static String getNodeAttributesToString(Node node) {
		String atributos = "";
		String coma = "";
		for (int j = 0; j < node.getAttributes().getLength(); j++) {
			Node atributo = node.getAttributes().item(j);
			atributos = atributos + coma + atributo.toString();
			coma = " ; ";
		}
		return atributos;
	}

	/**
	 * Return de {@link List} of {@link Node} from a state, tag, attributes and
	 * values, comparission method: equals.
	 * 
	 * @param state
	 *            The state where get de nodes
	 * @param tag
	 *            the {@link HtmlElement} tag of the nodes.
	 * @param attributesAndValues
	 *            The {@link Map} of atributes and values of nodes.
	 * @return The {@link List} of {@link Node} obtained from the parameters.
	 */
	public static List<Node> getNodesByTagAndAttributes(StateVertex state,
			HtmlElement tag, Map<String, String> attributesAndValues) {
		List<Node> result = new ArrayList<Node>();

		NodeList nodeList = null;
		try {
			nodeList = state.getDocument().getElementsByTagName(tag.getValue());
		} catch (IOException e) {
			LOG.error("ERROR AL OBTENER LOS " + tag.getValue() + "'s ", e);

		}

		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				boolean flag = true;
				if (node.hasAttributes() && !attributesAndValues.isEmpty()) {

					NamedNodeMap nodeAttributes = node.getAttributes();

					Set<String> keySet = attributesAndValues.keySet();
					for (String attr : keySet) {
						String value = attributesAndValues.get(attr);
						Node nodeAttr = nodeAttributes.getNamedItem(attr);
						if (!(nodeAttr != null && nodeAttr.getNodeValue()
								.equals(value))) {
							flag = false;
							break;
						}

					}

				} else {
					flag = false;
				}

				if (flag) {
					result.add(node);
				}
			}
		}

		return result;
	}

	/**
	 * Return de {@link List} of {@link Node} from a state, tag, attributes and
	 * values, comparission method: contains.
	 * 
	 * @param state
	 *            The state where get de nodes
	 * @param tag
	 *            the {@link HtmlElement} tag of the nodes.
	 * @param attributesAndValues
	 *            The {@link Map} of atributes and values of nodes.
	 * @return The {@link List} of {@link Node} obtained from the parameters.
	 */
	public static List<Node> getNodesByTagAndAttributesLike(StateVertex state,
			HtmlElement tag, Map<String, String> attributesAndValues) {
		List<Node> result = new ArrayList<Node>();

		NodeList nodeList = null;
		try {
			nodeList = state.getDocument().getElementsByTagName(tag.getValue());
		} catch (IOException e) {
			LOG.error("ERROR AL OBTENER LOS " + tag.getValue() + "'s ", e);

		}

		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				boolean flag = true;
				if (node.hasAttributes() && !attributesAndValues.isEmpty()) {

					NamedNodeMap nodeAttributes = node.getAttributes();

					Set<String> keySet = attributesAndValues.keySet();
					for (String attr : keySet) {
						String value = attributesAndValues.get(attr);
						Node nodeAttr = nodeAttributes.getNamedItem(attr);
						if (!(nodeAttr != null && nodeAttr.getNodeValue()
								.contains(value))) {
							flag = false;
							break;
						}

					}

				} else {
					flag = false;
				}

				if (flag) {
					result.add(node);
				}
			}
		}

		return result;
	}
	
	/**
	 * Return the concatenation of Text of the element under the Xpath
	 * parameter.
	 * 
	 * @param xpathExpression
	 *            The Xpath expression to locate elements to concatenate the
	 *            text inside.
	 * @param context
	 *            The {@link CrawlerContext} to obtain the Browser Driver.
	 * @return The concatenation of the text inside de elements under the xpath
	 *         expression parameter.
	 */
	public static String getTextUnderXPathExpresion(EmbeddedBrowser browser,
			String xpathExpression) {

		String result = "";
		Identification body = new Identification(How.xpath, "//body");
		if( xpathExpression!=null && !xpathExpression.isEmpty() && browser.elementExists(body)) {

			WebElement root = browser.getWebElement(body);

			Identification identification = new Identification(How.xpath,
					xpathExpression);

			List<WebElement> titleTagList = root.findElements(By
					.xpath(xpathExpression));

			for (WebElement titleElement : titleTagList) {
				if (browser.elementExists(identification)) {

					String textTitle = titleElement.getText();
					if(textTitle!=null && !textTitle.trim().isEmpty()){
						result = result + " " + textTitle.trim();
					}

				}
			}	
		}else{
			return null;
		}
		

		if (result.isEmpty()) {
			result = "N/A";
		}

		return result;

	}

}
