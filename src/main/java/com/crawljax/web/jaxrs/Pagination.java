/**
 * 
 */
package com.crawljax.web.jaxrs;

/**
 * Class that represents the meta-data for pagination.
 * 
 * @author mgimenez
 *
 */
public class Pagination {

	Integer limit;
	Integer offset;
	Integer count;

	public Pagination(Integer limit, Integer offset, Integer count) {
		super();
		this.limit = limit;
		this.offset = offset;
		this.count = count;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

}
