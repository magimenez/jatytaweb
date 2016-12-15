/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import org.hibernate.Session;

import com.crawljax.web.jatyta.model.entities.JatytaValidationRecord;

/**
 * The DAO of {@link JatytaValidationRecord}.
 * 
 * @author mgimenez
 * 
 */
public class JatytaValidationRecordDAO 
extends DAOImpl<JatytaValidationRecord, Long> {

	public JatytaValidationRecordDAO(Session session) {
		super(session);

	}



}
