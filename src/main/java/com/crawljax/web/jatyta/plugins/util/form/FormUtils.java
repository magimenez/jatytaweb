/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.form;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.state.StateVertex;
import com.crawljax.web.jatyta.plugins.util.dom.DOMUtils;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementAttribute;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementInputType;

/**
 * @author mgimenez
 * 
 */
public class FormUtils {

	private static final Logger LOG = LoggerFactory.getLogger(FormUtils.class);

	/**
	 * The {@link List} of {@link HtmlElementInputType} what support the plugin.
	 */
	private static final HtmlElementInputType[] attributeInputTypeValues = { HtmlElementInputType.TEXT,
			HtmlElementInputType.PASSWORD, HtmlElementInputType.RADIO, HtmlElementInputType.CHECKBOX };

	/**
	 * The {@link List} of {@link HtmlElement} thats support the plugin.
	 */
	private static final HtmlElement[] formHtmlElements = { HtmlElement.INPUT, HtmlElement.SELECT,
			HtmlElement.TEXTAREA };

	/**
	 * Return a {@link List} of {@link FormFieldCandidate} obtained from the
	 * {@link StateVertex} parameter.
	 * 
	 * @param stateVertex
	 *            The {@link StateVertex} DOM to search form fields.
	 * @return A {@link List} of {@link FormFieldCandidate}.
	 */
	public static List<FormFieldCandidate> obtainFormFieldCandidates(StateVertex stateVertex) {
		List<FormFieldCandidate> result = new ArrayList<>();
		
		List<Node> nodeList = null;

		for (HtmlElement htmlElem : formHtmlElements) {
			if (HtmlElement.INPUT.equals(htmlElem)) {
				for (HtmlElementInputType htmlInputType : attributeInputTypeValues) {
					Map<String, String> inputAttributesValues = new HashMap<String, String>();
					inputAttributesValues.put(HtmlElementAttribute.TYPE.getValue(), htmlInputType.getValue());
					nodeList = DOMUtils.getNodesByTagAndAttributes(stateVertex, HtmlElement.INPUT,
							inputAttributesValues);
					for (Node node : nodeList) {
						LOG.debug(htmlElem.getValue() + " of type " + htmlInputType.getValue() + " found : "
								+ DOMUtils.getNodeAttributesToString(node));

						result.add(new FormFieldCandidate(node, htmlElem, htmlInputType));

					}
				}
			} else {
				nodeList = DOMUtils.getNodesByTagAndAttributes(stateVertex, htmlElem, new HashMap<String, String>());
				for (Node node : nodeList) {
					LOG.debug(htmlElem.getValue() + " found : " + DOMUtils.getNodeAttributesToString(node));
					// no se envia el type, debido a que son select o
					// textArea.
					result.add(new FormFieldCandidate(node, htmlElem, null));

				}
			}
		}

		return result;
	}

	/**
	 * Obtain a screenshot of {@link WebElement} parameter.
	 * 
	 * @param element
	 *            The {@link WebElement} paramter.
	 * @param browser
	 *            The {@link EmbeddedBrowser} to take the screenshot.
	 * @return A byte[] of the element screenshot.
	 * @throws IOException
	 *             If the browser cannot take the screenshot.
	 * @throws RasterFormatException If element position is invalid. 
	 */
	public static byte[] getElementScreenShot(WebElement element, 
			EmbeddedBrowser browser) throws IOException, RasterFormatException {

		byte[] screenshot = browser.getScreenShot();
		// Create instance of BufferedImage from captured screenshot
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(screenshot));

		// Get the Height and Width of WebElement
		int height = element.getSize().height;
		int width = element.getSize().width;

		// Create Rectangle using Height and Width to get size
		Rectangle rect = new Rectangle(width, height);

		// Get location of WebElement in a Point
		Point p = element.getLocation();
		// This will provide X & Y co-ordinates of the WebElement

		// Create an image for WebElement using its size and location
		BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
		// This will give image data specific to the WebElement
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(dest, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

}
