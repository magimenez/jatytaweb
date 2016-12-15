package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemProp;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.Schema;
import com.crawljax.web.jatyta.model.dao.DAOImpl;
/**
 * 
 * @author mgimenez
 *
 */
public class SchemaDAO extends DAOImpl<Schema, Long> {


	public SchemaDAO(Session session) {
		super(session);

	}

	/**
	 * Delete the {@link Schema} with the id parameter and the {@link ItemType},
	 * {@link ItemProp} and {@link PropValue} associated.
	 * 
	 * @param idSchema
	 *            the id parameter of the {@link Schema} to be deleted.
	 * @throws JatytaException
	 *             If a {@link HibernateException} occurs.
	 */

	public void deleteSchemaAndDataAssociated(Long idSchema)
			throws JatytaException {
		try {
			session.beginTransaction();

			// delete prop value
			String hql = "from  PropValue where itemProp.itemType.schema.id =:idschema";
			Query query = session.createQuery(hql);
			query.setLong("idschema", idSchema);
			List<PropValue> propValuelist = (List<PropValue>)query.list();
			for (PropValue propValue : propValuelist) {
				session.delete(propValue);
			}
			// delete itemprop
			hql = "from  ItemProp  where itemType.schema.id =:idschema";
			query = session.createQuery(hql);
			query.setLong("idschema", idSchema);
			List<ItemProp> itemProplist = query.list();
			for (ItemProp itemProp : itemProplist) {
				session.delete(itemProp);
			}
			// delete itemtype
			hql = "from  ItemType where schema.id =:idschema";
			query = session.createQuery(hql);
			query.setLong("idschema", idSchema);
			List<ItemType> itemTypelist = query.list();
			for (ItemType item : itemTypelist) {
				session.delete(item);
			}
			// delete schema
			hql = "from  Schema where id =:idschema";
			query = session.createQuery(hql);
			query.setLong("idschema", idSchema);
			Schema schema = (Schema) query.uniqueResult();
			session.delete(schema);

			session.getTransaction().commit();

			logger.info("Eliminado: " + Schema.class.getSimpleName());
		} catch (Exception ex) {
			session.getTransaction().rollback();
			logger.error(ex.getMessage());
			throw new JatytaException(
					JatytaException._PERSISTENCE_DELETE_ERROR_MESSAGE);

		}
	}

}
