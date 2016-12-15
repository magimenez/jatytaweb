/**
 * 
 */
package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementAttribute;

/**
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class JatytaFormConfiguration implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	/**
	 * Configuration id from Crawljax Configuration
	 */
	private String configurationId;
	
	/**
	 * Form submit element, by default is input.
	 */
	private String formSubmitElement = HtmlElement.INPUT.getValue().toLowerCase();
	/**
	 * The form submit element attribute for identification, by default type.
	 */
	private String formSubmitAttribute = HtmlElementAttribute.TYPE.getValue();
	/**
	 * The form submit element attribute value, by default submit.
	 */
	private String formSubmitAttributeValue = "submit";
	/**
	 * Form submit element text, by default is NULL.
	 */
	private String formSubmitText = null;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the configurationId
	 */
	@Column(name = "configurationId", nullable = false, unique = true)
	public String getConfigurationId() {
		return configurationId;
	}

	/**
	 * @param configurationId
	 *            the configurationId to set
	 */
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	
	/**
	 * @return the formSubmitElement
	 */
	@Column(name = "formSubmitElement", nullable = false)
	public String getFormSubmitElement() {
		return formSubmitElement;
	}

	/**
	 * @param formSubmitElement
	 *            the formSubmitElement to set
	 */
	public void setFormSubmitElement(String formSubmitElement) {
		this.formSubmitElement = formSubmitElement;
	}

	/**
	 * @return the formSubmitAttribute
	 */
	@Column(name = "formSubmitAttribute", nullable = true)
	public String getFormSubmitAttribute() {
		return formSubmitAttribute;
	}

	/**
	 * @param formSubmitAttribute
	 *            the formSubmitAttribute to set
	 */
	public void setFormSubmitAttribute(String formSubmitAttribute) {
		this.formSubmitAttribute = formSubmitAttribute;
	}

	/**
	 * @return the formSubmitAttributeValue
	 */
	@Column(name = "formSubmitAttributeValue", nullable = true)
	public String getFormSubmitAttributeValue() {
		return formSubmitAttributeValue;
	}

	/**
	 * @param formSubmitAttributeValue
	 *            the formSubmitAttributeValue to set
	 */
	public void setFormSubmitAttributeValue(String formSubmitAttributeValue) {
		this.formSubmitAttributeValue = formSubmitAttributeValue;
	}

	/**
	 * @return the formSubmitText
	 */
	@Column(name = "formSubmitText", nullable = true)
	public String getFormSubmitText() {
		return formSubmitText;
	}

	/**
	 * @param formSubmitText
	 *            the formSubmitText to set
	 */
	public void setFormSubmitText(String formSubmitText) {
		this.formSubmitText = formSubmitText;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#
	 * getKeyName ()
	 */
	@Override
	@Transient
	public String getKeyName() {

		return "" + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#
	 * setKeyName (java.lang.String)
	 */
	@Override
	public void setKeyName(String keyName) {
		//do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " id = " + getId() + ", configurationId = " 
				+ getConfigurationId() + " formSubmitElement: " 
				+ getFormSubmitElement()+", formSubmitAttribute: "
				+ getFormSubmitAttribute() + ", formSubmitAttributeValue: " 
				+ getFormSubmitAttributeValue() +", formSubmitText: "
				+ getFormSubmitText();
	}
}
