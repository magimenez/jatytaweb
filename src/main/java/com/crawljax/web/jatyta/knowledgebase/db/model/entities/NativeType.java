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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.model.entities.EntityInterface;

/**
 * @author Beto
 * 
 */

@JatytaAnnotation(keyName = "typeName")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class NativeType implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;
	private Long idNativeType;
	protected String typeName;

	// private Set<ItemProp> itemprops;

	/**
	 * @return the idNativeType
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idNativeType")
	public Long getIdNativeType() {
		return idNativeType;
	}

	/**
	 * @param idNativeType
	 *            the idNativeType to set
	 */
	public void setIdNativeType(Long idNativeType) {
		this.idNativeType = idNativeType;
	}

	/**
	 * @return the typeName
	 */
	@Column(unique = true, name = "typeName")
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	@Transient
	public Long getId() {
		return idNativeType;
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			this.idNativeType = id;
		}
	}

	@Override
	@Transient
	public String getKeyName() {
		return typeName;
	}

	@Override
	public void setKeyName(String keyName) {
		if (keyName != null && typeName == null) {
			this.typeName = keyName;
		}
	}

	@Override
	public String toString() {
		return "[" + this.getId() + "," + this.getKeyName() + "]";
	}

	/**
	 * 
	 * @return
	 */
	/*
	 * @OneToMany(mappedBy = "nativeType", fetch=FetchType.EAGER) public
	 * Set<ItemProp> getItemprops() { return itemprops; }
	 */

	/**
	 * 
	 * @param itemprops
	 */
	/*
	 * public void setItemprops(Set<ItemProp> itemprops) { this.itemprops =
	 * itemprops; }
	 */

}
