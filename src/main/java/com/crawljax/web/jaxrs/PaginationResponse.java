/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.util.ArrayList;
import java.util.List;

/**
 * Object to define the JSON Pagination Response.
 * 
 * @author mgimenez
 *
 */
public class PaginationResponse {

	List entities = new ArrayList<>();
	Pagination pagination;
	Sorting sorting;

	public PaginationResponse(List entities, Pagination pagination, Sorting sorting) {
		super();
		this.entities = entities;
		this.pagination = pagination;
		this.sorting = sorting;
	}

	/**
	 * @return the entities
	 */
	public List getEntities() {
		return entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(List entities) {
		this.entities = entities;
	}

	/**
	 * @return the pagination
	 */
	public Pagination getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            the pagination to set
	 */
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return the sorting
	 */
	public Sorting getSorting() {
		return sorting;
	}

	/**
	 * @param sorting
	 *            the sorting to set
	 */
	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

}
