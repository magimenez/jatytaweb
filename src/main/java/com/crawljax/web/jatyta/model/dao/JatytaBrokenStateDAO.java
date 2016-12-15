/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import org.hibernate.Session;

import com.crawljax.web.jatyta.model.entities.JatytaBrokenState;

/**
 * The DAO of {@link JatytaBrokenState}.
 * 
 * @author mgimenez
 * 
 */
public class JatytaBrokenStateDAO extends DAOImpl<JatytaBrokenState, Long> {

	/**
	 * The Constructor
	 * 
	 * @param session
	 *            the {@link Session} to use.
	 */
	public JatytaBrokenStateDAO(Session session) {
		super(session);

	}

}
