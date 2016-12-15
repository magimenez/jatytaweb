package com.crawljax.forms;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.eventablecondition.EventableCondition;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.state.Identification;
import com.crawljax.util.DomUtils;
import com.crawljax.util.XPathHelper;
import com.crawljax.web.jatyta.comparator.JatytaStateComparator;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseSearchCriteria;
import com.crawljax.web.jatyta.plugins.AutomationFormInputValuesPlugin;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;
import com.crawljax.web.jatyta.utils.MathUtils;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;

/**
 * Helper class for FormHandler.
 */
public final class FormInputValueHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(FormInputValueHelper.class.getName());

	private ImmutableMap<String, String> formFields;
	private ImmutableListMultimap<String, String> formFieldNames;
	private ImmutableListMultimap<String, String> fieldValues;
	private AutomationFormInputValuesPlugin plugin;

	private boolean randomInput;

	private String[] defaultBooleanValues = { "true", "false", "" };

	private static final int EMPTY = 0;
	
	//mgimenez 06/09/2016, for reference of FormValueType
	private Map<String, FormValueType> valueTypes = new HashMap<String, FormValueType>();

	/**
	 * @param inputSpecification
	 *            the input specification.
	 * @param randomInput
	 *            if random data should be used on the input fields.
	 */
	public FormInputValueHelper(InputSpecification inputSpecification, boolean randomInput) {

		this.plugin = inputSpecification.getPlugin();
		this.formFieldNames = inputSpecification.getFormFieldNames();
		this.fieldValues = inputSpecification.getFormFieldValues();

		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		for (Entry<String, String> entry : formFieldNames.entries()) {
			builder.put(entry.getValue(), entry.getKey());
		}
		formFields = builder.build();

		this.randomInput = randomInput;
		
		this.valueTypes = inputSpecification.getValueTypes();
	}

	/**
	 * Updated for obtain the element with the id or name of the fieldName
	 * parameter.
	 * 
	 * @param dom
	 *            The {@link Document} to search the element.
	 * @param fieldName
	 *            The id or name of the element
	 * @return The {@link Element} with the id or name of fieldName.
	 */
	private Element getBelongingElement(Document dom, String fieldName) {
		// mgimenez: 28/08/2015, modificado para obtener los elementos de los
		// input que no estan definidos en la KB.
		// List<String> names = getNamesForInputFieldId(fieldName);
		List<String> names = new ArrayList<String>();
		names.add(fieldName);

		if (names != null) {
			for (String name : names) {
				String xpath = "//*[@name='" + name + "' or @id='" + name + "']";
				try {
					Node node = DomUtils.getElementByXpath(dom, xpath);
					if (node != null) {
						return (Element) node;
					}
				} catch (XPathExpressionException e) {
					LOGGER.debug(e.getLocalizedMessage(), e);
					// just try next
				}
			}
		}
		return null;
	}

	private int getMaxNumberOfValuesOld(List<String> fieldNames) {
		int maxValues = 0;
		// check the maximum number of form inputValues
		for (String fieldName : fieldNames) {
			List<String> values = getValuesForName(fieldName);
			if (values != null && values.size() > maxValues) {
				maxValues = values.size();
			}
		}
		return maxValues;
	}
	
	private int getMaxNumberOfValues(List<String> fieldNames) {
		
		return 4;
	}

	/**
	 * @param browser
	 *            the browser instance
	 * @param sourceElement
	 *            the form elements
	 * @param eventableCondition
	 *            the belonging eventable condition for sourceElement
	 * @return a list with Candidate elements for the inputs
	 */
	public List<CandidateElement> getCandidateElementsForInputs(EmbeddedBrowser browser, Element sourceElement,
			EventableCondition eventableCondition) {
		List<CandidateElement> candidateElements = new ArrayList<CandidateElement>();
		int maxValues = getMaxNumberOfValues(eventableCondition.getLinkedInputFields());
		if (maxValues == EMPTY) {
			LOGGER.warn("No input values found for element: " + DomUtils.getElementString(sourceElement));
			return candidateElements;
		}

		Document dom;
		try {
			dom = DomUtils.asDocument(browser.getStrippedDomWithoutIframeContent());
		} catch (IOException e) {
			LOGGER.error("Catched IOException while parsing dom", e);
			return candidateElements;
		}
		
		//TODO form node for eventable
		Node formNodeEventable = getFirstFormParent(sourceElement);
		
		if(formNodeEventable!=null){
			//only if the eventable element has a parent form.

			// add maxValues Candidate Elements for every input combination
			for (int curValueIndex = 0; curValueIndex < maxValues; curValueIndex++) {
				List<FormInput> formInputsForCurrentIndex = new ArrayList<FormInput>();
	
				// List<String> linkedInputFields =
				// eventableCondition.getLinkedInputFields();
				List<String> linkedInputFields = getInputNames(dom);
				for (String fieldName : linkedInputFields) {
					Element element = getBelongingElement(dom, fieldName);
					if (element != null) {
						//mgimenez 10/09/2016
						//TODO verificar si el elemento se encuentra en el mismo form del
						//eventable
						Node formNode = getFirstFormParent(element);
						if(formNode!=null && compareNodeByAllAttributes(formNodeEventable, formNode)){
							//mgimenez 11/09/2016
							//if the element has a form parent and is equal to the form of the eventable.
							FormInput formInput = getFormInputWithIndexValue(browser, element, curValueIndex);
							formInputsForCurrentIndex.add(formInput);	
						}
					} else {
						List<String> names = getNamesForInputFieldId(fieldName);
						LOGGER.warn("Could not find input element for: " + fieldName + " - " + names);
					}
				}
	
				String id = eventableCondition.getId() + "_" + curValueIndex;
				sourceElement.setAttribute("atusa", id);
	
				// clone node inclusive text content
				Element cloneElement = (Element) sourceElement.cloneNode(false);
				cloneElement.setTextContent(DomUtils.getTextValue(sourceElement));
	
				CandidateElement candidateElement = new CandidateElement(cloneElement,
						XPathHelper.getXPathExpression(sourceElement), formInputsForCurrentIndex);
				//mgimenez: set de state title name
				String title = JatytaStateComparator.getDOMTitleName(browser, plugin.TITLE_UNDER_XPATH);
				if(title!=null){
					candidateElement.setStateTitleName(title);	
				}else{
					//si no encuentra el titulo, utiliza el url
					candidateElement.setStateTitleName(browser.getCurrentUrl());
				}
				candidateElements.add(candidateElement);
			}
		}
		return candidateElements;
	}

	/**
	 * @param input
	 *            the form input
	 * @param dom
	 *            the document
	 * @return returns the belonging node to input in dom
	 * @throws XPathExpressionException
	 *             if a failure is occurred.
	 */
	public Node getBelongingNode(FormInput input, Document dom) throws XPathExpressionException {

		Node result = null;

		switch (input.getIdentification().getHow()) {
		case xpath:
			result = DomUtils.getElementByXpath(dom, input.getIdentification().getValue());
			break;

		case id:
		case name:
			String xpath = "";
			String element = "";

			if (input.getType().equalsIgnoreCase("select") || input.getType().equalsIgnoreCase("textarea")) {
				element = input.getType().toUpperCase();
			} else {
				element = "INPUT";
			}
			xpath = "//" + element + "[@name='" + input.getIdentification().getValue() + "' or @id='"
					+ input.getIdentification().getValue() + "']";
			result = DomUtils.getElementByXpath(dom, xpath);
			break;

		default:
			LOGGER.info("Identification " + input.getIdentification() + " not supported yet for form inputs.");
			break;

		}

		return result;
	}

	/**
	 * @param element
	 * @return returns the id of the element if set, else the name. If none
	 *         found, returns null
	 * @throws Exception
	 */
	private Identification getIdentification(Node element) throws Exception {
		NamedNodeMap attributes = element.getAttributes();
		if (attributes.getNamedItem("id") != null) {
			return new Identification(Identification.How.id, attributes.getNamedItem("id").getNodeValue());
		} else if (attributes.getNamedItem("name") != null) {
			return new Identification(Identification.How.name, attributes.getNamedItem("name").getNodeValue());
		}

		// try to find the xpath
		String xpathExpr = XPathHelper.getXPathExpression(element);
		if (xpathExpr != null && !xpathExpr.equals("")) {
			return new Identification(Identification.How.xpath, xpathExpr);
		}

		return null;
	}

	/**
	 * @param browser
	 *            the current browser instance
	 * @param element
	 *            the element in the dom
	 * @return the first related formInput belonging to element in the browser
	 */
	public FormInput getFormInputWithDefaultValue(EmbeddedBrowser browser, Node element) {
		return getFormInput(browser, element, 0);
	}

	/**
	 * @param browser
	 *            the current browser instance
	 * @param element
	 *            the element in the dom
	 * @param indexValue
	 *            the i-th specified value. if i>#values, first value is used
	 * @return the specified value with index indexValue for the belonging
	 *         elements
	 */
	public FormInput getFormInputWithIndexValue(EmbeddedBrowser browser, Node element, int indexValue) {
		return getFormInput(browser, element, indexValue);
	}

	// mgimenez: modificar este metodo, debe diferenciar entre white box y black
	// box
	// al obtener el id del campo.
	private FormInput getFormInputOld(EmbeddedBrowser browser, Node element, int indexValue) {
		Identification identification;
		try {
			identification = getIdentification(element);
			if (identification == null) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		// CHECK
		FormInput input = new FormInput();
		input.setType(getElementType(element));
		input.setIdentification(identification);

		// if the random values is enabled.
		if (this.randomInput) {
			return browser.getInputWithRandomValue(input);
		}

		KnowledgeBaseSearchCriteria searchCriteria = plugin.constructSearchCriteria(element);

		Set<InputValue> values = new HashSet<InputValue>();
		String stringValue = "";
		String id = fieldMatches(searchCriteria);

		// null if the field is not in the knowledge base.
		if (id != null) {

			Set<InputValue> whiteBoxvalues = new HashSet<InputValue>();
			Set<InputValue> blackBoxvalues = new HashSet<InputValue>();

			if (plugin.getWHITEBOX_TEST()) {
				// white box test
				// mgimenez 09/06/2016 retorna todos los valores obtenidos de la
				// base de conocimiento.

				whiteBoxvalues.addAll(plugin.obtainValuesWithSearchCriteria(searchCriteria));

			} else if (fieldValues.containsKey(id)) {

				// black box test

				// TODO: make multiple selection for list available
				// add defined value to element
				// mgimenez 09/06/2016 retorna todos los valores obtenidos de la
				// base de conocimiento.

				for (String string : fieldValues.get(id)) {
					InputValue inputValue = new InputValue(string, false, FormValueType.UNKNOWN);
					blackBoxvalues.add(inputValue);
				}

			}

			if (input.getType().equals("checkbox") || input.getType().equals("radio")) {
				// mgimenez 09/06/2016 retorna todos los valores obtenidos por
				// whitebox o blackbox.
				Set<InputValue> checkValues = new HashSet<InputValue>();
				checkValues.addAll(blackBoxvalues);
				checkValues.addAll(whiteBoxvalues);
				for (InputValue inputValue : checkValues) {
					// check element
					inputValue.setChecked(inputValue.getValue().equals("1"));
					values.add(inputValue);
				}
			}else{
				values.addAll(blackBoxvalues);
				values.addAll(whiteBoxvalues);
			}
		} else {

			// When a field dont have a value in the knowledge base.
			// field is not specified, lets try the defaults values.
			if (input.getType().equals("checkbox") || input.getType().equals("radio")) {

				if (indexValue == 0 || defaultBooleanValues.length == 1
						|| indexValue + 1 > defaultBooleanValues.length) {
					// default value
					stringValue = defaultBooleanValues[0];
				} else if (indexValue > 0) {
					// index value
					stringValue = defaultBooleanValues[indexValue];
				} else {
					// random value
					stringValue = defaultBooleanValues[new Random().nextInt(defaultBooleanValues.length - 1)];
				}

				// check element
				values.add(new InputValue(stringValue, stringValue.equals("1"), FormValueType.UNKNOWN));
			} else {

				if (indexValue < 7) {
					// text or number or null(empty)
					//stringValue = getDefaultValue(indexValue);
					stringValue = plugin.getTestValue(indexValue);
				} else {
					// random
					
					//stringValue = getDefaultValue(new Random().nextInt(3));
					stringValue = plugin.getTestValue(new Random().nextInt(7));
				}

				// set value of text input field
				values.add(new InputValue(stringValue, true, FormValueType.UNKNOWN));
			}
		}

		input.setInputValues(values);

		return input;
	}

	private FormInput getFormInput(EmbeddedBrowser browser, Node element, int indexValue) {
		Identification identification;
		try {
			identification = getIdentification(element);
			if (identification == null) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		
		KnowledgeBaseSearchCriteria searchCriteria = plugin.constructSearchCriteria(element);
		// CHECK
		String id = fieldMatches(searchCriteria);
		FormInput input = new FormInput();
		input.setType(getElementType(element));
		input.setIdentification(identification);
		
		//si es blackbox
		if(!plugin.getWHITEBOX_TEST()){
			input.setInputValues(generateValues(input, browser, indexValue));
		}else{
			//si es whitebox, obtiene de la base de conocimiento.
			if (id != null && fieldValues.containsKey(id)) {
				// TODO: make multiple selection for list available
				// add defined value to element
				String value;
				Set<InputValue> values = new HashSet<InputValue>();
				FormValueType type;
				if (indexValue == 0 || fieldValues.get(id).size() == 1
				        || indexValue + 1 > fieldValues.get(id).size()) {
					// default value
					value = fieldValues.get(id).get(0);
				} else if (indexValue > 0) {
					// index value
					value = fieldValues.get(id).get(indexValue);
				} else {
					// random value
					value =
					        fieldValues.get(id).get(
					                new Random().nextInt(fieldValues.get(id).size() - 1));
				}
	
				type= getFormValueType(id, value);
				 
				if (input.getType().equals("checkbox") || input.getType().equals("radio")) {
					// check element
					values.add(new InputValue(value, value.equals("1"), type));
				} else {
					// set value of text input field
					values.add(new InputValue(value, true, type));
				}
				input.setInputValues(values);
				
			}else{
				//si es whitebox y no se encuentra el field. genera el valor.
				input.setInputValues(generateValues(input, browser, indexValue));
			}
		}
		return input;
	}
	
	/**
	 * Generates random values for the parameters.
	 * @param input
	 * @param browser
	 * @param indexValue
	 * @return
	 */
	private Set<InputValue> generateValues(FormInput input, 
			EmbeddedBrowser browser, int indexValue){
		
		Set<InputValue> values = new HashSet<InputValue>();
		FormValueType type;
		
		if (this.randomInput) {
			 return browser.getInputWithRandomValue(input).getInputValues();
		}else{
			String value;
			switch (indexValue) {
			case 0:
				//empty value
				value="";
				type= FormValueType.UNKNOWN;
				break;
			case 1:
				//overflow value
				value = plugin.getTestValue(1);
				type= FormValueType.UNKNOWN;
				break;

			 default: 
				//random text, number, date
			 	value = getDefaultValue(indexValue%3);
			 	type= FormValueType.UNKNOWN;
			 	break;
			}				
			values.add(new InputValue(value, true, type));
		}
		
		return values;
		
	}
	
	/**
	 * Don't used for Jatyta.
	 * 
	 * @param fieldName
	 *            The fieldname
	 * @return The fieldname associated.
	 */
	@Deprecated
	private String fieldMatches(String fieldName) {
		for (String field : formFields.keySet()) {
			Pattern p = Pattern.compile(field, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(fieldName);
			if (m.matches()) {
				return formFields.get(field);
			}
		}
		return null;
	}

	/**
	 * Obtain the field Name obtained from {@link KnowledgeBaseSearchCriteria}
	 * itemprop value.
	 * 
	 * @param searchCriteria
	 *            The {@link KnowledgeBaseSearchCriteria} build with the
	 *            attributes of the form element.
	 * @return The Fieldname defined in the formFields.
	 */
	private String fieldMatches(KnowledgeBaseSearchCriteria searchCriteria) {

		String itempropValue = searchCriteria.getItemProp();
		if (itempropValue != null && !itempropValue.isEmpty()) {

			for (String field : formFields.keySet()) {
				Pattern p = Pattern.compile(field, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(itempropValue);
				if (m.matches()) {
					return formFields.get(field);
				}
			}
		}
		return null;
	}

	private List<String> getValuesForName(String inputFieldId) {
		if (!fieldValues.containsKey(inputFieldId)) {
			return null;
		}
		return fieldValues.get(inputFieldId);
	}

	private List<String> getNamesForInputFieldId(String inputFieldId) {
		if (!formFieldNames.containsKey(inputFieldId)) {
			return null;
		}
		return formFieldNames.get(inputFieldId);
	}

	private String getElementType(Node node) {
		if (node.getAttributes().getNamedItem("type") != null) {
			return node.getAttributes().getNamedItem("type").getNodeValue().toLowerCase();
		} else if (node.getNodeName().equalsIgnoreCase("input")) {
			return "text";
		} else {
			return node.getNodeName().toLowerCase();
		}
	}

	/**
	 * Return the {@link AutomationFormInputValuesPlugin} associated to the
	 * crawl.
	 * 
	 * @return the plugin
	 */
	public AutomationFormInputValuesPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Return the list of id or name input elements.
	 * 
	 * @param dom
	 *            The {@link Document} that represents the DOM.
	 * @return all input element in dom
	 */
	private List<String> getInputNames(Document dom) {
		List<String> nodes = new ArrayList<String>();
		try {
			NodeList nodeList = XPathHelper.evaluateXpathExpression(dom, "//INPUT");
			List<String> allowedTypes = new ArrayList<String>(Arrays.asList(FormHandler.ALLOWED_INPUT_TYPES));

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node candidate = nodeList.item(i);
				Node typeAttribute = candidate.getAttributes().getNamedItem("type");
				if (typeAttribute == null || (allowedTypes.contains(typeAttribute.getNodeValue()))) {
					addInputNodeNames(nodes, nodeList.item(i));
				}
			}
			nodeList = XPathHelper.evaluateXpathExpression(dom, "//TEXTAREA");
			for (int i = 0; i < nodeList.getLength(); i++) {
				addInputNodeNames(nodes, nodeList.item(i));
			}
			nodeList = XPathHelper.evaluateXpathExpression(dom, "//SELECT");
			for (int i = 0; i < nodeList.getLength(); i++) {
				addInputNodeNames(nodes, nodeList.item(i));
			}
			return nodes;
		} catch (XPathExpressionException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return nodes;
	}

	/**
	 * Add the id value of the {@link Node} to the list parameter. If the id
	 * attribute is abscent, add de value of the name attribute.
	 * 
	 * @param list
	 *            The {@link List} parameter.
	 * @param node
	 *            The {@link Node} to obtain the id.
	 * 
	 */
	private List<String> addInputNodeNames(List<String> list, Node node) {

		String name = "";
		Node nodeId = node.getAttributes().getNamedItem("id");
		if (nodeId != null) {
			name = nodeId.getNodeValue();
		} else {
			Node nodeName = node.getAttributes().getNamedItem("name");
			if (nodeName != null) {
				name = nodeName.getNodeValue();
			}
		}
		if (name != null && !name.isEmpty()) {
			list.add(name);
		}
		return list;
	}

	/**
	 * Return a random value of Text, Number or null(empty)
	 * 
	 * @author mgimenez
	 * @param index
	 *            the format of the value. Text(index = 0), Number(index = 1) or
	 *            date(index = 2). Otherwise empty String.
	 * @return The value generated in random mode.
	 */
	private String getDefaultValue(int index) {
		String value = "";
		switch (index) {
		case 0:
			// text
			value = new RandomInputValueGenerator().getRandomString(this.plugin.getMAX_TEXT_SIZE());
			break;
		case 1:
			// number
			Random random = new Random();
			long longValue = MathUtils.nextLong(random, (long) Math.pow(10, this.plugin.getMAX_NUMBER_SIZE()));
			value = Long.toString(longValue);
			break;
		case 2:
			// Date
			Date date = new Date(System.currentTimeMillis());
			// hacer el pattern como parametro de configuracion
			SimpleDateFormat sdf = new SimpleDateFormat(this.plugin.getDATE_VALUE_PATTERN());
			value = sdf.format(date);
			break;
		}
		return value;
	}
	

	/**
	 * Return the FormValueType for the fieldName and value parameters.
	 * @param fieldName The fieldName
	 * @param value The value
	 * @return The FormValueType, FormValueType.UNKNOWN by default.
	 */
	public FormValueType getFormValueType(String fieldName, String value){
		FormValueType result =  FormValueType.UNKNOWN;
		String key =  fieldName+InputSpecification.SEPARATOR+value;
		if(valueTypes.containsKey(key)){
			return valueTypes.get(key);
		}
		return result;
	}
	
	/**
	 * Return the {@link Node} form the first Form parent of the node parameter.
	 * @param element The node parameter.
	 * @return The Node of the form parent, null if dont exists.
	 */
	public Node getFirstFormParent(Node node){
		
		if(HtmlElement.FORM.getValue().equals(node.getNodeName())){
			//si es un form
			return node;
		}else if (node.getParentNode()!=null){
			//si tiene parents, recursividad
			return getFirstFormParent(node.getParentNode());
		}else{
			//no tiene parent, entonces no hay form padre.
			return null;
		}
	}
	
	/**
	 * Compare two nodes with the id attribute value.
	 * @param source One Node.
	 * @param target The other Node.
	 * @return
	 */
	public boolean compareNodeById(Node source, Node target){
		
		boolean result = false;
		Node sourceId = source.getAttributes().getNamedItem("id");
		Node targetId = target.getAttributes().getNamedItem("id");
		
		if(sourceId!=null && targetId!=null &&
				sourceId.getNodeValue().equals(targetId.getNodeValue())){
			result = true;
		}
	
		return result;
	}
	
	/**
	 * Compare two nodes with the every attribute value.
	 * @param source One Node.
	 * @param target The other Node.
	 * @return true, if both have the same attributes and values. 
	 * False otherwise.
	 */
	public boolean compareNodeByAllAttributes(Node source, Node target){

		for (int i = 0; i < source.getAttributes().getLength(); i++) {
			Node sourceAttr = source.getAttributes().item(i);
			Node targetAttr = target.getAttributes().
					getNamedItem(sourceAttr.getNodeName());
			if(targetAttr==null || 
				!sourceAttr.getNodeValue().equals(targetAttr.getNodeValue())){
				//are different nodes.
				return false;
			}
		}
		return true;
	}
}
