/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase;

/**
 * @author mgimenez
 * 
 */
public class KnowledgeBaseSearchCriteria {

	/**
	 * The schema name
	 */
	private String schema;

	/**
	 * The id atribute, represents the itemtype name
	 */
	private String itemType;
	/**
	 * The name atribute, represents the itemprop name
	 */
	private String itemProp;
	/**
	 * The type attribute, represents the nativeType.
	 */
	private String type;
	/**
	 * The valid attribute
	 */
	private Boolean valid;

	/**
	 * 
	 * @return the value of schema.
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * 
	 * @param schema
	 *            the schema name
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType
	 *            the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the itemProp
	 */
	public String getItemProp() {
		return itemProp;
	}

	/**
	 * @param itemProp
	 *            the itemProp to set
	 */
	public void setItemProp(String itemProp) {
		this.itemProp = itemProp;
	}

	/**
	 * @return the type
	 */
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
	 * @return the valid
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String build() {

		return this.getSchema() + KnowledgeBase.PROPERTY_SEPARATOR
				+ this.getItemType() + KnowledgeBase.PROPERTY_SEPARATOR
				+ this.getItemProp() + KnowledgeBase.PROPERTY_SEPARATOR
				+ this.getType() + KnowledgeBase.PROPERTY_SEPARATOR
				+ this.getValid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return build();
	}

}
