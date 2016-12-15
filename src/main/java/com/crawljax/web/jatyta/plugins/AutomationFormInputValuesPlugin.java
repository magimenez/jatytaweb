/**
 * 
 */
package com.crawljax.web.jatyta.plugins;

import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.HibernateException;
import org.jfree.util.Log;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.OnFireEventSuccessPlugin;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.OnUrlLoadPlugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.plugin.PreStateCrawlingPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.core.state.StateVertex;
import com.crawljax.forms.FormInput;
import com.crawljax.forms.InputValue;
import com.crawljax.forms.RandomInputValueGenerator;
import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBase;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseFactory;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseItem;
import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseSearchCriteria;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.PropValueDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaFormConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormValueRecordDAO;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaFormConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaFormFieldRecord;
import com.crawljax.web.jatyta.model.entities.JatytaFormValueRecord;
import com.crawljax.web.jatyta.model.entities.JatytaStateName;
import com.crawljax.web.jatyta.model.entities.JatytaValidationRecord;
import com.crawljax.web.jatyta.plugins.util.MicroDataNodeAttribute;
import com.crawljax.web.jatyta.plugins.util.SchemaUtils;
import com.crawljax.web.jatyta.plugins.util.dom.DOMUtils;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementAttribute;
import com.crawljax.web.jatyta.plugins.util.form.FormUtils;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;
import com.crawljax.web.jatyta.plugins.util.form.FormValuesInfo;
import com.crawljax.web.jatyta.plugins.util.form.FormValuesInfoDetail;
import com.crawljax.web.jatyta.plugins.util.jsf.JSFUtils;
import com.crawljax.web.jatyta.plugins.util.validation.ValidationConfiguration;
import com.crawljax.web.jatyta.plugins.util.validation.ValidationRecord;
import com.crawljax.web.jatyta.plugins.util.validation.ValidationUtils;
import com.crawljax.web.jatyta.utils.MathUtils;
import com.google.common.collect.ImmutableList;

/**
 * @author mgimenez
 * 
 */
public class AutomationFormInputValuesPlugin implements OnNewStatePlugin, OnFireEventSuccessPlugin, PostCrawlingPlugin,
		PreCrawlingPlugin, PreStateCrawlingPlugin, OnUrlLoadPlugin {

	private String DEFAULT_SCHEMA = null;
	private Boolean WHITEBOX_TEST = false;
	private Boolean AJUST_JSF_AUTO_GENERATED_ID = false;
	private Integer MAX_VALUES_PER_FORM_FIELD = 0;
	private String FORM_SUBMIT_ELEMENT = "";
	private String FORM_SUBMIT_ATTRIBUTE = "";
	private String FORM_SUBMIT_ATTRIBUTE_VALUE = "";
	private String FORM_SUBMIT_TEXT = "";
	private Integer MAX_TEXT_SIZE = 1;
	private Integer MAX_NUMBER_SIZE = 1;
	private String DATE_VALUE_PATTERN = "dd/MM/yyyy";

	private String LOGIN_USERNAME_XPATH = "";
	private String LOGIN_PASSWORD_XPATH = "";
	private String LOGIN_USERNAME_VALUE = "";
	private String LOGIN_PASSWORD_VALUE = "";
	private String LOGIN_SUBMIT_XPATH = "";

	private static final Logger LOG = LoggerFactory.getLogger(AutomationFormInputValuesPlugin.class);

	private List<ValidationRecord> listValidationRecords = new ArrayList<ValidationRecord>();

	/**
	 * The {@link List} of {@link MicroDataNodeAttribute} supported by the
	 * plugin.
	 */
	private static final MicroDataNodeAttribute[] microDataNodeAttributes = MicroDataNodeAttribute.values();

	/**
	 * A collection of info about data set in the inputs forms.
	 */
	private Map<String, FormValuesInfo> formsInputRevisited = new HashMap<>();

	/**
	 * 
	 */
	private FormValueType formValueType = null;

	/**
	 * The {@link ValidationUtils} used to validation management.
	 */
	private ValidationUtils validationUtils = null;

	/**
	 * The {@link JatytaService} to persistence KB data.
	 */
	private final JatytaService jatytaService;

	/**
	 * The crawl record Id.
	 */
	private final int crawlId;
	/**
	 * Crawljax Configuration.
	 */
	private String crawlConfigurationId;

	/**
	 * The count of states for the crawl. Default value ZERO.
	 */
	private Integer statesNumber = 0;

	/**
	 * The Xpath that defines where is the title of forms.
	 */
	public String TITLE_UNDER_XPATH = "";

	/**
	 * The list of {@link JatytaStateName}
	 */
	private List<JatytaStateName> stateNameList = new LinkedList<>();

	/**
	 * The tag to indentify the menu items.
	 */
	public String menuItemTag;
	
	/**
	 * The xpath to identify the menu container.
	 */
	public String menuUnderXpath;
	
	/**
	 * Constructor.
	 * 
	 * @param jatytaCrawlConfiguration
	 *            The needed {@link JatytaCrawlConfiguration} to run the plugin.
	 * @param validationConfiguration
	 *            The {@link ValidationConfiguration} to configure the
	 *            {@link ValidationUtils}.
	 * @param jatytaService
	 *            The {@link JatytaService} to conect to Data Base.
	 * @param crawlId
	 *            The Crawljax Record id.
	 */
	public AutomationFormInputValuesPlugin(JatytaCrawlConfiguration jatytaCrawlConfiguration,
			ValidationConfiguration validationConfiguration, JatytaService jatytaService, int crawlId) {

		this.validationUtils = new ValidationUtils(validationConfiguration);
		this.formValueType = FormValueType.valueOf(jatytaCrawlConfiguration.getFormValuesFilter());
		if (jatytaCrawlConfiguration.getWhiteBoxTest() != null) {
			this.WHITEBOX_TEST = jatytaCrawlConfiguration.getWhiteBoxTest();
		}
		this.MAX_VALUES_PER_FORM_FIELD = jatytaCrawlConfiguration.getMaxValuesForFormInput();

		this.jatytaService = jatytaService;
		// si schema name es distinto de ALL, se utiliza para el filtro.
		if (jatytaCrawlConfiguration.getSchema() != null) {
			this.DEFAULT_SCHEMA = jatytaCrawlConfiguration.getSchema().getSchemaName();
		}
		this.AJUST_JSF_AUTO_GENERATED_ID = jatytaCrawlConfiguration.getAjustJSFAutoGeneratedId();

		this.FORM_SUBMIT_ELEMENT = jatytaCrawlConfiguration.getFormSubmitElement();
		this.FORM_SUBMIT_ATTRIBUTE = jatytaCrawlConfiguration.getFormSubmitAttribute();
		this.FORM_SUBMIT_ATTRIBUTE_VALUE = jatytaCrawlConfiguration.getFormSubmitAttributeValue();
		this.FORM_SUBMIT_TEXT = jatytaCrawlConfiguration.getFormSubmitText();

		this.MAX_TEXT_SIZE = jatytaCrawlConfiguration.getMaxTextFieldSize();
		this.validationUtils.setMaxTextSize(MAX_TEXT_SIZE);
		this.MAX_NUMBER_SIZE = jatytaCrawlConfiguration.getMaxNumberFieldSize();

		this.crawlId = crawlId;

		this.TITLE_UNDER_XPATH = jatytaCrawlConfiguration.getTitleFormUnderXpath();
		if (jatytaCrawlConfiguration.getDateValuePattern() != null) {
			this.DATE_VALUE_PATTERN = jatytaCrawlConfiguration.getDateValuePattern();
		}
		// configuracion de login
		if (jatytaCrawlConfiguration.getLoginUserNameXpath() != null
				&& !jatytaCrawlConfiguration.getLoginUserNameXpath().trim().isEmpty()) {
			this.LOGIN_USERNAME_XPATH = jatytaCrawlConfiguration.getLoginUserNameXpath();
		}
		if (jatytaCrawlConfiguration.getLoginUserNameValue() != null
				&& !jatytaCrawlConfiguration.getLoginUserNameValue().trim().isEmpty()) {
			this.LOGIN_USERNAME_VALUE = jatytaCrawlConfiguration.getLoginUserNameValue();
		}
		if (jatytaCrawlConfiguration.getLoginPasswordXpath() != null
				&& !jatytaCrawlConfiguration.getLoginPasswordXpath().trim().isEmpty()) {
			this.LOGIN_PASSWORD_XPATH = jatytaCrawlConfiguration.getLoginPasswordXpath();
		}
		if (jatytaCrawlConfiguration.getLoginPasswordValue() != null
				&& !jatytaCrawlConfiguration.getLoginPasswordValue().trim().isEmpty()) {
			this.LOGIN_PASSWORD_VALUE = jatytaCrawlConfiguration.getLoginPasswordValue();
		}
		if (jatytaCrawlConfiguration.getLoginSubmitXpath() != null
				&& !jatytaCrawlConfiguration.getLoginSubmitXpath().trim().isEmpty()) {
			this.LOGIN_SUBMIT_XPATH = jatytaCrawlConfiguration.getLoginSubmitXpath();
		}
		this.crawlConfigurationId = jatytaCrawlConfiguration.getConfigurationId(); 

		this.menuItemTag = jatytaCrawlConfiguration.getMenuItemTag();
		this.menuUnderXpath = jatytaCrawlConfiguration.getMenuUnderXpath();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.core.plugin.PreCrawlingPlugin#preCrawling(com.crawljax.core
	 * .configuration.CrawljaxConfiguration)
	 */
	@Override
	public void preCrawling(CrawljaxConfiguration config) throws RuntimeException {
		LOG.info("Iniciando el recorrido de : {}", config.getUrl());
		formsInputRevisited = new HashMap<>();

	}

	/**
	 * Add the {@link FormInput} values to the formsInputRevisited.
	 * 
	 * @param node
	 *            The {@link Node} element.
	 * @param formInput
	 *            The {@link FormInput} to get the values.
	 * @param currentState
	 *            The current state.
	 * @param browser The {@link EmbeddedBrowser} to obtain {@link WebElement} and position.
	 */
	public void addFormInputValuesToInfo(Node node, FormInput formInput, 
			StateVertex currentState, EmbeddedBrowser browser) {
		String element = "";

		if (formInput.getType().equalsIgnoreCase(HtmlElement.SELECT.getValue())
				|| formInput.getType().equalsIgnoreCase(HtmlElement.TEXTAREA.getValue())) {
			element = formInput.getType().toUpperCase();
		} else {
			element = "INPUT";
		}

		// aqui se debe agregar el info del form value
		FormValuesInfo info;

		String xpathExpr = DOMUtils.generateXPathExpression(node, HtmlElement.valueOf(element));
		
		//mgimenez 13/06/2016 si no es visible se obvia el campo.
		//if (browser.isVisible(new Identification(How.xpath, xpathExpr))){
			if (formsInputRevisited.containsKey(xpathExpr)) {

				info = formsInputRevisited.get(xpathExpr);

			} else {
				// se crea el form value info
				info = new FormValuesInfo();
				info.setId(xpathExpr);
				//se obtiene el webelement para saber su posicion
				Identification ident = new Identification(How.xpath, xpathExpr);
				if(browser.elementExists(ident)){
					//si existe en el browser
					WebElement webElement = browser.getWebElement(ident);
					byte[] image = null;
					try {
						image = FormUtils.getElementScreenShot(webElement, browser);
						info.setImage(image);
						// se obtiene el label asociado al web element id.
						String id = webElement.getAttribute(
								HtmlElementAttribute.ID.getValue());
						if(id!=null && !id.isEmpty()){
							Identification labelIdent = new 
									Identification(How.xpath, "//label[@for='"+id+"']");
							if(browser.elementExists(labelIdent)){
								//si existe en el browser
								WebElement labelElement = browser.
										getWebElement(labelIdent);
								byte[] labelImage = null;
								labelImage = FormUtils.
										getElementScreenShot(labelElement, 
													browser);
								info.setLabelImage(labelImage);
							}
						}
					} catch (IOException |RasterFormatException e) {
						LOG.error("Error in obtain element screenshot "
								+ident.getValue()+" : " + e.getLocalizedMessage());
					}
				}
			}
			InputValue inputValue = formInput.getInputValues().iterator().next();
			info.addNewDetail(inputValue.getType(), inputValue.getValue(), currentState);
			LOG.trace("Se agrega un nuevo registro en"+
			" el formsInputRevisited {} {} {}",inputValue.getType(), 
			inputValue.getValue(), currentState.getId() );
			formsInputRevisited.put(xpathExpr, info);
		//}
		// analizar como sera con los invalidos: se define antes

	}

	/**
	 * The onNewState listener implemented to search validation elements.
	 * 
	 * @param crawlerContext
	 *            The {@link CrawlerContext}.
	 * @param stateVertex
	 *            The new {@link StateVertex}.
	 */
	public void onNewState(CrawlerContext crawlerContext, StateVertex stateVertex) {
		
		//obtiene el url
		String url = stateVertex.getUrl();

		// obtener el nombre del estado
		String name = DOMUtils.getTextUnderXPathExpresion(crawlerContext.getBrowser(), this.TITLE_UNDER_XPATH);
		LOG.trace("State title name = {}", name);
		if( name == null){
			//si TITLE_UNDER_XPATH es null, el url es el nombre de la pagina.
			name = url;
		}
		
		stateNameList.add(new JatytaStateName(crawlId, stateVertex.getId(), name, url));


		// se aumenta el contador de estados.
		if (stateVertex.getId() > statesNumber) {
			statesNumber = stateVertex.getId();
		}
		
		LOG.debug("Iniciando el metodo on new state del plugin...para el estado " 
		+ stateVertex.getId() + " - "+ name);
		if(stateVertex.getId() > 0){
			LOG.debug("Crawlpath size :" + crawlerContext.getCrawlPath().size());	
		}
		

	}

	@Override
	public String toString() {
		return "AutomationFormInputValuesPlugin";
	}

	/**
	 * Obtain the values from KnowledgeBase with the
	 * {@link KnowledgeBaseSearchCriteria} parameter. if
	 * MAX_VALUES_PER_FORM_FIELD > 0 then the list size is truncated by the
	 * value of MAX_VALUES_PER_FORM_FIELD.
	 * 
	 * @param searchCriteria
	 *            The {@link KnowledgeBaseSearchCriteria} parameter.
	 * @return The {@link InputValue} with the value and {@link FormValueType}
	 *         obtained from KnowledgeBase, <code>null</code> if search Criteria
	 *         return ZERO results.
	 */
	public List<InputValue> obtainValuesWithSearchCriteria(KnowledgeBaseSearchCriteria searchCriteria) {
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance();
		List<KnowledgeBaseItem> valueList = knowledgeBase.getValueList(searchCriteria);
		if (valueList != null && valueList.size() > 0) {
			List<InputValue> result = new LinkedList<InputValue>();

			for (KnowledgeBaseItem item : valueList) {
				FormValueType type = FormValueType.UNKNOWN;
				if(item.getValid()){
					// valid = true
					type = FormValueType.VALID;

				}else{
					// valid = false
					type = FormValueType.INVALID;
				}
				for (String value : item.getValues()) {
					InputValue inputValue = new InputValue(value, true, type);
					result.add(inputValue);
					if (MAX_VALUES_PER_FORM_FIELD > 0 && result.size() >= MAX_VALUES_PER_FORM_FIELD) {
						result = result.subList(0, MAX_VALUES_PER_FORM_FIELD);
						break;
					}
				}
				if (MAX_VALUES_PER_FORM_FIELD > 0 && result.size() >= MAX_VALUES_PER_FORM_FIELD) {
					break;
				}

			}

			return result;
		}
		return null;
	}

	/**
	 * Constructs and return the {@link KnowledgeBaseSearchCriteria} with the
	 * attributes "name" and "id", if the node have
	 * {@link MicroDataNodeAttribute} defined, overrides "name" and "id" in the
	 * {@link Node} parameter.
	 * 
	 * @param node
	 *            The {@link Node} parameter.
	 * @return The {@link KnowledgeBaseSearchCriteria} with the node attributes.
	 */
	public KnowledgeBaseSearchCriteria constructSearchCriteria(Node node) {

		// se obtiene el itemtype y schema desde el parent node
		String itemTypeValue = getFirstParentNodeWithMicroDataAttribute(node, MicroDataNodeAttribute.ITEMTYPE);
		//LOG.debug("itemTypeValue = "+itemTypeValue+" for "+node);
		KnowledgeBaseSearchCriteria searchCriteria = new KnowledgeBaseSearchCriteria();

		if(itemTypeValue !=null && !itemTypeValue.isEmpty()){
			
			String schemaName = SchemaUtils.getSchemaName(itemTypeValue);
			String itemTypeName = SchemaUtils.getItemTypeName(itemTypeValue);
			// Se setean el schema y itemtype.
			searchCriteria.setItemType(itemTypeName);
			searchCriteria.setSchema(schemaName);
		}
		//si no se obtuvo, se obtiene desde la configuracion
		if(searchCriteria.getSchema()==null ||  
				searchCriteria.getSchema().isEmpty()){
			searchCriteria.setSchema(DEFAULT_SCHEMA);	
		}
		

		// se obtiene el valor del filtro VALID =true, INVALID = false, BOTH =
		// null
		if (formValueType.equals(FormValueType.VALID)) {
			searchCriteria.setValid(true);
		} else if (formValueType.equals(FormValueType.INVALID)) {
			searchCriteria.setValid(false);
		} else {
			searchCriteria.setValid(null);
		}

		Node nodeAttr;

		if (WHITEBOX_TEST) {

			for (int i = 0; i < microDataNodeAttributes.length; i++) {
				nodeAttr = node.getAttributes().getNamedItem(microDataNodeAttributes[i].getValue());
				if (nodeAttr != null && nodeAttr.getNodeValue() != null && !nodeAttr.getNodeValue().isEmpty()) {
					String value = nodeAttr.getNodeValue();
					if (MicroDataNodeAttribute.ITEMPROP.equals(microDataNodeAttributes[i])) {
						// debe setear el campo name
						searchCriteria.setItemProp(value);
					} else if (MicroDataNodeAttribute.ITEMSCOPE.equals(microDataNodeAttributes[i])) {
						// TODO que puede definir este tag?
						// searchCriteria.setType(value);
					} else if (MicroDataNodeAttribute.ITEMID.equals(microDataNodeAttributes[i])) {
						// TODO como obtener un valor especifico de la KB
						// searchCriteria.setId(value);
					} else if (MicroDataNodeAttribute.ITEMREF.equals(microDataNodeAttributes[i])) {
						// TODO que se debe realizar con este tag?
						// searchCriteria.setMin(value);
					} else if (MicroDataNodeAttribute.ITEMTYPE.equals(microDataNodeAttributes[i])) {
						// Se debe separar el schema del itemtype name.
						String schemaName = SchemaUtils.getSchemaName(value);
						String itemTypeName = SchemaUtils.getItemTypeName(value);
						// Se setean el schema y itemtype.
						searchCriteria.setItemType(itemTypeName);
						searchCriteria.setSchema(schemaName);
					}
				}
			}
		} else {

			// BLACK BOX TEST

			searchCriteria.setItemType(null);

			nodeAttr = node.getAttributes().getNamedItem(HtmlElementAttribute.ID.getValue());

			String nodeValue = null;
			// obtiene el valor de Id del tag html
			if (nodeAttr != null && nodeAttr.getNodeValue() != null && !nodeAttr.getNodeValue().isEmpty()) {
				// set del valor de itemprop

				nodeValue = nodeAttr.getNodeValue();
			} else {
				// obtiene el valor de name del tag html
				nodeAttr = node.getAttributes().getNamedItem(HtmlElementAttribute.NAME.getValue());
				if (nodeAttr != null && nodeAttr.getNodeValue() != null && !nodeAttr.getNodeValue().isEmpty()) {
					// set del valor de itemprop
					nodeValue = nodeAttr.getNodeValue();
				}
			}
			// si se debe ajustar el ID que genero JSF
			if (AJUST_JSF_AUTO_GENERATED_ID) {
				nodeValue = JSFUtils.removeAutoGeneratedParentIdOrName(nodeValue);
			}
			searchCriteria.setItemProp(nodeValue);
		}
		LOG.trace("searchCriteria = {} for {} ", searchCriteria, 
				node.getAttributes().getNamedItem(
						HtmlElementAttribute.ID.getValue()));
		return searchCriteria;
	}

	/**
	 * Recursive method to obtain the first parent {@link Node} with the
	 * {@link MicroDataNodeAttribute} return the value of the attribute.
	 * 
	 * @param child
	 *            The child {@link Node} to find the parent.
	 * @param microDataNodeAttribute
	 *            The {@link MicroDataNodeAttribute} to search.
	 * @return The parent microdataNodeAttribute value,
	 *         null if don't exists.
	 */
	private String getFirstParentNodeWithMicroDataAttribute(Node child, 
			MicroDataNodeAttribute microDataNodeAttribute) {

		String result = null;
		
		if(child.getParentNode()!=null 
				&& child.getParentNode().getAttributes()!=null){
			Node attr = child.getParentNode().getAttributes()
					.getNamedItem(microDataNodeAttribute.getValue());
			if(attr != null && attr.getNodeValue() != null && !attr.getNodeValue().isEmpty()){
				result = attr.getNodeValue();
			}else{
				result = getFirstParentNodeWithMicroDataAttribute(child.getParentNode(), microDataNodeAttribute);
			}
		}
		return result;
	}

	/**
	 * Return all propNames and values from Knowledge Base with the filter
	 * criteria(VALID, INVALID and BOTH). Use the FormValueType value defined en
	 * this object.
	 * 
	 * @return a {@link Map} with the propName and the {@link List} of values
	 *         Associated.
	 * @throws JatytaException
	 *             if a {@link HibernateException} occurs.
	 */
	public Map<String, List<String>> getAllPropNamesAndValuesByFilter() throws JatytaException {
		PropValueDAO dao = (PropValueDAO) this.getJatytaService().getDAO(new PropValue());
		Map<String, List<String>> propnames = dao.getAllPropNamesAndValuesByFilter(this.getFormValueType());
		return propnames;
	}
	
	
	/**
	 * Return all propNames and values from Knowledge Base with VALID 
	 * FormValueType value defined in this object.
	 * 
	 * @return a {@link Map} with the propName and the {@link List} of values
	 *         Associated.
	 * @throws JatytaException
	 *             if a {@link HibernateException} occurs.
	 */
	public Map<String, List<String>> getAllPropNamesAndValuesValid() throws JatytaException {
		PropValueDAO dao = (PropValueDAO) this.getJatytaService().getDAO(new PropValue());
		Map<String, List<String>> propnames = dao.getAllPropNamesAndValuesByFilter(FormValueType.VALID);
		return propnames;
	}
	
	/**
	 * Return all propNames and values from Knowledge Base with INVALID 
	 * FormValueType value defined in this object.
	 * 
	 * @return a {@link Map} with the propName and the {@link List} of values
	 *         Associated.
	 * @throws JatytaException
	 *             if a {@link HibernateException} occurs.
	 */
	public Map<String, List<String>> getAllPropNamesAndValuesInvalid() throws JatytaException {
		PropValueDAO dao = (PropValueDAO) this.getJatytaService().getDAO(new PropValue());
		Map<String, List<String>> propnames = dao.getAllPropNamesAndValuesByFilter(FormValueType.INVALID);
		return propnames;
	}
	
	@Override
	public void onFireEventSuccess(CrawlerContext context,StateVertex source, 
			StateVertex target, List<Eventable> pathToSuccess) {
		LOG.trace("Se ejecuto  exitosamente un evento "+
			"desde el estado {} al estado {} ",  source,target);
		
		Map<String, ValidationRecord> listValidationRec = validationUtils.
				findValidation(context, target,
						source, formsInputRevisited);


		listValidationRecords.addAll(listValidationRec.values());

		
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.core.plugin.PreStateCrawlingPlugin#preStateCrawling(com.
	 * crawljax.core.CrawlerContext, com.google.common.collect.ImmutableList,
	 * com.crawljax.core.state.StateVertex)
	 */
	@Override
	public void preStateCrawling(CrawlerContext context, ImmutableList<CandidateElement> candidateElements,
			StateVertex state) {
		LOG.debug("Antes explorar el estado " + state.getId() + " - " + state.getName());
		for (CandidateElement candidateElement : candidateElements) {
			LOG.debug("Candidate Element = " + candidateElement.getGeneralString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.core.plugin.PostCrawlingPlugin#postCrawling(com.crawljax
	 * .core.CrawlSession, com.crawljax.core.ExitNotifier.ExitStatus)
	 */
	@Override
	public void postCrawling(CrawlSession session, ExitStatus exitReason) {
		LOG.info("Finalizando el recorrido: {} , Razon de finalización : {}", 
				session.getConfig().getUrl(), exitReason.name());

		for (JatytaStateName stateName : stateNameList) {
			try {
				jatytaService.saveEntity(stateName);
			} catch (JatytaException e) {
				LOG.error(" StateNameList: "+e.getMessage());
			}

		}
		
		for (String key : formsInputRevisited.keySet()) {
			try {
				LOG.trace(" Form Info : " + formsInputRevisited.get(key)
				.toString());
				FormValuesInfo info = formsInputRevisited.get(key);
				// crear nueva entidad para guardar la imagen del elemento.
				JatytaFormFieldRecord field = new JatytaFormFieldRecord();
				field.setIdCrawlRecord(crawlId);
				field.setFormFieldXPath(key);
				field.setImage(info.getImage());
				field.setLabelImage(info.getLabelImage());
				
				jatytaService.saveEntity(field);
	
				for (FormValuesInfoDetail detail : info.getValuesDetail()) {
					JatytaFormValueRecord record = JatytaFormValueRecordDAO.
							mapping(crawlId, info.getId(), detail);
						record.setFormFieldRecord(field);
						jatytaService.saveEntity(record);	
				}
			} catch (JatytaException e) {
				LOG.error(" FormsInputRevisited : " + e.getMessage());
			}

		}

		for (ValidationRecord info : listValidationRecords) {
			
			LOG.trace(" Validation Info : " + info.toString());
			JatytaValidationRecord record = new JatytaValidationRecord();
			try{
				record.setIdCrawlRecord(crawlId);
				record.setValidationState(info.getValidationState());
				record.setValidationStatus(info.getValidationStatus());
				record.setTargetState(info.getTargetState());
				record.setValidationElementXpath(info.getValidationElementXpath());
				record.setTargetElementXpath(info.getTargetElementXpath());
				record.setTargetValue(info.getTargetValue());
				record.setImage(info.getImage());
				record.setValueOrder(info.getValueOrder());
				record.setValueType(info.getValueType().getValue());
				jatytaService.saveEntity(record);
			}catch(NullPointerException e){
				LOG.error("ERROR when try to create JatytaValidationRecord"
						+" from ValidationRecord : {}", info);
			} catch (JatytaException e) {
				LOG.error(" ListValidationRecords : " + e.getMessage());
			}
		}
	} 

	/**
	 * If the LOGIN_USERNAME_XPATH, LOGIN_PASSWORD_XPATH,LOGIN_SUBMIT_XPATH
	 * attributes have values,it search this elements in the first state, id the
	 * elements exists ,then is a login page and the LOGIN_USERNAME_VALUE and
	 * LOGIN_PASSWORD_VALUE are used to submit the form.
	 * 
	 * @param crawlerContext
	 *            The {@link CrawlerContext}.
	 * @author mgimenez
	 */
	@Override
	public void onUrlLoad(CrawlerContext crawlerContext) {
		LOG.trace("URL loaded on state {}.", crawlerContext.getCurrentState());
		Identification username = new Identification(How.xpath, this.LOGIN_USERNAME_XPATH);
		Identification password = new Identification(How.xpath, this.LOGIN_PASSWORD_XPATH);
		Identification submit = new Identification(How.xpath, this.LOGIN_SUBMIT_XPATH);
		if (crawlerContext.getBrowser().elementExists(username) && crawlerContext.getBrowser().elementExists(password)
				&& crawlerContext.getBrowser().elementExists(submit)) {
			// solo si existen los tres elementos, se ejecuta el login.
			LOG.info("Login elements found in state : ", crawlerContext.getCurrentState());
			crawlerContext.getBrowser().input(username, this.LOGIN_USERNAME_VALUE);
			crawlerContext.getBrowser().input(password, this.LOGIN_PASSWORD_VALUE);
			crawlerContext.getBrowser().getWebElement(submit).click();


	
			try {
				Thread.sleep(1000);	
			} catch (InterruptedException e) {
				LOG.error(e.getMessage());
			}
			
			// despues del login, ir a la url principal
			//URI auth = crawlerContext.getConfig().getBasicAuthUrl(); 
			crawlerContext.getBrowser().goToUrl(crawlerContext.getConfig().getUrl());
		}

	}
	
	/**
	 * Return a test value for the index number. 
	 * 0 for empty value
	 * 1 for overflow Text 
	 * 2 for overflow Number
	 * 3 for Invalid Date
	 * 4 for Valid Text
	 * 5 for Valid Number
	 * 6 for Valid Date
	 * 
	 * @author mgimenez
	 * @param index
	 *            the format of the value.
	 * @return The value generated in random mode.
	 */
	public String getTestValue(int index) {
		String value ="";
		long longValue;
		Random random = new Random();
		switch (index) {
		case 0:
			// empty
			value = "";
			break;
		case 1:
			// overflow text
			value = new RandomInputValueGenerator().getRandomString(
					this.getMAX_TEXT_SIZE()+1);
			break;
		case 2:
			//overflow number
			longValue = MathUtils.nextLong(random, (long) Math.pow(10, 
					this.getMAX_NUMBER_SIZE()+1));
			value = Long.toString(longValue);
			break;
		case 3:
			//invalid date
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(System.currentTimeMillis()));
			
			String datePattern = this.getDATE_VALUE_PATTERN();
			datePattern.replaceAll("[yyyy]",String.valueOf(calendar.get(
					Calendar.YEAR)));
			datePattern.replaceAll("[dd]",String.valueOf(calendar.get(
					Calendar.DAY_OF_MONTH)));
			datePattern.replaceAll("[MM]",String.valueOf(13));
			
			break;
		case 4:
			//valid text
			value = new RandomInputValueGenerator().getRandomString(
					this.getMAX_TEXT_SIZE());
			break;
		case 5:
			//valid number
			longValue = MathUtils.nextLong(random, (long) Math.pow(10, 
					this.getMAX_NUMBER_SIZE()));
			value = Long.toString(longValue);
			break;
		case 6:
			// Date valid
			Date date = new Date(System.currentTimeMillis());
			// hacer el pattern como parametro de configuracion
			SimpleDateFormat sdf = new SimpleDateFormat(this.getDATE_VALUE_PATTERN());
			value = sdf.format(date);
			break;
			
		}
		return value;
	}
	

	/**
	 * Return the {@link JatytaService} instance associated.
	 * 
	 * @return the jatytaService
	 */
	public JatytaService getJatytaService() {
		return jatytaService;
	}

	/**
	 * Return if the crawl is for White box test
	 * 
	 * @return the wHITEBOX_TEST (true o false)
	 */
	public Boolean getWHITEBOX_TEST() {
		return WHITEBOX_TEST;
	}

	/**
	 * @return the mAX_VALUES_PER_FORM_FIELD
	 */
	public Integer getMAX_VALUES_PER_FORM_FIELD() {
		return MAX_VALUES_PER_FORM_FIELD;
	}

	/**
	 * @return the fORM_SUBMIT_ELEMENT
	 */
	public String getFORM_SUBMIT_ELEMENT() {
		return FORM_SUBMIT_ELEMENT;
	}

	/**
	 * @return the fORM_SUBMIT_ATTRIBUTE
	 */
	public String getFORM_SUBMIT_ATTRIBUTE() {
		return FORM_SUBMIT_ATTRIBUTE;
	}

	/**
	 * @return the fORM_SUBMIT_ATTRIBUTE_VALUE
	 */
	public String getFORM_SUBMIT_ATTRIBUTE_VALUE() {
		return FORM_SUBMIT_ATTRIBUTE_VALUE;
	}

	/**
	 * @return the fORM_SUBMIT_TEXT
	 */
	public String getFORM_SUBMIT_TEXT() {
		return FORM_SUBMIT_TEXT;
	}

	/**
	 * @return the formValueType
	 */
	public FormValueType getFormValueType() {
		return formValueType;
	}

	/**
	 * @return the statesNumber
	 */
	public Integer getStatesNumber() {
		return statesNumber;
	}

	/**
	 * @return the mAX_TEXT_SIZE
	 */
	public Integer getMAX_TEXT_SIZE() {
		return MAX_TEXT_SIZE;
	}

	/**
	 * @return the mAX_NUMBER_SIZE
	 */
	public Integer getMAX_NUMBER_SIZE() {
		return MAX_NUMBER_SIZE;
	}

	/**
	 * @return the dATE_VALUE_PATTERN
	 */
	public String getDATE_VALUE_PATTERN() {
		return DATE_VALUE_PATTERN;
	}

	/**
	 * @return the lOGIN_USERNAME_XPATH
	 */
	public String getLOGIN_USERNAME_XPATH() {
		return LOGIN_USERNAME_XPATH;
	}

	/**
	 * @return the lOGIN_PASSWORD_XPATH
	 */
	public String getLOGIN_PASSWORD_XPATH() {
		return LOGIN_PASSWORD_XPATH;
	}

	/**
	 * @return the lOGIN_USERNAME_VALUE
	 */
	public String getLOGIN_USERNAME_VALUE() {
		return LOGIN_USERNAME_VALUE;
	}

	/**
	 * @return the lOGIN_PASSWORD_VALUE
	 */
	public String getLOGIN_PASSWORD_VALUE() {
		return LOGIN_PASSWORD_VALUE;
	}

	/**
	 * @return the lOGIN_SUBMIT_XPATH
	 */
	public String getLOGIN_SUBMIT_XPATH() {
		return LOGIN_SUBMIT_XPATH;
	}

	/**
	 * @param brokenLinksCheckerPlugin the brokenLinksCheckerPlugin to set in the validationUtils.
	 */
	public void setBrokenLinksCheckerPlugin(BrokenLinksCheckerPlugin brokenLinksCheckerPlugin) {
		this.validationUtils.setBrokenLinksCheckerPlugin(brokenLinksCheckerPlugin);
	}
	
	/**
	 * return the name of the state number.
	 * @param id
	 * @return
	 */
	public String getStateNameByState(Integer id){
		for (JatytaStateName stateName: stateNameList) {
			if(stateName.getState().equals(id)){
				return stateName.getName();
			}
		}
		return null;
	}
	
	/**
	 * Return the list of form Configurations
	 * @return a {@link List} of {@link JatytaFormConfiguration}
	 */
	public List<JatytaFormConfiguration> getFormConfigurationList(){
		List<JatytaFormConfiguration> result = new ArrayList<>();
		
		try {
			JatytaFormConfigurationDAO dao = (JatytaFormConfigurationDAO) 
					jatytaService.getDAO(new JatytaFormConfiguration());
			result = dao.getFormConfigurationsByConfigId(this.crawlConfigurationId);
		} catch (JatytaException e) {
			LOG.error(e.getLocalizedMessage());
		}
		
		return result;
	}

}
