package com.crawljax.core.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.model.entities.JatytaFormConfiguration;
import com.crawljax.web.jatyta.plugins.AutomationFormInputValuesPlugin;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;

/**
 * Specifies values for form input fields The user specifies the ids or names of
 * the input fields. When Crawljax enters a new state it scans the DOM for input
 * fields and tries to match the field ids/names to the specified input fields.
 * When there is a match, Crawljax enters the specified value EXAMPLE: HTML:
 * <code>
 * Name: <input type="text" id="name" /><br />
 * Phone: <input name="phone" /><br />
 * Mobile: <input name="mobile" /><br />
 * Agree to licence: <input id="agreelicence" type="checkbox" /><br />
 * Other:content
 * </code> JAVA: <code>
 * InputSpecification input = new InputSpecification();
 * //for fields with text
 * input.field("name").setValue("John Doe");
 * input.field("phone", "mobile").setValue("1234567890");
 * //for checkboxes
 * input.field("agreelicence").setValue(true);
 * </code> Crawljax will set Name, Phone, Mobile, and Agree values. It will
 * enter a random string in the Other field if enabled in
 * {@link CrawljaxConfiguration}
 */
public final class InputSpecification {

	private static final Logger LOG = LoggerFactory
			.getLogger(InputSpecification.class);
	private final List<InputField> inputFields = Lists.newLinkedList();
	private final List<Form> forms = Lists.newLinkedList();
	private AutomationFormInputValuesPlugin plugin;
	//mgimenez 06/09/2016, for reference of FormValueType
	private final Map<String, FormValueType> valueTypes = new HashMap<String, FormValueType>();
	public static final String SEPARATOR = "::";

	/**
	 * Default constructor.
	 */
	public InputSpecification() {
		super();
	}

	/**
	 * Constructor for BLACKBOX Testing, obtain the forms inputs and values from
	 * Knowledge Base
	 * 
	 * @param plugin
	 *            The {@link AutomationFormInputValuesPlugin} associated to the
	 *            crawl.
	 */
	public void InputSpecificationOld(AutomationFormInputValuesPlugin plugin) {
		this.plugin = plugin;

		try {
			Map<String, List<String>> propnames = plugin
					.getAllPropNamesAndValuesByFilter();
			Form form = new Form();
			for (String key : propnames.keySet()) {
				Object[] objList = propnames.get(key).toArray();
				Integer resultSize = objList.length;
				if (plugin.getMAX_VALUES_PER_FORM_FIELD() > 0
						&& resultSize > plugin.getMAX_VALUES_PER_FORM_FIELD()) {
					resultSize = plugin.getMAX_VALUES_PER_FORM_FIELD();
				}
				String[] values = new String[resultSize];
				for (int i = 0; i < resultSize; i++) {
					if (objList[i] != null) {
						values[i] = (String) objList[i];	
					} else {
						values[i] = "";	
					}
					
				}
				form.field(key).setValues(values);
				LOG.debug(" Values of field {} = {}.", key, values);
			}


			if (plugin.getFORM_SUBMIT_ATTRIBUTE() != null
					&& !plugin.getFORM_SUBMIT_ATTRIBUTE().isEmpty()
					&& plugin.getFORM_SUBMIT_ATTRIBUTE_VALUE() != null
					&& !plugin.getFORM_SUBMIT_ATTRIBUTE_VALUE().isEmpty()) {
				setValuesInForm(form).beforeClickElement(plugin
						.getFORM_SUBMIT_ELEMENT()).withAttribute(plugin.getFORM_SUBMIT_ATTRIBUTE(),
						plugin.getFORM_SUBMIT_ATTRIBUTE_VALUE());
			}
			if (plugin.getFORM_SUBMIT_TEXT() != null
					&& !plugin.getFORM_SUBMIT_TEXT().isEmpty()) {
				setValuesInForm(form).beforeClickElement(plugin
						.getFORM_SUBMIT_ELEMENT()).withText(plugin.getFORM_SUBMIT_TEXT());
			}
			
		} catch (JatytaException e) {
			LOG.error(e.getLocalizedMessage());
		}

	}
	
	/**
	 * Set the field and values for the parameters
	 * @param form 
	 * @param key
	 * @param values
	 * @return
	 */
	public void generateFormValuesForEntry(Map.Entry<String,List<String>> entry, 
			Map<String, List<String>> propnamesInvalid, String[] values){
		Object[] objList = entry.getValue().toArray();
		
		Integer resultSize = objList.length;
		if (plugin.getMAX_VALUES_PER_FORM_FIELD() > 0
				&& resultSize > plugin.getMAX_VALUES_PER_FORM_FIELD()) {
			resultSize = plugin.getMAX_VALUES_PER_FORM_FIELD();
		}

		
		//primero vacio
		values[0] = "";
		
		//segundo texto extremo
		values[1] = plugin.getTestValue(1);
		
		//tercero valido
		values[2]= (String)objList[0];
		//cuarto invalido
		if(propnamesInvalid.containsKey(entry.getKey())){
			List<String> listInv = propnamesInvalid.get(entry.getKey());
			values[3] = listInv.get(0);
			
		}else{
			values[3] = plugin.getTestValue(1);
		}
		if(values[3]==null){
			values[3] = "";
		}
		if(values[2]==null){
			values[2] = "";
		}
	}
	
	/**
	 * Create a form with the form action generated from 
	 * {@link JatytaFormConfiguration} parameter
	 * @param jatytaForm the parameter.
	 * @return A new Form.
	 */
	public Form createFormWithJatytaConfigurarion(JatytaFormConfiguration jatytaForm){
		
		FormAction action = new FormAction();
		if (jatytaForm.getFormSubmitAttribute() != null
				&& !jatytaForm.getFormSubmitAttribute().isEmpty()
				&& jatytaForm.getFormSubmitAttributeValue() != null
				&& !jatytaForm.getFormSubmitAttributeValue().isEmpty()) {
			
			action.beforeClickElement(jatytaForm
					.getFormSubmitElement()).withAttribute(
							jatytaForm.getFormSubmitAttribute(),
							jatytaForm.getFormSubmitAttributeValue());
		}
		if (jatytaForm.getFormSubmitText() != null
				&& !jatytaForm.getFormSubmitText().isEmpty()) {
			action.beforeClickElement(jatytaForm.getFormSubmitElement()).
			withText(jatytaForm.getFormSubmitText());
		}
		Form form = new Form();
		form.setFormAction(action);
		return form;
	}
	
	/**
	 * Set the valuesType for the id and values parameters.
	 * @param id
	 * @param values
	 */
	public void setValuesTypes(String id, String[] values){
		valueTypes.put(id+SEPARATOR+values[0], FormValueType.EMPTY);
		valueTypes.put(id+SEPARATOR+values[1], FormValueType.OVERFLOW);
		if(values[2]==null){
			valueTypes.put(id+SEPARATOR+values[2], FormValueType.EMPTY);	
		}else{
			valueTypes.put(id+SEPARATOR+values[2], FormValueType.VALID);	
		}
		if(values[3]==null){
			valueTypes.put(id+SEPARATOR+values[3], FormValueType.EMPTY);	
		}else{
			valueTypes.put(id+SEPARATOR+values[3], FormValueType.INVALID);	
		}
	}
	
	/**
	 * Constructor for BLACKBOX Testing, obtain the forms inputs and values from
	 * Knowledge Base
	 * 
	 * @param plugin
	 *            The {@link AutomationFormInputValuesPlugin} associated to the
	 *            crawl.
	 */
	public InputSpecification(AutomationFormInputValuesPlugin plugin) {
		this.plugin = plugin;

		try {
			List<JatytaFormConfiguration> jatytaForms =  
					plugin.getFormConfigurationList();
			
			if (!jatytaForms.isEmpty()){
				for (JatytaFormConfiguration jatytaFormConfiguration : jatytaForms) {
					//create forms
					Form form = createFormWithJatytaConfigurarion(jatytaFormConfiguration);
					this.forms.add(form);
				}
				Map<String, List<String>> propnamesValid = plugin
						.getAllPropNamesAndValuesValid();
				Map<String, List<String>> propnamesInvalid = plugin
						.getAllPropNamesAndValuesInvalid();
				
				for (Map.Entry<String,List<String>> entry: propnamesValid.entrySet()) {
					String[] values = new String[4];
					
					generateFormValuesForEntry(entry, propnamesInvalid, values);
					
					//for every form created,
					for (Form form : this.forms) {
						String id = form.field(entry.getKey()).setValues(values).getId();
						
						setValuesTypes(id, values);
						
						LOG.debug(" Values of field {} = {}.", entry.getKey(), values); 
					}	
				}			
			}
		} catch (JatytaException e) {
			LOG.error(e.getLocalizedMessage());
		}

	}

	/**
	 * Specifies an input field to assign a value to. Crawljax first tries to
	 * match the found HTML input element's id and then the name attribute.
	 * 
	 * @param fieldName
	 *            the id or name attribute of the input field
	 * @return an InputField
	 */
	public InputField field(String fieldName) {
		InputField inputField = new InputField();
		inputField.setFieldName(fieldName);
		this.inputFields.add(inputField);
		return inputField;
	}

	/**
	 * Specifies input fields to assign one value to. Crawljax first tries to
	 * match the found HTML input element's id and then the name attribute.
	 * 
	 * @param fieldNames
	 *            the ids or names of the input fields
	 * @return an InputField
	 */
	public InputField fields(String... fieldNames) {
		InputField inputField = new InputField();
		inputField.setFieldNames(fieldNames);
		this.inputFields.add(inputField);
		return inputField;
	}

	/**
	 * Links the form with an HTML element which can be clicked.
	 * 
	 * @see Form
	 * @param form
	 *            the collection of the input fields
	 * @return a FormAction
	 */
	public FormAction setValuesInForm(Form form) {
		FormAction formAction = new FormAction();
		form.setFormAction(formAction);
		this.forms.add(form);
		return formAction;
	}

	// hidden
	// TODO sobrescribir este metodo para que obtenga los valores desde la KB.
	public ImmutableListMultimap<String, String> getFormFieldNames() {
		ImmutableListMultimap.Builder<String, String> formFieldNames = ImmutableListMultimap
				.builder();
		//a map to check the repeated fields.
		Map<String, List<String>> mapFieldNames = new HashMap<String, List<String>>();
		for (Form form : this.forms) {
			for (FormInputField inputField : form.getInputFields()) {
				if(!mapFieldNames.containsValue(inputField.getFieldNames())){
					mapFieldNames.put(inputField.getId(),
					inputField.getFieldNames());	
				}
				
			}
		}
		for (InputField inputField : inputFields) {
			if(!mapFieldNames.containsValue(inputField.getFieldNames())){
				mapFieldNames.put(inputField.getId(),
				inputField.getFieldNames());	
			}
			
		}
		//only the not repeated field are set.
		for (Entry<String, List<String>> entryMap : mapFieldNames.entrySet()){
			formFieldNames.putAll(entryMap.getKey(),
					entryMap.getValue());	
		}
		
		return formFieldNames.build();
	}

	public ImmutableListMultimap<String, String> getFormFieldValues() {
		ImmutableListMultimap.Builder<String, String> formFieldNames = ImmutableListMultimap
				.builder();
		for (Form form : this.forms) {
			for (FormInputField inputField : form.getInputFields()) {
				formFieldNames.putAll(inputField.getId(),
						inputField.getFieldValues());
			}
		}
		for (InputField inputField : inputFields) {
			formFieldNames.putAll(inputField.getId(),
					inputField.getFieldValues());
		}
		return formFieldNames.build();
	}

	/**
	 * @return List of crawlelements.
	 */
	public ImmutableList<CrawlElement> getCrawlElements() {
		Builder<CrawlElement> builder = ImmutableList.builder();
		for (Form form : this.forms) {
			CrawlElement crawlTag = form.getCrawlElement();
			if (crawlTag != null) {
				builder.add(crawlTag);
			}
		}
		return builder.build();
	}

	/**
	 * Return the {@link AutomationFormInputValuesPlugin} associated.
	 * 
	 * @return the plugin
	 */
	public AutomationFormInputValuesPlugin getPlugin() {
		return plugin;
	}

	/**
	 * @return the valueTypes
	 */
	public Map<String, FormValueType> getValueTypes() {
		return valueTypes;
	}
	


}
