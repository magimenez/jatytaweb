package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropName;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.model.dao.DAOImpl;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;

public class PropValueDAO extends DAOImpl<PropValue, Long> {

	public static final String ITEMPROP_NUMBER = "ITEMPROP";
	public static final String PROPVALUE_NUMBER = "PROPVALUE";

	public PropValueDAO(Session session) {
		super(session);

	}

	/**
	 * Persist the parameters, if the object exist, don't perform any update.
	 * 
	 * @param propvalueList
	 *            The {@link List} of {@link PropValue} to persist.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 * @return A {@link Map} contain the PROPVALUE_NUMBER of persisted objects.
	 */
	public Map<String, Integer> savePropNamesAndPropValues(
			List<PropValue> propvalueList) throws JatytaException {
		try {
			Map<String, Integer> result = new HashMap<>();

			Integer countPropvalues = 0;
			Integer countPropNames = 0;
			String hql = "";
			Query query = null;

			session.beginTransaction();

			for (PropValue propValue : propvalueList) {

				// save propname
				hql = " from  PropName  where name =:name";
				query = session.createQuery(hql);
				query.setString("name", propValue.getPropName().getName());
				PropName prop = (PropName) query.uniqueResult();

				if (prop == null) {
					session.save(propValue.getPropName());
					countPropNames++;
				} else {
					propValue.setPropName(prop);
				}

				// save prop value
				hql = "select count(*) from  PropValue where value =:value and propName =:prop";
				query = session.createQuery(hql);
				query.setEntity("prop", propValue.getPropName());
				query.setString("value", propValue.getValue());
				Long count = (Long) query.uniqueResult();

				if (count == 0) {
					session.save(propValue);
					countPropvalues++;
				}

			}

			session.getTransaction().commit();

			result.put(PROPVALUE_NUMBER, countPropvalues);

			logger.info("Eliminado: " + ItemType.class.getSimpleName());
			return result;
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_INSERT_ERROR_MESSAGE);

		}
	}

	/**
	 * Return all propNames and values from Knowledge Base.
	 * 
	 * @return a {@link Map} with the propName and the {@link List} of values
	 *         Associated.
	 * @throws JatytaException
	 *             if a {@link HibernateException} occurs.
	 */
	public Map<String, List<String>> getAllPropNamesAndValues()
			throws JatytaException {
		HashMap<String, List<String>> result = new HashMap<>();
		try {

			session.beginTransaction();

			String hql = " select distinct propName.name, value from  PropValue "
					+ " order by propName.name ";
			Query query = session.createQuery(hql);
			List<Object[]> valueRegArrayList = query.list();

			for (Object[] objects : valueRegArrayList) {
				String key = (String) objects[0];
				String value = (String) objects[1];
				if (result.containsKey(key)) {
					result.get(key).add(value);
				} else {
					List<String> list = new LinkedList<String>();
					list.add(value);
					result.put(key, list);
				}
			}
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
	 * Return all propNames and values from Knowledge Base with the filter
	 * criteria(VALID, INVALID and BOTH).
	 * 
	 * @param filter
	 *            The {@link FormValueType} to filter the values.
	 * @return a {@link Map} with the propName and the {@link List} of values
	 *         Associated.
	 * @throws JatytaException
	 *             if a {@link HibernateException} occurs.
	 */
	public Map<String, List<String>> getAllPropNamesAndValuesByFilter(
			FormValueType filter) throws JatytaException {
		HashMap<String, List<String>> result = new HashMap<>();
		try {

			session.beginTransaction();

			String hql = "";
			String filterhql = "";

			if (filter.equals(FormValueType.VALID)) {
				filterhql = " where isValid is true ";
			} else if (filter.equals(FormValueType.INVALID)) {
				filterhql = " where isValid is false ";
			}

			hql = " select distinct propName.name, value from  PropValue "
					+ filterhql + " order by propName.name ";
			Query query = session.createQuery(hql);
			List<Object[]> valueRegArrayList = query.list();

			for (Object[] objects : valueRegArrayList) {
				String key = (String) objects[0];
				String value = (String) objects[1];
				if (result.containsKey(key)) {
					result.get(key).add(value);
				} else {
					List<String> list = new LinkedList<String>();
					list.add(value);
					result.put(key, list);
				}
			}
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
