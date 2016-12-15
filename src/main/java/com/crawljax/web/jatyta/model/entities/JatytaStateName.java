/**
 * 
 */
package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.plugins.crawloverview.CrawlOverview;
import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;

/**
 * The entity that represents the Crawljax states.
 * 
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class JatytaStateName implements Serializable, EntityInterface<Long> {
	private static final long serialVersionUID = 1L;
	/**
	 * The id
	 */
	private Long id;

	/**
	 * The crawljax id record.
	 */
	private int idCrawlRecord;

	/**
	 * The id of the Crawljax State.
	 */
	private Integer state;

	/**
	 * The name of the state.
	 */
	private String name;

	/**
	 * The url of the state.
	 */
	private String url;

	/**
	 * Constructor.
	 * 
	 * @param idCrawlRecord
	 *            The Crawlrecord ID.
	 * @param state
	 *            The state ID.
	 * @param name
	 *            The name of the state.
	 */
	public JatytaStateName(int idCrawlRecord, Integer state, String name, String url) {
		super();
		this.idCrawlRecord = idCrawlRecord;
		this.state = state;
		this.name = name;
		this.url = url;
	}
	/**
	 * Default Constructor
	 */
	public JatytaStateName() {
		super();
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
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
	 * @return the idCrawlRecord
	 */
	@Column(name = "idCrawlRecord", nullable = false)
	public int getIdCrawlRecord() {
		return idCrawlRecord;
	}

	/**
	 * @param idCrawlRecord
	 *            the idCrawlRecord to set
	 */
	public void setIdCrawlRecord(int idCrawlRecord) {
		this.idCrawlRecord = idCrawlRecord;
	}

	/**
	 * @return the state
	 */
	@Column(name = "state", nullable = false)
	public Integer getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return the name
	 */
	@Column(name = "name", nullable = false)
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
	 * @return the URL
	 */
	@Column(name = "url", nullable = false)
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the URL to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
			return id.toString();
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
		id = Long.decode(keyName);

	}
	
	/**
	 * Return the URL of thumbnail from the state generated by
	 * {@link CrawlOverview}.
	 * 
	 * @return The URL of the small image saved by {@link CrawlOverview}.
	 */
	@Transient
	public String getThumbnailURL() {
		String result = "/output/crawl-records/" + idCrawlRecord
				+ "/plugins/0/screenshots/";
		if (state == 0) {
			result = result + "index_small.jpg";
		} else {
			result = result + "state" + state + "_small.jpg";
		}
		return result;
	}

	/**
	 * Return the URL of screenshot from the  state generated by
	 * {@link CrawlOverview}.
	 * 
	 * @return The URL of the image saved by {@link CrawlOverview}.
	 */
	@Transient
	public String getScreenshotURL() {
		String result = "/output/crawl-records/" + idCrawlRecord
				+ "/plugins/0/screenshots/";
		if (state == 0) {
			result = result + "index.jpg";
		} else {
			result = result + "state" + state + ".jpg";
		}
		return result;
	}

}