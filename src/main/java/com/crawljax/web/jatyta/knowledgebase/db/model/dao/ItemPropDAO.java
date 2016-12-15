package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemProp;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.model.dao.DAOImpl;

/**
 * The DAO of {@link ItemProp}
 * 
 * @author mgimenez
 * 
 */
public class ItemPropDAO extends DAOImpl<ItemProp, Long> {


	/**
	 * The default constructor
	 * 
	 * @param session
	 *            The {@link Session} to be used.
	 */
	public ItemPropDAO(Session session) {
		super(session);
	}

	/**
	 * Delete the {@link ItemProp} with the id parameter and the
	 * {@link PropValue} associated.
	 * 
	 * @param idItemProp
	 *            the id parameter of the {@link ItemProp} to be deleted.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */
	public void deleteItemPropAndDataAssociated(Long idItemProp)
			throws JatytaException {
		try {
			session.beginTransaction();

			// delete prop value
			String hql = "from  PropValue where itemProp.id =:iditemprop";
			Query query = session.createQuery(hql);
			query.setLong("iditemprop", idItemProp);
			List<PropValue> propValuelist = query.list();
			for (PropValue propValue : propValuelist) {
				session.delete(propValue);
			}
			// delete itemprop
			hql = "from  ItemProp  where id =:iditemprop";
			query = session.createQuery(hql);
			query.setLong("iditemprop", idItemProp);
			List<ItemProp> itemProplist = query.list();
			for (ItemProp itemProp : itemProplist) {
				session.delete(itemProp);
			}

			session.getTransaction().commit();

			logger.info("Eliminado: " + ItemProp.class.getSimpleName());
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_DELETE_ERROR_MESSAGE);

		}
	}

}
