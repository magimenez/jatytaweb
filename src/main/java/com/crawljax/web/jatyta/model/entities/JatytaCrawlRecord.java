/**
 * 
 */
package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;

/**
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "idCrawlRecord")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class JatytaCrawlRecord implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private int idCrawlRecord;
	private String configurationId;
	private String configurationName;
	private Date createTime;
	private Date startTime;
	private long duration;
	private int statesNumber;
	private boolean whiteBoxTest;

	/**
	 * @return the id
	 */
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
	 * @return the idCrawlRecord
	 */
	@Column(name = "idCrawlRecord", nullable = false, unique = true)
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
	 * @return the configurationName
	 */
	@Column(name = "configurationName", nullable = false)
	public String getConfigurationName() {
		return configurationName;
	}

	/**
	 * @param configurationName
	 *            the configurationName to set
	 */
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the startTime
	 */
	@Column(name = "startTime")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the duration
	 */
	@Column(name = "duration")
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	

	/**
	 * @return the statesNumber
	 */
	@Column(name = "statesNumber")
	public int getStatesNumber() {
		return statesNumber;
	}

	/**
	 * @param statesNumber the statesNumber to set
	 */
	public void setStatesNumber(int statesNumber) {
		this.statesNumber = statesNumber;
	}

	
	
	/**
	 * @return the whiteBoxTest
	 */
	@Column(name = "whiteBoxTest")
	public boolean isWhiteBoxTest() {
		return whiteBoxTest;
	}

	/**
	 * @param whiteBoxTest the whiteBoxTest to set
	 */
	public void setWhiteBoxTest(boolean whiteBoxTest) {
		this.whiteBoxTest = whiteBoxTest;
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

		return "" + idCrawlRecord;
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
		if (keyName != null && idCrawlRecord == 0) {
			idCrawlRecord = Integer.parseInt(keyName);
		}
	}

}
