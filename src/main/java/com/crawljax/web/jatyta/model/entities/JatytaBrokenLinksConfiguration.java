/**
 * 
 */
package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;

/**
 * Class that represents the broken link configuration.
 * 
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "configurationId",
		"pattern" }))
public class JatytaBrokenLinksConfiguration implements Serializable,
		EntityInterface<Long> {
	private static final long serialVersionUID = 1L;
	/**
	 * The id
	 */
	private Long id;

	/**
	 * The Crawljax configuration id.
	 */
	private String configurationId;

	/**
	 * The pattern to identify a broken state.
	 */
	private String pattern;

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
	 * Obtain the Crawljax Configuration id associated.
	 * 
	 * @return the configurationId
	 */
	@Column(name = "configurationId", nullable = false)
	public String getConfigurationId() {
		return configurationId;
	}

	/**
	 * @param configurationId
	 *            the configurationId to set
	 */
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	/**
	 * Return the patten to identify broken states.
	 * 
	 * @return the pattern
	 */
	@Column(name = "pattern", nullable = false)
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	@Transient
	public String toString() {

		return "id = " + getId() + ", configurationId = "
				+ getConfigurationId() + ", pattern = " + getPattern();

	}

	/**
	 * Return a {@link List} of {@link JatytaBrokenLinksConfiguration} with the
	 * default patterns
	 * 
	 * @param configurationId
	 *            The Crawljax configuration id.
	 * @return The {@link List} of {@link JatytaBrokenLinksConfiguration} with
	 *         the patterns : "404", "Error [0-9]+", "no encontrada",
	 *         "not found".
	 */
	@Transient
	public static List<JatytaBrokenLinksConfiguration> getDefaultValues(
			String configurationId) {

		List<JatytaBrokenLinksConfiguration> result = new ArrayList<JatytaBrokenLinksConfiguration>();

		JatytaBrokenLinksConfiguration config = new JatytaBrokenLinksConfiguration();
		config.setConfigurationId(configurationId);
		config.setPattern("404");
		result.add(config);
		config = new JatytaBrokenLinksConfiguration();
		config.setConfigurationId(configurationId);
		config.setPattern("Error [0-9]+");
		result.add(config);
		config = new JatytaBrokenLinksConfiguration();
		config.setConfigurationId(configurationId);
		config.setPattern("no encontrada");
		result.add(config);
		config = new JatytaBrokenLinksConfiguration();
		config.setConfigurationId(configurationId);
		config.setPattern("not found");
		result.add(config);

		return result;

	}
}
