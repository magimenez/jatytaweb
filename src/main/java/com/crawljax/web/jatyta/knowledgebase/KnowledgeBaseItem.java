/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase;

import java.util.List;

/**
 * @author mgimenez
 * 
 *         Class to represent the knowledgeBase item and attributes.
 */
public class KnowledgeBaseItem {

	/**
	 * The schema name
	 */
	private String schema;
	/**
	 * The id atribute
	 */
	private String id;
	/**
	 * The name atribute
	 */
	private String name;
	/**
	 * The type attribute
	 */
	private String type;
	/**
	 * The valid attribute
	 */
	private Boolean valid;
	/**
	 * The list of values.
	 */
	private List<String> values;

	/**
	 * The constructor with attributes values.
	 * 
	 * @param schema
	 *            The value of the schema name.
	 * @param id
	 *            The value of the id attribute.
	 * @param name
	 *            The value of the name attribute.
	 * @param type
	 *            The value of the type attribute.
	 * @param valid
	 *            The value of the valid attribute.
	 * @param values
	 *            The list of values attribute.
	 */
	public KnowledgeBaseItem(String schema, String id, String name,
			String type, Boolean valid, List<String> values) {
		super();
		this.schema = schema;
		this.id = id;
		this.name = name;
		this.type = type;
		this.valid = valid;
		this.values = values;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The getter of name.
	 * 
	 * @return The name attribute.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The setter of name.
	 * 
	 * @param name
	 *            The new value of name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The getter of type.
	 * 
	 * @return The type value.
	 */
	public String getType() {
		return type;
	}

	/**
	 * The setter of type.
	 * 
	 * @param type
	 *            The new value of type.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The getter of valid.
	 * 
	 * @return The value of valid.
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * The setter of valid.
	 * 
	 * @param valid
	 *            The new value of valid.
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	/**
	 * The getter of values.
	 * 
	 * @return The list of values.
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * The setter of values.
	 * 
	 * @param values
	 *            The new list of values.
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

}
