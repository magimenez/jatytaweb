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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class JatytaFormValueRecord implements Serializable,
		EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String formFieldXPath;
	private String type;
	private String value;
	private Integer stateAsociated;
	private int idCrawlRecord;
	private int valueOrder;
	private JatytaFormFieldRecord formFieldRecord;


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
	 * @return the type
	 */
	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	@Column(name = "value", nullable = false)
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the stateAsociated
	 */
	@Column(name = "stateAsociated", nullable = false)
	public Integer getStateAsociated() {
		return stateAsociated;
	}

	/**
	 * @param stateAsociated
	 *            the stateAsociated to set
	 */
	public void setStateAsociated(Integer stateAsociated) {
		this.stateAsociated = stateAsociated;
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
	 * @return the valueOrder
	 */
	@Column(name = "value_order", nullable = false)
	public int getValueOrder() {
		return valueOrder;
	}

	/**
	 * @param valueOrder the valueOrder to set
	 */
	public void setValueOrder(int valueOrder) {
		this.valueOrder = valueOrder;
	}

	/**
	 * @return the formFieldRecord
	 */
	@ManyToOne
	@JoinColumn(name = "idformfieldrecord", nullable = true)
	public JatytaFormFieldRecord getFormFieldRecord() {
		return formFieldRecord;
	}

	/**
	 * @param formFieldRecord the formFieldRecord to set
	 */
	public void setFormFieldRecord(JatytaFormFieldRecord formFieldRecord) {
		this.formFieldRecord = formFieldRecord;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * Return de id value of the xpathExpression
	 * 
	 * @return The id value
	 */
	@Transient
	public String getIdFromXPathExpresion() {
		return DOMUtils.getIdFromXPathExpresion(formFieldXPath);

	}

}
