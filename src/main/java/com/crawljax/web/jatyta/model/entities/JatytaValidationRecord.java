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

import com.crawljax.core.state.StateVertex;
import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;

/**
 * Class that represents the records in the validation analysis.
 * 
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "idCrawlRecord",
		"validationElementXpath", "validationState", "targetState",
		"targetElementXpath", "value_order" }))
public class JatytaValidationRecord implements Serializable,
		EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 * Indicates if the validation fails or success.
	 */
	private String validationStatus;

	/**
	 * ID of the {@link StateVertex} when ocurred the validation.
	 */
	private Integer validationState;

	/**
	 * The Xpath of the validation element.
	 */
	private String validationElementXpath;

	/**
	 * ID of the target {@link StateVertex} of validation.
	 */
	private Integer targetState;

	/**
	 * The Xpath of the target element.
	 */
	private String targetElementXpath;
	
	/**
	 * The Value of the target element.
	 */
	private String targetValue;
	
	/**
	 * The crawljax id record.
	 */
	private int idCrawlRecord;
	/**
	 * The image of the validation element.
	 */
	private byte[] image;
	/**
	 * The order for the validation value.
	 */
	private int valueOrder;
	/**
	 * The FormValueType of the target Value.
	 */
	private String valueType;
	
	/**
	 * @return the id
	 */
	@Override
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
	 * @return the validationStatus
	 */
	@Column(name = "validationStatus", nullable = false)
	public String getValidationStatus() {
		return validationStatus;
	}

	/**
	 * @param validationStatus
	 *            the validationStatus to set
	 */
	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	/**
	 * @return the validationState
	 */
	@Column(name = "validationState", nullable = false)
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
	 * @return the validationElementXpath
	 */
	@Column(name = "validationElementXpath", nullable = true)
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
	 * @return the targetState
	 */
	@Column(name = "targetState", nullable = true)
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
	 * @return the targetElementXpath
	 */
	@Column(name = "targetElementXpath", nullable = true)
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
	@Column(name = "targetValue", nullable = true)
	public String getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
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
	 * @return the valueType
	 */
	@Column(name = "value_type", nullable = false)
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
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

}
