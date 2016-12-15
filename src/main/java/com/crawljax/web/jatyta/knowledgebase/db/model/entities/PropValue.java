/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase.db.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.model.entities.EntityInterface;

/**
 * @author Beto
 * 
 */
@JatytaAnnotation(keyName = "value")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "value",
		"idPropName" }))
public class PropValue implements Serializable, EntityInterface<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idPropValues;
	protected String value;
	protected boolean isValid;
	private PropName propName;

	/**
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idPropValues")
	public Long getIdPropValues() {
		return idPropValues;
	}

	/**
	 * 
	 * @param idPropValues
	 */
	public void setIdPropValues(Long idPropValues) {
		this.idPropValues = idPropValues;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "value")
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	@Transient
	public Long getId() {
		return idPropValues;
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			this.idPropValues = id;
		}
	}

	@Override
	@Transient
	public String getKeyName() {
		return value;
	}

	@Override
	public void setKeyName(String keyName) {
		if (keyName != null && value == null) {
			this.value = keyName;
		}
	}

	@Column(name = "valid")
	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return the propName
	 */
	@ManyToOne
	@JoinColumn(name = "idPropName", nullable = false)
	public PropName getPropName() {
		return propName;
	}

	/**
	 * @param propName
	 *            the propName to set
	 */
	public void setPropName(PropName propName) {
		this.propName = propName;
	}

	@Override
	public String toString() {
		return "[" + this.getId() + "," + this.getPropName().getKeyName() + ","
				+ this.getIsValid() + "," + this.getKeyName() + "]";
	}
}
