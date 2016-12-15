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
 * Class that represents the schema that classified the {@link ItemType}s
 * 
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "schemaName")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Schema implements Serializable, EntityInterface<Long> {

	public static final String ALL_SCHEMAS_NAME = "ALL";

	private static final long serialVersionUID = 1L;
	private Long idSchema;
	protected String schemaName;
	protected boolean isSchemaOrg;
	protected String comments;

	/**
	 * @return the idSchema
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idSchema")
	public Long getIdSchema() {
		return idSchema;
	}

	/**
	 * @param idSchema
	 *            the idSchema to set
	 */
	public void setIdSchema(Long idSchema) {
		this.idSchema = idSchema;
	}

	/**
	 * @return the schemaName
	 */
	@Column(unique = true, name = "schemaName")
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		// TODO no debe permitir el nombre ALL enla interfaz ni base de datos
		this.schemaName = schemaName;
	}

	/**
	 * @return the isSchemaOrg
	 */
	@Column(name = "isSchemaOrg")
	public boolean getIsSchemaOrg() {
		return isSchemaOrg;
	}

	/**
	 * @param isSchemaOrg
	 *            the isSchemaOrg to set
	 */
	public void setIsSchemaOrg(boolean isSchemaOrg) {
		this.isSchemaOrg = isSchemaOrg;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	@Transient
	public Long getId() {
		return idSchema;
	}

	@Override
	public void setId(Long id) {
		if (id != null) {
			this.idSchema = id;
		}

	}

	@Override
	@Transient
	public String getKeyName() {
		return schemaName;
	}

	@Override
	public void setKeyName(String keyName) {
		if (keyName != null && schemaName == null) {
			this.schemaName = keyName;
		}
	}

	@Override
	public String toString() {
		return "[" + this.getId() + "," + this.getKeyName() + ","
				+ this.getIsSchemaOrg() + "," + this.getComments() + "]";
	}
}
