package com.crawljax.web.jatyta.model.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.model.entities.EntityInterface;

public class DAOImpl<Entity extends EntityInterface<K>, K extends Serializable>
		implements DAOInterface<Entity, K> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected Session session;
	public Class domainClass = getDomainClass();

	public DAOImpl(Session session) {

		this.session = session;
	}

	@Override
	public Entity save(Entity t) {

		if (t != null) {
			if (t.getId() == null) {
				K id = (K) session.save(t);
				t.setId(id);
			} else {

				session.update(t);
			}
		}
		return t;
	}

	@Override
	public Entity update(Entity t) {

		if (t != null) {

			session.update(t);

		}
		return t;
	}

	@Override
	public void delete(Entity t) {

		if (t != null) {
			if (t.getId() != null) {

				session.delete(t);

			}
		}

	}

	@Override
	public Entity find(Entity t) {

		if (t == null) {
			return null;
		}

		return (Entity) session.get(domainClass, t.getId());
	}

	@Override
	public List<Entity> getAll() {

		return session.createQuery("from " + domainClass.getSimpleName())
				.list();
	}

	protected Class getDomainClass() {
		if (domainClass == null) {
			ParameterizedType thisType = (ParameterizedType) getClass()
					.getGenericSuperclass();
			domainClass = (Class) thisType.getActualTypeArguments()[0];
		}

		return domainClass;
	}
}
