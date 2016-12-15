package com.crawljax.web.jatyta.model.dao;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.model.entities.JatytaCrawlRecord;
import com.crawljax.web.model.CrawlRecord;

public class JatytaCrawlRecordDAO extends DAOImpl<JatytaCrawlRecord, Long> {

	public JatytaCrawlRecordDAO(Session session) {
		super(session);

	}

	/**
	 * Generates a {@link JatytaCrawlRecord} from the {@link CrawlRecord}
	 * parameter.
	 * 
	 * @param newRecord
	 *            The {@link JatytaCrawlRecord} to be mapped. The
	 * @param r
	 *            the {@link CrawlRecord} to generate the
	 *            {@link JatytaCrawlRecord}.
	 * @param statesNumber
	 *            The number of states for the crawl.
	 * @param whiteBoxTest
	 *            a boolean value that indicates if the crawl is with whitebox
	 *            test (true) or blackbox test (false)
	 * @return The {@link JatytaCrawlRecord}.
	 */
	public static JatytaCrawlRecord mappingCrawlRecord(
			JatytaCrawlRecord newRecord, CrawlRecord r, Integer statesNumber, boolean whiteBoxTest) {

		newRecord.setIdCrawlRecord(r.getId());
		newRecord.setConfigurationId(r.getConfigurationId());
		newRecord.setConfigurationName(r.getConfigurationName());
		newRecord.setCreateTime(r.getCreateTime());
		newRecord.setStartTime(r.getStartTime());
		newRecord.setDuration(r.getDuration());
		newRecord.setStatesNumber(statesNumber);
		newRecord.setWhiteBoxTest(whiteBoxTest);
		return newRecord;
	}

	/**
	 * Update the {@link JatytaCrawlRecord} associated with the
	 * {@link CrawlRecord} parameter.
	 * 
	 * @param record
	 *            The {@link CrawlRecord} parameter.
	 * @param statesNumber
	 *            The number of states for the crawl.
	 * @param whiteBoxTest
	 *            a boolean value that indicates if the crawl is with whitebox
	 *            test (true) or blackbox test (false)
	 * @return The updated {@link JatytaCrawlRecord}.
	 */
	public JatytaCrawlRecord updateRecord(CrawlRecord record,
			Integer statesNumber, boolean whiteBoxTest) {
		JatytaCrawlRecord newRecord = getCrawlRecordByCrawlId(record.getId());

		newRecord = JatytaCrawlRecordDAO.mappingCrawlRecord(newRecord, record,
				statesNumber,whiteBoxTest);
		return newRecord;
	}

	/**
	 * Returns the {@link JatytaCrawlRecord} associated with the idCrawlRecord
	 * 
	 * @param idCrawlRecord
	 *            The id of the {@link CrawlRecord} associated.
	 * @return The {@link JatytaCrawlRecord}
	 */
	public JatytaCrawlRecord getCrawlRecordByCrawlId(Integer idCrawlRecord) {

		JatytaCrawlRecord result = null;
		try {

			session.beginTransaction();

			String hql = "from JatytaCrawlRecord jcr where jcr.idCrawlRecord = "
					+ idCrawlRecord + " ";

			Query query = session.createQuery(hql);

			result = (JatytaCrawlRecord) query.uniqueResult();

			session.getTransaction().commit();

		} catch (NonUniqueResultException e) {
			logger.error(e.getLocalizedMessage());
			result = null;
		}

		return result;
	}
}
