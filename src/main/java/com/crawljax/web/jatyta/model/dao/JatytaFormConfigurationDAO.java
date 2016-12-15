/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlRecord;
import com.crawljax.web.jatyta.model.entities.JatytaFormConfiguration;

/**
 * @author mgimenez
 *
 */
public class JatytaFormConfigurationDAO extends DAOImpl<JatytaCrawlRecord, Long> {

	public JatytaFormConfigurationDAO(Session session) {
		super(session);
		
	}
	
	/**
	 * 
	 * @param configId
	 * @return
	 * @throws JatytaException
	 */
	public List<JatytaFormConfiguration> getFormConfigurationsByConfigId(
			String configId) throws JatytaException {
		List<JatytaFormConfiguration> result = null;
		String hql = "";
		Query query = null;

		try {

			session.beginTransaction();

			hql = " from  JatytaFormConfiguration  where configurationId =:configId";
			query = session.createQuery(hql);
			query.setString("configId", configId);
			result = (List<JatytaFormConfiguration>) query.list();

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
