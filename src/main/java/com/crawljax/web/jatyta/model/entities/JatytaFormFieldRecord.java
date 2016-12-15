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
import com.crawljax.web.jatyta.plugins.util.dom.DOMUtils;

/**
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class JatytaFormFieldRecord implements Serializable,
		EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String formFieldXPath;
	private int idCrawlRecord;
	private byte[] image;
	private byte[] labelImage;

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
	 * Return the xpath of the web element.
	 * @return the formFieldXPath
	 */
	@Column(name = "formFieldXPath", nullable = false)
	public String getFormFieldXPath() {
		return formFieldXPath;
	}

	/**
	 * @param formFieldXPath
	 *            the formFieldXPath to set
	 */
	public void setFormFieldXPath(String formFieldXPath) {
		this.formFieldXPath = formFieldXPath;
	}

	/**
	 * @return the idCrawlRecord
	 */
	@Column(name = "idCrawlRecord", nullable = false)
	public int getIdCrawlRecord() {
		return idCrawlRecord;
	}

	/**
	 * @param idCrawlRecord
	 *            the idCrawlRecord to set
	 */
	public void setIdCrawlRecord(int idCrawlRecord) {
		this.idCrawlRecord = idCrawlRecord;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

	/**
	 * Return the web element image.
	 * @return the image
	 */
	@Column(name = "image", nullable = true)
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
	 * Return the label image of the web element.
	 * @return the labelImage
	 */
	@Column(name = "labelimage", nullable = true)
	public byte[] getLabelImage() {
		return labelImage;
	}

	/**
	 * @param labelImage the labelImage to set
	 */
	public void setLabelImage(byte[] labelImage) {
		this.labelImage = labelImage;
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
		if (formFieldXPath != null) {
			return formFieldXPath;
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
			setFormFieldXPath(keyName);
		}

	}

	/**
	 * Return de id value of the xpathExpression
	 * 
	 * @return The id value
	 */
	@Transient
	public String getIdFromXPathExpresion() {
		return DOMUtils.getIdFromXPathExpresion(formFieldXPath);

	}

}
