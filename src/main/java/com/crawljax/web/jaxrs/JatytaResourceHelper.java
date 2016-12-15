/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.util.List;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropName;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.entities.EntityInterface;

/**
 * Class with static methods to support persistence for restful services.
 * 
 * @author mgimenez
 *
 */
public class JatytaResourceHelper {

	/**
	 * Return a {@link PaginationResponse} with the entities from
	 * {@link EntityInterface} parameter.
	 * 
	 * @param jatytaService
	 *            The Persistence Service.
	 * @param entity
	 *            The {@link EntityInterface} parameter.
	 * @param pageNumber
	 *            The page number (offset).
	 * @param condition
	 *            The condition (hql where)
	 * @param pageSize
	 *            The page size (limit rows)
	 * @return The {@link PaginationResponse} with the entities,
	 *         {@link Pagination} and {@link Sorting}.
	 * @throws JatytaException
	 */
	public static PaginationResponse getPagination(JatytaService jatytaService, EntityInterface entity,
			Integer pageNumber, String condition, Integer pageSize) throws JatytaException {
		Integer count = 0;
		List<EntityInterface> entities = null;
		if (pageNumber != null && pageNumber > 0) {
			if (condition != null && !condition.isEmpty()) {
				count = jatytaService.getCountByHqlCondition(entity, condition);
				entities = jatytaService.getEntityByHqlCondition(entity, condition, pageNumber, pageSize);
			} else {
				count = jatytaService.getCount(entity);
				entities = jatytaService.getAllEntityValues(entity, pageNumber, pageSize);
			}
		} else if (condition != null && !condition.isEmpty()) {
			count = jatytaService.getCountByHqlCondition(entity, condition);
			entities = jatytaService.getEntityByHqlCondition(entity, condition);
		} else {
			count = jatytaService.getCount(entity);
			entities = jatytaService.getAllEntityValues(entity);
		}

		PaginationResponse pr = new PaginationResponse(entities, new Pagination(pageSize, pageNumber, count),
				new Sorting(null, null));
		return pr;
	}

}
