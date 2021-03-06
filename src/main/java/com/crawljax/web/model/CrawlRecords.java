package com.crawljax.web.model;

import java.util.ArrayList;
import java.util.List;

import com.crawljax.web.fs.WorkDirManager;
import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlRecordDAO;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlRecord;
import com.crawljax.web.model.CrawlRecord.CrawlStatusType;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CrawlRecords {
	private final List<CrawlRecord> crawlList;
	private final WorkDirManager workDirManager;
	private final Configurations configurations;
	private int identity = 0;
	private final JatytaService jatytaService;

	@Inject
	public CrawlRecords(WorkDirManager workDirManager,
			Configurations configurations, JatytaService jatytaService) {
		this.workDirManager = workDirManager;
		this.configurations = configurations;
		crawlList = this.workDirManager.loadCrawlRecords();
		this.jatytaService = jatytaService;
		if (crawlList.size() > 0)
			identity = crawlList.get(0).getId();
	}

	/**
	 * @return the crawlRecords
	 */
	public List<CrawlRecord> getCrawlList() {
		return crawlList;
	}

	public List<CrawlRecord> getCrawlListByConfigID(String configId) {
		List<CrawlRecord> configRecordList = new ArrayList<CrawlRecord>();
		for (CrawlRecord r : crawlList) {
			if (r.getConfigurationId().equals(configId))
				configRecordList.add(r);
		}
		return configRecordList;
	}

	public List<CrawlRecord> getActiveCrawlList() {
		List<CrawlRecord> recordList = new ArrayList<CrawlRecord>();
		for (CrawlRecord r : crawlList) {
			if (r.getCrawlStatus() == CrawlStatusType.queued
					|| r.getCrawlStatus() == CrawlStatusType.running
					|| r.getCrawlStatus() == CrawlStatusType.initializing) {
				recordList.add(0, r);
			}
		}
		return recordList;
	}

	public CrawlRecord findByID(int id) {
		CrawlRecord record = null;
		for (CrawlRecord r : crawlList) {
			if (r.getId() == id) {
				record = r;
				break;
			}
		}
		return record;
	}

	public CrawlRecord add(String configId) {
		CrawlRecord r = new CrawlRecord();
		r.setId(++identity);
		r.setConfigurationId(configId);
		r.setConfigurationName(configurations.findByID(configId).getName());
		crawlList.add(0, r);
		workDirManager.saveCrawlRecord(r);
		// MGIMENEZ: se almacena en la Base de conocimiento
		JatytaCrawlRecord newRecord = new JatytaCrawlRecord();
		newRecord = JatytaCrawlRecordDAO.mappingCrawlRecord(newRecord, r, 0, false);
		try {
			jatytaService.saveEntity(newRecord);
		} catch (JatytaException e) {
			e.printStackTrace();
		}

		return r;
	}

	public CrawlRecord update(CrawlRecord record) {
		// assuming we are not updating from client side and can use same
		// reference
		workDirManager.saveCrawlRecord(record);

		return record;
	}
}
