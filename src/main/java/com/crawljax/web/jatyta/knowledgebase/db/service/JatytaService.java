package com.crawljax.web.jatyta.knowledgebase.db.service;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.inject.Singleton;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.ItemPropDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.ItemTypeDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.NativeTypeDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.PropNameDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.PropValueDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.SchemaDAO;
import com.crawljax.web.jatyta.knowledgebase.db.util.HibernateUtil;
import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.model.dao.DAOImpl;
import com.crawljax.web.jatyta.model.dao.DAOInterface;
import com.crawljax.web.jatyta.model.dao.JatytaBrokenLinksConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaBrokenStateDAO;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlRecordDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormFieldRecordDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormValueRecordDAO;
import com.crawljax.web.jatyta.model.dao.JatytaStateNameDAO;
import com.crawljax.web.jatyta.model.dao.JatytaValidationConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaValidationRecordDAO;
import com.crawljax.web.jatyta.model.entities.EntityInterface;

@Singleton
public class JatytaService {
	protected static final Logger logger = LoggerFactory
			.getLogger(JatytaService.class);

	// protected Session session;
	// private Transaction tx;

	public JatytaService() {
		// session = HibernateUtil.getSessionFactory().openSession();
	}

	public EntityInterface saveEntity(EntityInterface e) throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {

			DAOInterface dao = getDAO(e, session);

			session.beginTransaction();

			e = dao.save(e);

			session.getTransaction().commit();

			logger.debug("Guardado: " + e.getClass().getSimpleName());
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_INSERT_ERROR_MESSAGE);

		} finally {
			// session.close();
		}

		return e;
	}

	/**
	 * 
	 * @param e
	 * @throws JatytaException
	 */
	public void deleteEntity(EntityInterface e) throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {

			DAOInterface dao = getDAO(e, session);

			session.beginTransaction();

			dao.delete(e);

			session.getTransaction().commit();

			logger.debug("Guardado: " + e.getClass().getSimpleName());
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_DELETE_ERROR_MESSAGE);
		} finally {

		}

	}

	public List<EntityInterface> getEntityByKeyName(EntityInterface e)
			throws JatytaException {

		String columnName = null;

		for (Annotation annotation : e.getClass().getAnnotations()) {
			if (annotation instanceof JatytaAnnotation) {
				columnName = ((JatytaAnnotation) annotation).keyName();
				break;
			}
		}

		if (columnName != null) {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String query = "from " + e.getClass().getSimpleName() + " where "
					+ columnName + " = '" + e.getKeyName() + "'";
			List<EntityInterface> list = null;

			try {
				session.beginTransaction();
				list = session.createQuery(query).list();
				session.getTransaction().commit();
			} catch (Exception ex) {
				session.getTransaction().rollback();
				logger.error(ex.getMessage());
				throw new JatytaException(
						JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
			} finally {
				// session.close();
			}

			return list;
		}

		return null;

	}

	public List<EntityInterface> getAllEntityValues(EntityInterface e)
			throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		String query = "from " + e.getClass().getSimpleName();
		List<EntityInterface> list = null;
		try {

			session.beginTransaction();
			list = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
		} finally {
			// session.close();
		}

		return list;
	}
	
	public List<EntityInterface> getAllEntityValues(EntityInterface e, Integer pageNumber, Integer pageSize)
			throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		String query = "from " + e.getClass().getSimpleName();
		List<EntityInterface> list = null;
		try {

			session.beginTransaction();
			Query qry = session.createQuery(query);
			qry.setFirstResult((pageNumber - 1) * pageSize);
			qry.setMaxResults(pageSize);
			list = qry.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
		} finally {
			// session.close();
		}

		return list;
	}
	
	
	public Integer getCount(EntityInterface e)
			throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		String query = "select count(*) from " + e.getClass().getSimpleName();
		Long count = new Long(0);
		try {

			session.beginTransaction();
			count = (Long) session.createQuery(query).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
		} finally {
			// session.close();
		}

		return count.intValue();
	}

	public List<EntityInterface> getEntityByHqlCondition(EntityInterface e,
			String condicion, Integer pageNumber, Integer pageSize) throws JatytaException {

		if (condicion != null) {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String query = "from " + e.getClass().getSimpleName() + " where "
					+ condicion;

			List<EntityInterface> list = null;
			try {

				session.beginTransaction();
				Query qry = session.createQuery(query);
				qry.setFirstResult((pageNumber - 1) * pageSize);
				qry.setMaxResults(pageSize);
				list = qry.list();
				session.getTransaction().commit();
			} catch (Exception ex) {
				session.getTransaction().rollback();
				logger.error(ex.getMessage());
				throw new JatytaException(
						JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
			} finally {
				// session.close();
			}
			return list;
		}

		return null;
	}
	
	public List<EntityInterface> getEntityByHqlCondition(EntityInterface e,
			String condicion ) throws JatytaException {

		if (condicion != null) {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String query = "from " + e.getClass().getSimpleName() + " where "
					+ condicion;

			List<EntityInterface> list = null;
			try {

				session.beginTransaction();
				list = session.createQuery(query).list();
				session.getTransaction().commit();
			} catch (Exception ex) {
				session.getTransaction().rollback();
				logger.error(ex.getMessage());
				throw new JatytaException(
						JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
			} finally {
				// session.close();
			}
			return list;
		}

		return null;
	}
	
	public Integer getCountByHqlCondition(EntityInterface e,
			String condicion) throws JatytaException {

		if (condicion != null) {
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();

			String query = "select count(*) from " + e.getClass().getSimpleName() + " where "
					+ condicion;

			Long count = null;
			try {

				session.beginTransaction();
				count = (Long) session.createQuery(query).uniqueResult();
				session.getTransaction().commit();
			} catch (Exception ex) {
				session.getTransaction().rollback();
				logger.error(ex.getMessage());
				throw new JatytaException(
						JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
			} finally {
				// session.close();
			}
			return count.intValue();
		}

		return new Integer(0);
	}

	/**
	 * Return a unique object from e Class with the id parameter
	 * 
	 * @param e
	 *            The class parameter
	 * @param id
	 *            the id parameter
	 * @return The object of class e.
	 * @throws JatytaException
	 */
	public EntityInterface getEntityByID(EntityInterface e, Long id)
			throws JatytaException {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();

		String query = "from " + e.getClass().getSimpleName() + " where id = "
				+ id;

		EntityInterface list = null;
		try {

			session.beginTransaction();
			list = (EntityInterface) session.createQuery(query).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
		} finally {
			// session.close();
		}
		return list;

	}

	public List<Object> getAllTestingValues() throws JatytaException {
		String hql = "select distinct t.keyName, p.keyName, n.keyName, v.isValid, v.keyName "
				+ "from PropValue v inner join v.itemProp p inner join p.itemType t inner join p.nativeType n "
				+ "order by t.keyName, p.keyName, n.keyName, v.isValid, v.keyName";

		List<Object> list = null;

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {

			session.beginTransaction();
			Query query = session.createQuery(hql);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_SELECT_ERROR_MESSAGE);
		} finally {
			// session.close();
		}

		return list;
	}

	/**
	 * Return a instance of {@link DAOImpl} for the {@link EntityInterface} 
	 * parameter.
	 * @param e The {@link EntityInterface} parameter.
	 * @param session The {@link Session}
	 * @return The {@link DAOImpl} for the {@link EntityInterface} parameter.
	 * @throws JatytaException is DAO not found for the {@link EntityInterface} 
	 * parameter.
	 */
	private DAOImpl getDAO(EntityInterface e, Session session) throws JatytaException{

		String className = e.getClass().getSimpleName();

		if (className.compareToIgnoreCase("ItemProp") == 0) {
			return new ItemPropDAO(session);
		} else if (className.compareToIgnoreCase("ItemType") == 0) {
			return new ItemTypeDAO(session);
		} else if (className.compareToIgnoreCase("NativeType") == 0) {
			return new NativeTypeDAO(session);
		} else if (className.compareToIgnoreCase("PropValue") == 0) {
			return new PropValueDAO(session);
		} else if (className.compareToIgnoreCase("Schema") == 0) {
			return new SchemaDAO(session);
		} else if (className.compareToIgnoreCase("JatytaCrawlRecord") == 0) {
			return new JatytaCrawlRecordDAO(session);
		} else if (className.compareToIgnoreCase("JatytaFormValueRecord") == 0) {
			return new JatytaFormValueRecordDAO(session);
		} else if (className
				.compareToIgnoreCase("JatytaValidationConfiguration") == 0) {
			return new JatytaValidationConfigurationDAO(session);
		} else if (className.compareToIgnoreCase("JatytaCrawlConfiguration") == 0) {
			return new JatytaCrawlConfigurationDAO(session);
		} else if (className.compareToIgnoreCase("JatytaValidationRecord") == 0) {
			return new JatytaValidationRecordDAO(session);
		} else if (className
				.compareToIgnoreCase("JatytaBrokenLinksConfiguration") == 0) {
			return new JatytaBrokenLinksConfigurationDAO(session);
		} else if (className.compareToIgnoreCase("JatytaBrokenState") == 0) {
			return new JatytaBrokenStateDAO(session);
		} else if (className.compareToIgnoreCase("PropName") == 0) {
			return new PropNameDAO(session);
		} else if (className.compareToIgnoreCase("JatytaStateName") == 0) {
			return new JatytaStateNameDAO(session);
		} else if (className.compareToIgnoreCase("JatytaFormFieldRecord") == 0) {
				return new JatytaFormFieldRecordDAO(session);
		} else if (className.compareToIgnoreCase("JatytaFormConfiguration") == 0) {
			return new JatytaFormConfigurationDAO(session);
		} else {
			throw new JatytaException(JatytaException._PERSISTENCE_DAO_ERROR_MESSAGE);
		}
	}

	/**
	 * Return a {@link DAOImpl} of the {@link EntityInterface} parameter
	 * 
	 * @param e
	 *            The {@link EntityInterface}
	 * @return The {@link DAOImpl} of the {@link EntityInterface}.
	 * @throws JatytaException if the DAO not found.
	 */
	public DAOImpl getDAO(EntityInterface e) throws JatytaException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		return getDAO(e, session);
	}

	/*
	 * public void openSession() { if (!session.isOpen()) { session =
	 * HibernateUtil.getSessionFactory().openSession(); } }
	 * 
	 * public void closeSession() { if (session.isOpen()) { session.close(); } }
	 */

}
