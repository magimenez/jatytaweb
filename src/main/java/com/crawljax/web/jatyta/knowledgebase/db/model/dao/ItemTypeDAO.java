package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemProp;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.model.dao.DAOImpl;

/**
 * The DAO of {@link ItemType}
 * 
 * @author mgimenez
 * 
 */
public class ItemTypeDAO extends DAOImpl<ItemType, Long> {


	/**
	 * Constructor by Default.
	 * 
	 * @param session
	 *            the {@link Session} to use.
	 */
	public ItemTypeDAO(Session session) {
		super(session);
	}

	/**
	 * Delete the {@link ItemType} with the id parameter and the
	 * {@link ItemProp} and {@link PropValue} associated.
	 * 
	 * @param idItemType
	 *            the id parameter of the {@link ItemType} to be deleted.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */
	public void deleteItemTypeAndDataAssociated(Long idItemType)
			throws JatytaException {
		try {
			session.beginTransaction();

			// delete prop value
			String hql = "from  PropValue where itemProp.itemType.id =:iditemtype";
			Query query = session.createQuery(hql);
			query.setLong("iditemtype", idItemType);
			List<PropValue> propValuelist = query.list();
			for (PropValue propValue : propValuelist) {
				session.delete(propValue);
			}
			// delete itemprop
			hql = "from  ItemProp  where itemType.id =:iditemtype";
			query = session.createQuery(hql);
			query.setLong("iditemtype", idItemType);
			List<ItemProp> itemProplist = query.list();
			for (ItemProp itemProp : itemProplist) {
				session.delete(itemProp);
			}
			// delete itemtype
			hql = "from  ItemType where id =:iditemtype";
			query = session.createQuery(hql);
			query.setLong("iditemtype", idItemType);
			List<ItemType> itemTypelist = query.list();
			for (ItemType item : itemTypelist) {
				session.delete(item);
			}

			session.getTransaction().commit();

			logger.info("Eliminado: " + ItemType.class.getSimpleName());
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_DELETE_ERROR_MESSAGE);

		}
	}
}
