/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.model.entities.JatytaFormValueRecord;
import com.crawljax.web.jatyta.model.entities.JatytaStateName;

/**
 * * The DAO of {@link JatytaStateName}
 * 
 * @author mgimenez
 * 
 */
public class JatytaStateNameDAO extends DAOImpl<JatytaStateName, Long> {

	/**
	 * Constructor by Default.
	 * 
	 * @param session
	 *            the {@link Session} to use.
	 */
	public JatytaStateNameDAO(Session session) {
		super(session);
	}

	/**
	 * Returns the {@link List} of {@link JatytaStateName} for 
	 * {@link JatytaValidationRecord} associated with the idCrawlRecord 
	 * parameter.
	 * @param idCrawlRecord The id of the crawl record.
	 * @return The {@link List} of {@link JatytaStateName}.
	 */
	@SuppressWarnings("unchecked")
	public List<JatytaStateName> getValidationStates(int idCrawlRecord) {
		List<JatytaStateName> result = new LinkedList<>();

		try {

			session.beginTransaction();

			String hql = "from JatytaStateName jsn where jsn.state in (" 
					+ " select jvr.validationState "
					+ " from JatytaValidationRecord jvr " 
					+ " where jvr.idCrawlRecord = " + idCrawlRecord
					+ " ) and jsn.idCrawlRecord = " + idCrawlRecord + " ";

			Query query = session.createQuery(hql);

			result = (List<JatytaStateName>) query.list();

			session.getTransaction().commit();

		} catch (NonUniqueResultException e) {
			logger.error(e.getLocalizedMessage());
			result = null;
		}

		return result;
	}
	
	/**
	 * Returns the {@link List} of {@link JatytaStateName} for 
	 * {@link JatytaFormValueRecord} associated with the idCrawlRecord 
	 * parameter.
	 * @param idCrawlRecord The id of the crawl record.
	 * @return The {@link List} of {@link JatytaStateName}.
	 */
	@SuppressWarnings("unchecked")
	public List<JatytaStateName> getFormStates(int idCrawlRecord) {
		List<JatytaStateName> result = new LinkedList<>();

		try {

			session.beginTransaction();

			String hql = "from JatytaStateName jsn where jsn.state in (" 
					+ " select jfvr.stateAsociated "
					+ " from JatytaFormValueRecord jfvr " 
					+ " where jfvr.idCrawlRecord = " + idCrawlRecord
					+ " ) and jsn.idCrawlRecord = " + idCrawlRecord + " ";

			Query query = session.createQuery(hql);

			result = (List<JatytaStateName>) query.list();

			session.getTransaction().commit();

		} catch (NonUniqueResultException e) {
			logger.error(e.getLocalizedMessage());
			result = null;
		}

		return result;
	}
}
