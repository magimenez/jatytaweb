/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import org.hibernate.Session;

import com.crawljax.web.jatyta.model.entities.JatytaFormFieldRecord;

/**
 * The DAO of {@link JatytaFormFieldRecord}.
 * 
 * @author mgimenez
 * 
 */
public class JatytaFormFieldRecordDAO extends DAOImpl<JatytaFormFieldRecord, Long> {

	public JatytaFormFieldRecordDAO(Session session) {
		super(session);

	}

}
