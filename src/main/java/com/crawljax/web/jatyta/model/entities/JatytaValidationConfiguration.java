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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;

/**
 * Class that represents the configuration to find validation elements in the
 * DOM.
 * 
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "htmlElement",
		"attributeName", "configurationId", "attributeValue" }))
public class JatytaValidationConfiguration implements Serializable,
		EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String htmlElement;
	private String attributeName;
	private String attributeValue;
	private String configurationId;
	private String targetAttributeName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#getId
	 * ()
	 */
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {

		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#setId
	 * (java.io.Serializable)
	 */
	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#getKeyName
	 * ()
	 */
	@Override
	@Transient
	public String getKeyName() {
		if (id != null) {
			return getId().toString();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#setKeyName
	 * (java.lang.String)
	 */
	@Override
	public void setKeyName(String keyName) {
		if (keyName != null && id == null) {
			setId(Long.decode(keyName));
		}

	}

	/**
	 * Obtain the htmlElement value
	 * 
	 * @return the htmlElement
	 */
	@Column(name = "htmlElement", nullable = false)
	public String getHtmlElement() {
		return htmlElement;
	}

	/**
	 * Set the htmlElement value
	 * 
	 * @param htmlElement
	 *            the htmlElement to set
	 */
	public void setHtmlElement(String htmlElement) {
		this.htmlElement = htmlElement;
	}

	/**
	 * Obtain the attributeName value.
	 * 
	 * @return the attributeName
	 */
	@Column(name = "attributeName", nullable = false)
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the attributeName value
	 * 
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Obtain the attributeValue
	 * 
	 * @return the attributeValue
	 */
	@Column(name = "attributeValue", nullable = false)
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Set the attributeValue
	 * 
	 * @param attributeValue
	 *            the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * Obtain the Crawljax Configuration id associated.
	 * 
	 * @return the configurationId
	 */
	@Column(name = "configurationId", nullable = false)
	public String getConfigurationId() {
		return configurationId;
	}

	/**
	 * Set the Crawljax Configuration id associated.
	 * 
	 * @param configurationId
	 *            the configurationId to set
	 */
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	/**
	 * @return the targetAttributeName
	 */
	@Column(name = "targetAttributeName", nullable = true)
	public String getTargetAttributeName() {
		return targetAttributeName;
	}

	/**
	 * @param targetAttributeName the targetAttributeName to set
	 */
	public void setTargetAttributeName(String targetAttributeName) {
		this.targetAttributeName = targetAttributeName;
	}
	
	

}
