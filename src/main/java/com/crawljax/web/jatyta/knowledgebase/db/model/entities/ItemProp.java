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
@JatytaAnnotation(keyName = "idItemProp")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "idPropName",
		"idItemType" }))
public class ItemProp implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long idItemProp;
	private String comments;
	private ItemType itemType;
	private PropName propName;

	/**
	 * @return the idItemProp
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idItemProp")
	public Long getIdItemProp() {
		return idItemProp;
	}

	/**
	 * @param idItemProp
	 *            the idItemProp to set
	 */
	public void setIdItemProp(Long idItemProp) {
		this.idItemProp = idItemProp;
	}

	@Override
	@Transient
	public Long getId() {
		return idItemProp;
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			this.idItemProp = id;
		}
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	/**
	 * 
	 * @param comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name = "idItemType", nullable = false)
	public ItemType getItemType() {
		return itemType;
	}

	/**
	 * 
	 * @param itemType
	 */
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
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
		if (idItemProp != null) {
			return idItemProp.toString();
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
		if (keyName != null && propName == null) {
			this.idItemProp = Long.decode(keyName);

		}
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[" + this.getId() + "," + this.getKeyName() + ","
				+ this.getItemType().getKeyName() + "," + ","
				+ this.getComments() + "]";
	}

}
