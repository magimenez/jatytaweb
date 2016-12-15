/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.validation;

import org.w3c.dom.Node;

import com.crawljax.core.state.StateVertex;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;

/**
 * The record of the validation messages detected.
 * 
 * @author mgimenez
 * 
 */
public class ValidationRecord {

	/**
	 * The status of a success validation.
	 */
	public static final String STATUS_SUCCESS = "SUCCESS";
	/**
	 * The status of a failed validation.
	 */
	public static final String STATUS_FAIL = "FAILED";
	/**
	 * The status of a unknown behaviour.
	 */
	public static final String STATUS_UNKNOWN = "UNKNOWN";

	/**
	 * ID of the {@link StateVertex} when ocurred the validation.
	 */
	private Integer validationState;

	/**
	 * Indicates if the validation fails or success. STATUS_UNKNOWN by default
	 */
	private String validationStatus = STATUS_UNKNOWN;

	/**
	 * The {@link HtmlElement} of the validation element (Message)
	 */
	private HtmlElement validationHtmlElement;
	/**
	 * The {@link Node} of the validation element (Message)
	 */
	private Node validationNode;
	/**
	 * The Xpath of the validation element.
	 */
	private String validationElementXpath;

	/**
	 * The target {@link Node} of validation.
	 */
	private Node targetNode;

	/**
	 * The target {@link HtmlElement} of validation.
	 */
	private HtmlElement targetHtmlElement;
	/**
	 * The Xpath of the target element.
	 */
	private String targetElementXpath;

	/**
	 * ID of the target {@link StateVertex} of validation.
	 */
	private Integer targetState;

	/**
	 * Value of the target {@link Node} of validation.
	 */
	private String targetValue;
	/**
	 * The screenshot for the validation element.
	 */
	private byte[] image;
	/**
	 * The order of the value in the state.
	 */
	private Integer valueOrder;
	/**
	 * The Value type of target value.
	 */
	private FormValueType valueType = FormValueType.UNKNOWN;

	/**
	 * The constructor when validation message ocurred.
	 * 
	 * @param validationHtmlElement
	 *            The {@link HtmlElement} of the validation message.
	 * @param validationNode
	 *            The {@link Node} of the validation message.
	 * @param validationState
	 *            The {@link StateVertex} asociated.
	 */
	public ValidationRecord(HtmlElement validationHtmlElement,
			Node validationNode, Integer validationState) {
		super();
		this.validationHtmlElement = validationHtmlElement;
		this.validationNode = validationNode;
		this.validationState = validationState;
	}

	/**
	 * Default constructor when validation message not found.
	 */
	public ValidationRecord() {
		super();
	}

	/**
	 * @return the validationHtmlElement
	 */
	public HtmlElement getValidationHtmlElement() {
		return validationHtmlElement;
	}

	/**
	 * @param validationHtmlElement
	 *            the validationHtmlElement to set
	 */
	public void setValidationHtmlElement(HtmlElement validationHtmlElement) {
		this.validationHtmlElement = validationHtmlElement;
	}

	/**
	 * @return the validationNode
	 */
	public Node getValidationNode() {
		return validationNode;
	}

	/**
	 * @param validationNode
	 *            the validationNode to set
	 */
	public void setValidationNode(Node validationNode) {
		this.validationNode = validationNode;
	}

	/**
	 * @return the targetNode
	 */
	public Node getTargetNode() {
		return targetNode;
	}

	/**
	 * @param targetNode
	 *            the targetNode to set
	 */
	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	/**
	 * @return the targetHtmlElement
	 */
	public HtmlElement getTargetHtmlElement() {
		return targetHtmlElement;
	}

	/**
	 * @param targetHtmlElement
	 *            the targetHtmlElement to set
	 */
	public void setTargetHtmlElement(HtmlElement targetHtmlElement) {
		this.targetHtmlElement = targetHtmlElement;
	}

	/**
	 * @return the crawlState
	 */
	public Integer getCrawlState() {
		return validationState;
	}

	/**
	 * @param crawlState
	 *            the crawlState to set
	 */
	public void setCrawlState(Integer crawlState) {
		this.validationState = crawlState;
	}

	/**
	 * @return the targetState
	 */
	public Integer getTargetState() {
		return targetState;
	}

	/**
	 * @param targetState
	 *            the targetState to set
	 */
	public void setTargetState(Integer targetState) {
		this.targetState = targetState;
	}

	/**
	 * @return the validationState
	 */
	public Integer getValidationState() {
		return validationState;
	}

	/**
	 * @param validationState
	 *            the validationState to set
	 */
	public void setValidationState(Integer validationState) {
		this.validationState = validationState;
	}

	/**
	 * @return the validationStatus
	 */
	public String getValidationStatus() {
		return validationStatus;
	}

	/**
	 * Set the status validation to FAILED.
	 */
	public void setStatusFailed() {
		this.validationStatus = STATUS_FAIL;
	}

	/**
	 * Set the status validation to SUCCESS.
	 */
	public void setStatusSuccess() {
		this.validationStatus = STATUS_SUCCESS;
	}

	/**
	 * @return the validationElementXpath
	 */
	public String getValidationElementXpath() {
		return validationElementXpath;
	}

	/**
	 * @param validationElementXpath
	 *            the validationElementXpath to set
	 */
	public void setValidationElementXpath(String validationElementXpath) {
		this.validationElementXpath = validationElementXpath;
	}

	/**
	 * @return the targetElementXpath
	 */
	public String getTargetElementXpath() {
		return targetElementXpath;
	}

	/**
	 * @param targetElementXpath
	 *            the targetElementXpath to set
	 */
	public void setTargetElementXpath(String targetElementXpath) {
		this.targetElementXpath = targetElementXpath;
	}

	/**
	 * @return the targetValue
	 */
	public String getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetValue
	 *            the targetValue to set
	 */
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the valueOrder
	 */
	public Integer getValueOrder() {
		return valueOrder;
	}

	/**
	 * @param valueOrder the valueOrder to set
	 */
	public void setValueOrder(Integer valueOrder) {
		this.valueOrder = valueOrder;
	}
	
	

	/**
	 * @return the valueType
	 */
	public FormValueType getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(FormValueType valueType) {
		this.valueType = valueType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "[ Validation State : " + getValidationState()
				+ ", Validation Status: " + getValidationStatus()
				+ ", Validation Xpath: " + getValidationElementXpath()
				+ ", Target State: " + getTargetState() + ", Target Xpath: "
				+ getTargetElementXpath() + ", Target Value: "
				+ getTargetValue() +", Value Order:"+getValueOrder()
				+ ", Value Type: "+ getValueType().getValue()+" ]";
	}
}
