/**
 * 
 */
package com.crawljax.web.jatyta.configuration;

/**
 * The configuration of the KnowledgeBase
 * 
 * @author mgimenez
 */
public class KnowledgeBaseConfiguration {
	/**
	 * Type of KnowledgeBase Source
	 */
	public enum KnowledgeBaseSourceType {
		/**
		 * Use a data base for persistence the data of KnowledgeBase
		 */
		DATABASE("db"),
		/**
		 * Use a XML file for persistence the data of KnowledgeBase
		 */
		XMLFILE("xml"),
		/**
		 * Use an text file for persistence the data of KnowledgeBase
		 */
		TEXTFILE("file");

		private final String value;

		private KnowledgeBaseSourceType(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	private KnowledgeBaseSourceType sourceType;
	private String sourceName;

	public KnowledgeBaseSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(KnowledgeBaseSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

}
