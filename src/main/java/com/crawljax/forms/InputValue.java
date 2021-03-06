package com.crawljax.forms;

import com.crawljax.web.jatyta.plugins.util.form.FormValueType;

/**
 * Value for a FormInput.
 * 
 * @author dannyroest@gmail.com (Danny Roest)
 */
public class InputValue {

	private long id;
	private String value;
	private boolean checked = false;
	// MGIMENEZ: Jatyta FormValueType
	private FormValueType type = FormValueType.UNKNOWN;

	/**
	 * default constructor.
	 */
	public InputValue() {

	}

	/**
	 * @param value
	 *            the text value
	 */
	public InputValue(String value) {
		this(value, true);
	}

	/**
	 * Created a form input value.
	 * 
	 * @param value
	 *            the text value
	 * @param checked
	 *            whether the element should be checked
	 */
	public InputValue(String value, boolean checked) {
		this.value = value;
		this.checked = checked;
	}

	/**
	 * Created a form input value.
	 * 
	 * @param value
	 *            the text value
	 * @param checked
	 *            whether the element should be checked
	 * @param type
	 *            The FormValueType.
	 */
	public InputValue(String value, boolean checked, FormValueType type) {
		this.value = value;
		this.checked = checked;
		this.type = type;
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	// MGIMENEZ: add new getters and setters.
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

}
