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
@JatytaAnnotation(keyName = "typeName")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"typeName", "idSchema"}))
public class ItemType implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long idItemType;
	protected String typeName;
	protected String comments;
	private Schema schema;
	// private Set<ItemProp> itemprops;

	/**
	 * @return the idItemType
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idItemType")
	public Long getIdItemType() {
		return idItemType;
	}

	/**
	 * @param idItemType
	 *            the idItemType to set
	 */
	public void setIdItemType(Long idItemType) {
		this.idItemType = idItemType;
	}

	/**
	 * @return the typeName
	 */
	@Column( name = "typeName")
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

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the itemprops
	 */
	/*
	 * @OneToMany(mappedBy = "itemType", fetch=FetchType.LAZY) public
	 * Set<ItemProp> getItemprops() { return itemprops; }
	 */

	/**
	 * @param itemprops
	 *            the itemprops to set
	 */
	/*
	 * public void setItemprops(Set<ItemProp> itemprops) { this.itemprops =
	 * itemprops; }
	 */

	/**
	 * @return the schema
	 */
	@ManyToOne
	@JoinColumn(name = "idSchema")
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(Schema schema) {
		this.schema = schema;
		
	}

	@Override
	@Transient
	public Long getId() {
		return idItemType;
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			this.idItemType = id;
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
		return "[" + this.getId() + "," + this.getKeyName() + "," + ","
				+ this.getComments() + "]";
	}

}
