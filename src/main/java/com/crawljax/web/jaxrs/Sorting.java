/**
 * 
 */
package com.crawljax.web.jaxrs;

/**
 * Class that represents the meta-data for sorting list.
 * 
 * @author mgimenez
 *
 */
public class Sorting {
	OrderDirection orderDirection;
	String orderFieldName;

	public Sorting(OrderDirection orderDirection, String orderFieldName) {
		super();
		this.orderDirection = orderDirection;
		this.orderFieldName = orderFieldName;
	}

	/**
	 * @return the orderDirection
	 */
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	/**
	 * @param orderDirection
	 *            the orderDirection to set
	 */
	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * @return the orderFieldName
	 */
	public String getOrderFieldName() {
		return orderFieldName;
	}

	/**
	 * @param orderFieldName
	 *            the orderFieldName to set
	 */
	public void setOrderFieldName(String orderFieldName) {
		this.orderFieldName = orderFieldName;
	}

}
