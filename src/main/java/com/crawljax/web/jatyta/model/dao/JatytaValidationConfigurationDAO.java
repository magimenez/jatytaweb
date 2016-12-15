/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import java.util.List;

import javax.management.ListenerNotFoundException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.model.entities.JatytaValidationConfiguration;

/**
 * The DAO of {@link JatytaValidationConfiguration}.
 * 
 * @author mgimenez
 * 
 */
public class JatytaValidationConfigurationDAO extends
		DAOImpl<JatytaValidationConfiguration, Long> {

	public JatytaValidationConfigurationDAO(Session session) {
		super(session);

	}

	/**
	 * Return the {@link List} of {@link JatytaValidationConfiguration}
	 * associated to the Crawljax Configuration Id parameter.
	 * 
	 * @param configId
	 *            The Crawljax Configuration Id.
	 * @return The {@link List} of {@link JatytaValidationConfiguration}.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */
	public List<JatytaValidationConfiguration> getValidationConfigurationsByConfigId(
			String configId) throws JatytaException {

		List<JatytaValidationConfiguration> result = null;
		String hql = "";
		Query query = null;

		try {

			session.beginTransaction();

			hql = " from  JatytaValidationConfiguration  where configurationId =:configId";
			query = session.createQuery(hql);
			query.setString("configId", configId);
			result = (List<JatytaValidationConfiguration>) query.list();

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
