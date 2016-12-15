package com.crawljax.web.jatyta.knowledgebase.db.model.dao;

import org.hibernate.Session;

import com.crawljax.web.jatyta.knowledgebase.db.model.entities.NativeType;
import com.crawljax.web.jatyta.model.dao.DAOImpl;

public class NativeTypeDAO extends DAOImpl<NativeType, Long> {

	public NativeTypeDAO(Session session) {
		super(session);
	}

}
