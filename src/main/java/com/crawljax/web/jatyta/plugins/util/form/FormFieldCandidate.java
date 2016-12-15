/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.form;

import org.w3c.dom.Node;

import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementInputType;

/**
 * Class to asociate the DOM node and html tags supported by jatyta
 * 
 * @author mgimenez
 * 
 */
public class FormFieldCandidate {
	private Node node;
	private HtmlElement htmlElement;
	private HtmlElementInputType htmlElementInputType;

	/**
	 * The default constructor
	 * 
	 * @param node
	 *            The {@link Node} attribute. NOT NULL
	 * @param htmlElement
	 *            The {@link HtmlElement} attribute. NOT NULL
	 * @param htmlElementInputType
	 *            The {@link HtmlElementInputType} attribute.
	 */
	public FormFieldCandidate(Node node, HtmlElement htmlElement,
			HtmlElementInputType htmlElementInputType) {
		super();
		this.node = node;
		this.htmlElement = htmlElement;
		this.htmlElementInputType = htmlElementInputType;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * @return the htmlElement
	 */
	public HtmlElement getHtmlElement() {
		return htmlElement;
	}

	/**
	 * @return the htmlElementInputType
	 */
	public HtmlElementInputType getHtmlElementInputType() {
		return htmlElementInputType;
	}

}
