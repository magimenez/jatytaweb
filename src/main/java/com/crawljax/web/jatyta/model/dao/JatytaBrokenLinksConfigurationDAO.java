/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenLinksConfiguration;

/**
 * The DAO of {@link JatytaBrokenLinksConfigurationDAO}
 * 
 * @author mgimenez
 * 
 */
public class JatytaBrokenLinksConfigurationDAO extends
		DAOImpl<JatytaBrokenLinksConfiguration, Long> {

	/**
	 * The Constructor
	 * 
	 * @param session
	 *            the {@link Session} to use.
	 */
	public JatytaBrokenLinksConfigurationDAO(Session session) {
		super(session);

	}

	/**
	 * Return the {@link List} of {@link JatytaBrokenLinksConfiguration}
	 * associated to the Crawljax Configuration Id parameter.
	 * 
	 * @param configId
	 *            The Crawljax Configuration Id.
	 * @return The {@link List} of {@link JatytaBrokenLinksConfiguration}.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */
	public List<JatytaBrokenLinksConfiguration> getBrokenLinksConfigurationsByConfigId(
			String configId) throws JatytaException {

		List<JatytaBrokenLinksConfiguration> result = null;
		String hql = "";
		Query query = null;

		try {

			session.beginTransaction();

			hql = " from  JatytaBrokenLinksConfiguration  where configurationId =:configId";
			query = session.createQuery(hql);
			query.setString("configId", configId);
			result = (List<JatytaBrokenLinksConfiguration>) query.list();

			session.getTransaction().commit();
			return result;
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);

		}

	}

	/**
	 * Return the {@link List} of Patterns associated to the Crawljax
	 * Configuration Id parameter.
	 * 
	 * @param configId
	 *            The Crawljax Configuration Id.
	 * @return The {@link List} of {@link String}
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */
	public List<String> getBrokenLinksPatternsByConfigId(String configId)
			throws JatytaException {

		List<String> result = null;
		String hql = "";
		Query query = null;

		try {

			session.beginTransaction();

			hql = "select b.pattern from  JatytaBrokenLinksConfiguration b where b.configurationId =:configId";
			query = session.createQuery(hql);
			query.setString("configId", configId);
			result = (List<String>) query.list();

			session.getTransaction().commit();
			return result;
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);

		}

	}

}
