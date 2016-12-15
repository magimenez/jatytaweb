/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.validation;

import java.util.ArrayList;
import java.util.List;

import com.crawljax.web.jatyta.model.entities.JatytaValidationConfiguration;

/**
 * Used to configure the {@link ValidationUtils} instance.
 * 
 * @author mgimenez
 * 
 */
public class ValidationConfiguration {

	/**
	 * The {@link List} of {@link JatytaValidationConfiguration} to search the
	 * validation messages.
	 */
	private List<JatytaValidationConfiguration> validationElements = new ArrayList<JatytaValidationConfiguration>();

	/**
	 * The default constructor, if validationElements is null, they used the
	 * default values.
	 * 
	 * @param validationElements
	 *            The list of {@link JatytaValidationConfiguration} used to find
	 *            validation messages.
	 */
	public ValidationConfiguration(
			List<JatytaValidationConfiguration> validationElements) {
		super();
		if (validationElements != null && !validationElements.isEmpty()) {
			this.validationElements = validationElements;
		}
	}

	/**
	 * @return the validationElements
	 */
	public List<JatytaValidationConfiguration> getValidationElements() {
		return validationElements;
	}

}
