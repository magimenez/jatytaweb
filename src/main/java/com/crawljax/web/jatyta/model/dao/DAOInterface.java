/**
 * 
 */
package com.crawljax.web.jatyta.model.dao;

import java.io.Serializable;
import java.util.List;

import com.crawljax.web.jatyta.model.entities.EntityInterface;

/**
 * @author Beto
 * 
 */
public interface DAOInterface<Entity extends EntityInterface<K>, K extends Serializable> {
	Entity save(Entity t);
	Entity update(Entity t);
	void delete(Entity t);

	Entity find(Entity t);

	List<Entity> getAll();
}
