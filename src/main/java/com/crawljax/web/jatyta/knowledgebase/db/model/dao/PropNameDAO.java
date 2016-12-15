/**
 * 
 */
package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropName;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.model.dao.DAOImpl;

/**
 * The DAO of {@link PropName}
 * 
 * @author mgimenez
 * 
 */
public class PropNameDAO extends DAOImpl<PropName, Long> {

	/**
	 * The default constructor
	 * 
	 * @param session
	 *            The {@link Session} to be used.
	 */
	public PropNameDAO(Session session) {
		super(session);
	}

	/**
	 * Delete the {@link PropName} with the id parameter and delete the
	 * {@link PropValue} associated to the knowledge base.
	 * 
	 * @param idpropName
	 *            The id of the {@link PropName} to delete.
	 */
	public void deletePropnameAndValues(Long idpropName) {
		session.beginTransaction();

		// delete prop value
		String hql = "from  PropValue where propName.id =:idpropname";
		Query query = session.createQuery(hql);
		query.setLong("idpropname", idpropName);
		List<PropValue> propValuelist = ((List<PropValue>) query.list());
		for (PropValue propValue : propValuelist) {
			session.delete(propValue);
		}

		// delete PROPNAME
		hql = "from  PropName where id =:idpropname";
		query = session.createQuery(hql);
		query.setLong("idpropname", idpropName);
		PropName propName = (PropName) query.uniqueResult();
		session.delete(propName);

		session.getTransaction().commit();

		logger.debug("Eliminado: " + PropName.class.getSimpleName() + " : "
				+ propName);

	}

}
