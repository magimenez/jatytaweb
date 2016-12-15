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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.model.entities.EntityInterface;

/**
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "propName")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class PropName implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long idPropName;
	private String name;
	private String comments;
	private NativeType nativeType;

	/**
	 * @return the idPropName
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idPropName")
	public Long getIdPropName() {
		return idPropName;
	}

	/**
	 * @param idPropName
	 *            the idPropName to set
	 */
	public void setIdPropName(Long idPropName) {
		this.idPropName = idPropName;
	}

	/**
	 * @return the name
	 */
	@Column(name = "name", unique = true, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the comments
	 */
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the nativeType
	 */
	@ManyToOne
	@JoinColumn(name = "idNativeType", nullable = false)
	public NativeType getNativeType() {
		return nativeType;
	}

	/**
	 * @param nativeType
	 *            the nativeType to set
	 */
	public void setNativeType(NativeType nativeType) {
		this.nativeType = nativeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#getId
	 * ()
	 */
	@Override
	@Transient
	public Long getId() {

		return idPropName;
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
		if (id != null) {
			this.idPropName = id;
		}

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
		return name;
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
		if (keyName != null && name == null) {
			this.name = keyName;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	@Transient
	public String toString() {

		return "[ idPropName = " + idPropName + ", name = " + name
				+ ", comments = " + comments + " ]";
	}

}
