/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.form;

/**
 * Class that represents the value used in a form field for a specific state 
 * and order (iteration of values for the same state).
 * @author mgimenez
 * 
 */
public class FormValuesInfoDetail {

	/**
	 * The type for Value, (VALID, INVALID, UNKNOWN, EMPTY, OVERFLOW)
	 */
	private FormValueType type;
	/**
	 * The value.
	 */
	private Object value;
	/**
	 * The state associated.
	 */
	private Integer stateAsociated;
	/**
	 * The order for the value in the associated state.
	 */
	private Integer valueOrder = 0;

	/**
	 * @return the type
	 */
	public FormValueType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FormValueType type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the stateAsociated
	 */
	public Integer getStateAsociated() {
		return stateAsociated;
	}

	/**
	 * @param stateAsociated
	 *            the stateAsociated to set
	 */
	public void setStateAsociated(Integer stateAsociated) {
		this.stateAsociated = stateAsociated;
	}

	/**
	 * @return the valueOrder
	 */
	public Integer getValueOrder() {
		return valueOrder;
	}

	/**
	 * @param valueOrder the valueOrder to set
	 */
	public void setValueOrder(Integer valueOrder) {
		this.valueOrder = valueOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = " State = " + getStateAsociated() + " ; Type = "
				+ getType() + " ; Value = " + getValue();

		return result;
	}

}
