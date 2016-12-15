/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.validation;

import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPathExpressionException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawljax.core.CrawlerContext;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.Identification.How;
import com.crawljax.core.state.StateVertex;
import com.crawljax.util.DomUtils;
import com.crawljax.web.jatyta.model.entities.JatytaValidationConfiguration;
import com.crawljax.web.jatyta.plugins.BrokenLinksCheckerPlugin;
import com.crawljax.web.jatyta.plugins.util.dom.DOMUtils;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.form.FormFieldCandidate;
import com.crawljax.web.jatyta.plugins.util.form.FormUtils;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;
import com.crawljax.web.jatyta.plugins.util.form.FormValuesInfo;
import com.crawljax.web.jatyta.plugins.util.form.FormValuesInfoDetail;

/**
 * Utils for validation management
 * 
 * @author mgimenez
 * 
 */
public class ValidationUtils {
	private static final Logger LOG = LoggerFactory.getLogger(
			ValidationUtils.class);
	/**
	 * The identifier of summary validations messages.
	 */
	private static final String GLOBAL_VALIDATION_TARGET = "SUMMARY_TARGET";
	/**
	 * The {@link ValidationConfiguration} to obtain the search parameters.
	 */
	private ValidationConfiguration configuration;

	/**
	 * To obtain the list of broken states.
	 */
	private BrokenLinksCheckerPlugin brokenLinksCheckerPlugin;

	/**
	 * The maximum size of text field value, bigger than is overflow values.
	 */
	private Integer maxTextSize=1;
	
	/**
	 * 
	 */
	public ValidationUtils(ValidationConfiguration validationConfiguration) {
		super();
		this.configuration = validationConfiguration;
	}

	/**
	 * Search for Validation messages
	 * 
	 * @param context
	 *            The {@link CrawlerContext} to get the browser instance.
	 * @param newState
	 *            The {@link StateVertex} to find validations.
	 * @param lastStateVisited
	 *            The {@link StateVertex} of the last state visited to obtain
	 *            the form values.
	 * @param formsInputRevisited
	 *            The {@link Map} with the form values history.
	 */
	public Map<String, ValidationRecord> findValidation(CrawlerContext context, 
			StateVertex newState, StateVertex lastStateVisited, Map<String, 
			FormValuesInfo> formsInputRevisited) {

		// Se generan los validation records para el estado actual.
		Map<String, ValidationRecord> listValidationRec = 
				generateValidationRecords(newState, context);
		LOG.debug("Cantidad de Validations records iniciales del estado " 
				+ newState.getName() + " : "+ listValidationRec.size());
		// existe un estado anterior
		if (lastStateVisited != null) {

			// obtener los campos posibles del form anterior,
			List<FormFieldCandidate> candidates = FormUtils.
					obtainFormFieldCandidates(lastStateVisited);

			LOG.debug("Cantidad de FormFieldCandidates del Estado " + 
					lastStateVisited.getName() + " :" + candidates.size());
			
			// si hay validaciones en el estado actual
			if (!listValidationRec.isEmpty()) {
				// por cada mensaje de validacion:
				for (Entry<String, ValidationRecord> valEntry : listValidationRec.entrySet()) {
					//mgimenez 25/07/2016 no estaba seteando el target state
					valEntry.getValue().setTargetState(lastStateVisited.getId());
					// verificar si el Global
					if (valEntry.getKey().equals(GLOBAL_VALIDATION_TARGET)) {
						LOG.debug("Mensaje GLOBAL encontrado");
						List<FormValuesInfoDetail> unknownDetails = new ArrayList<>();
						boolean invalidAll = true;
						boolean validAll = true;
						boolean unknownAll = true;

						for (FormFieldCandidate formFieldCandidate : candidates) {
							// se obtiene el xpath
							String xpathExpression = DOMUtils.generateXPathExpression(formFieldCandidate.getNode(),
									formFieldCandidate.getHtmlElement());
							// se verifica el si registro valor en el campo
							if (formsInputRevisited.containsKey(xpathExpression)) {
								LOG.debug("Verificando FormValuesInfo de "+
										xpathExpression);
								// se obtienen los valores utilizados en el
								// campo.
								FormValuesInfo info = formsInputRevisited.get(xpathExpression);
								// se debe obtener el ultimo valor asociado
								// al estado.
								FormValuesInfoDetail detail = info.getLastOrderDetailByState(lastStateVisited); 
								if (detail!=null) {
									LOG.debug("Se obtuvo el FormValuesInfoDetail de "+
											xpathExpression);
									//mgimenez 24/07/2016, set del value y order.
									valEntry.getValue().setValueOrder(detail.getValueOrder());
									valEntry.getValue().setTargetValue(detail.getValue().toString());
									valEntry.getValue().setValueType(detail.getType());
									if (detail.getType().equals(FormValueType.VALID)) {
										invalidAll = false;
										unknownAll = false;
									} else if (detail.getType().equals(FormValueType.INVALID)
											|| detail.getType().equals(FormValueType.EMPTY)
											||detail.getType().equals(FormValueType.OVERFLOW)) {
										validAll = false;
										unknownAll = false;
									} else {
										// es unknown
										invalidAll = false;
										validAll = false;
										unknownDetails.add(detail);
									}
								}
							}
						}
						// todos los form input value son invalid, la validacion
						// es exitosa
						if (invalidAll) {
							valEntry.getValue().setStatusSuccess();
							LOG.debug("ValidationRecord ["+valEntry.getValue()+
									"] con estado "+ 
									ValidationRecord.STATUS_SUCCESS);
						} else
						// si todos son form input value son unknown, pasan a
						// invalid, la validacion es exitosa
						if (unknownAll) {
							valEntry.getValue().setStatusSuccess();
							for (FormValuesInfoDetail formValuesInfoDetail : unknownDetails) {
								// se marca como invalid
								formValuesInfoDetail.setType(FormValueType.INVALID);
								valEntry.getValue().setValueType(FormValueType.INVALID);
							}

						} else
						// si todos son valid, la validacion es erronea
						if (validAll) {
							valEntry.getValue().setStatusFailed();
							LOG.debug("ValidationRecord ["+valEntry.getValue()+
									"] con estado "+ 
									ValidationRecord.STATUS_FAIL);
						}
						// no se puede definir si hay valores varios tipos,
						// queda como Unknown

					} else {
						// 09/11/2016
						// buscar el form input value en el estado anterior
						LOG.debug("buscar el form input value en el estado anterior ");
						// se verifica el si registro valor en el campo
						if (formsInputRevisited.containsKey(valEntry.getValue().getTargetElementXpath())) {
							LOG.debug("Verificando FormValuesInfo de "+
									valEntry.getValue().getTargetElementXpath());
							// se obtienen los valores utilizados en el
							// campo.
							FormValuesInfo info = formsInputRevisited.get(valEntry.getValue().getTargetElementXpath());
							// se debe obtener el ultimo valor asociado
							// al estado.
							FormValuesInfoDetail detail = info.getLastOrderDetailByState(lastStateVisited); 
							if (detail!=null) {
								LOG.debug("Se obtuvo el FormValuesInfoDetail de "+
										valEntry.getValue().getTargetElementXpath());
								//mgimenez 24/07/2016, set del value y order.
								valEntry.getValue().setValueOrder(detail.getValueOrder());
								valEntry.getValue().setTargetValue(detail.getValue().toString());
								valEntry.getValue().setValueType(detail.getType());
								if (detail.getType().equals(FormValueType.VALID)) {
									valEntry.getValue().setStatusFailed();
									LOG.debug("ValidationRecord ["+valEntry.getValue()+
											"] con estado "+ 
											ValidationRecord.STATUS_FAIL);
								} else if (detail.getType().equals(FormValueType.INVALID)
										|| detail.getType().equals(FormValueType.EMPTY)
										||detail.getType().equals(FormValueType.OVERFLOW)) {
									valEntry.getValue().setStatusSuccess();
									LOG.debug("ValidationRecord ["+valEntry.getValue()+
											"] con estado "+ 
											ValidationRecord.STATUS_SUCCESS);
								} else {
									// es unknown
									valEntry.getValue().setStatusSuccess();
									// se marca como invalid
									detail.setType(FormValueType.INVALID);
									valEntry.getValue().setValueType(FormValueType.INVALID);
								}
							}
						}
					}
				}

			} else {
				// no hay mensajes de validacion en el estado actual.
				LOG.debug("no hay mensajes de validacion en el estado "+newState.getName());
				for (FormFieldCandidate formFieldCandidate : candidates) {
					String xpathExpression = DOMUtils.generateXPathExpression(formFieldCandidate.getNode(),
							formFieldCandidate.getHtmlElement());
					ValidationRecord validationRecord = new ValidationRecord();
					validationRecord.setCrawlState(newState.getId());
					validationRecord.setValidationState(newState.getId());
					validationRecord.setTargetState(lastStateVisited.getId());
					validationRecord.setTargetHtmlElement(formFieldCandidate.getHtmlElement());
					validationRecord.setTargetNode(formFieldCandidate.getNode());
					validationRecord.setTargetElementXpath(xpathExpression);
					if (formsInputRevisited.containsKey(xpathExpression)) {
						// se obtienen los valores utilizados en el
						// campo.
						FormValuesInfo info = formsInputRevisited.get(xpathExpression);
						LOG.debug("Verificando FormValuesInfo de "+
								xpathExpression);
						// se debe obtener el ultimo valor asociado
						// al estado.
						FormValuesInfoDetail detail = info.getLastOrderDetailByState(lastStateVisited); 
						if (detail!=null) {
							LOG.debug("Se obtuvo el FormValuesInfoDetail de "+
									xpathExpression);
							validationRecord.setValueOrder(detail.getValueOrder());
							validationRecord.setTargetValue(detail.getValue().toString());
							validationRecord.setValueType(detail.getType());
							//  puede ser un estado de error
							if (brokenLinksCheckerPlugin.isBrokenState(newState)) {
								validationRecord.setStatusFailed();
								LOG.debug("ValidationRecord ["+validationRecord+
										"] con estado "+ 
										ValidationRecord.STATUS_FAIL);
								if (FormValueType.UNKNOWN.equals(detail.getType())) {
									// se marca como invalido los valores desconocidos.
									//mgimenez: 04/10/16 , si es unknown, setea a empty o overflow, otro caso, invalid.
									String value = detail.getValue().toString(); 
									if(value.isEmpty()){
										detail.setType(FormValueType.EMPTY);
										validationRecord.setValueType(FormValueType.EMPTY);
									}else if (value.length()>maxTextSize){
										detail.setType(FormValueType.OVERFLOW);
										validationRecord.setValueType(FormValueType.OVERFLOW);
									}else{
										detail.setType(FormValueType.INVALID);
										validationRecord.setValueType(FormValueType.INVALID);
									}

								}
							} else {
								//  puede ser un estado exitoso,

								if (FormValueType.INVALID.equals(detail.getType())
										|| detail.getType().equals(FormValueType.EMPTY)
										||detail.getType().equals(FormValueType.OVERFLOW)) {
									/*
									 * 4 - si no se encuentra mensajes de
									 * validacion para un valor invalido, se
									 * debe registrar como error de validacion.
									 */
									validationRecord.setStatusFailed();
									LOG.debug("ValidationRecord ["+validationRecord+
											"] con estado "+ 
											ValidationRecord.STATUS_FAIL);

								} else if (FormValueType.UNKNOWN.equals(detail.getType())) {
									/*
									 * 5 - si se introdujo un valor desconocido
									 * y no se encontro mensaje de validación,
									 * marcar como valido. validacion ok
									 */
									validationRecord.setStatusSuccess();
									LOG.debug("ValidationRecord ["+validationRecord+
											"] con estado "+ 
											ValidationRecord.STATUS_SUCCESS);
									//mgimenez: 04/10/16 , si es unknown, setea a empty o overflow, otro caso, valid.
									String value = detail.getValue().toString(); 
									if(value.isEmpty()){
										detail.setType(FormValueType.EMPTY);
										validationRecord.setValueType(FormValueType.EMPTY);
									}else if (value.length()>maxTextSize){
										detail.setType(FormValueType.OVERFLOW);
										validationRecord.setValueType(FormValueType.OVERFLOW);
									}else{
										detail.setType(FormValueType.VALID);
										validationRecord.setValueType(FormValueType.VALID);
									}


								} else if (FormValueType.VALID.equals(detail.getType())) {
									/*
									 * 3 - si el valor es valido y no hay
									 * mensaje de validacion, validacion OK.
									 */
									validationRecord.setStatusSuccess();
									LOG.debug("ValidationRecord ["+validationRecord+
											"] con estado "+ 
											ValidationRecord.STATUS_SUCCESS);
								}
								
							}

							listValidationRec.put(xpathExpression, validationRecord);
							LOG.debug("Add del ValidationRecord [ "+
									validationRecord+" ]");
						}
					}
				}
			}
		}
		LOG.debug("Cantidad de Validations records finales del estado " 
				+ newState.getName() + " : "+ listValidationRec.size());
		return listValidationRec;
	}

	/**
	 * Set the status for the {@link ValidationRecord} parameter when a
	 * Validation Record is Found. Set Status failed if {@link FormValueType} is
	 * VALID. Otherwise set Success.
	 * 
	 * @param validationRecord
	 *            The found {@link ValidationRecord} record.
	 * @param detail
	 *            The {@link FormValuesInfoDetail} to obtain the
	 *            {@link FormValueType}
	 */
	public void setStatusValidationFound(ValidationRecord validationRecord, FormValuesInfoDetail detail) {
		if (FormValueType.INVALID.equals(detail.getType())) {
			/*
			 * 1 - si sale un mensaje de validacion para valores invalidos en el
			 * estado anterior. validacion OK
			 */
			validationRecord.setStatusSuccess();

		} else if (FormValueType.UNKNOWN.equals(detail.getType())) {
			/*
			 * 2 - si sale un mensaje de validacion para un valor desconocido,
			 * se debe actualizar a invalido dicho valor. validacion OK
			 */
			validationRecord.setStatusSuccess();

			detail.setType(FormValueType.INVALID);

		} else if (FormValueType.VALID.equals(detail.getType())) {
			/*
			 * 3 - Si sale un mensaje de validacion para un valor valido, se
			 * debe registrar como un error de validacion.
			 */
			validationRecord.setStatusFailed();
		}
	}

	/**
	 * Generate a map of {@link ValidationRecord} obtained from the dom of the
	 * {@link StateVertex} parameter. the key is the xpath of the target
	 * element, otherwise is SUMMARY_TARGET.
	 * 
	 * @param state
	 *            The {@link StateVertex} parameter.
	 * @param context
	 *            The {@link CrawlerContext} parameter to obtain the Browser.
	 * @return The {@link Map} of {@link ValidationRecord} populated from the
	 *         state parameter.
	 */
	private Map<String, ValidationRecord> generateValidationRecords(StateVertex state, CrawlerContext context) {

		Map<String, ValidationRecord> result = new HashMap<String, ValidationRecord>();

		for (JatytaValidationConfiguration validationConfig : this.configuration.getValidationElements()) {

			Map<String, String> attributesValues = new HashMap<String, String>();
			// Los valores ya se obtienen de validationConfig
			attributesValues.put(validationConfig.getAttributeName(), validationConfig.getAttributeValue());
			// attributesValues.put(htmlAttr.getValue(),
			// "ui-message-error-icon");
			HtmlElement htmlElem = HtmlElement.valueOf(validationConfig.getHtmlElement());
			List<Node> nodeList = DOMUtils.getNodesByTagAndAttributesLike(state, htmlElem, attributesValues);
			for (Node node : nodeList) {
				LOG.debug(htmlElem.getValue() + " for validation found : " + DOMUtils.getNodeAttributesToString(node));
				String keyValue = GLOBAL_VALIDATION_TARGET;
				ValidationRecord newRecord = new ValidationRecord(htmlElem, node, state.getId());
				newRecord.setValidationElementXpath(DOMUtils.generateXPathExpression(node, htmlElem));

				// mgimenez: Se trata de obtener el screenshot del elemento de
				// validacion
				// se obtiene el webelement para saber su posicion
				Identification ident = new Identification(How.xpath, newRecord.getValidationElementXpath());
				if (context.getBrowser().elementExists(ident) && context.getBrowser().isVisible(ident)) {
					// si existe en el browser
					WebElement webElement = context.getBrowser().getWebElement(ident);
					byte[] image = null;
					try {
						image = FormUtils.getElementScreenShot(webElement, context.getBrowser());
						newRecord.setImage(image);
					} catch (IOException | RasterFormatException e) {
						LOG.error("Error in obtain element screenshot " + ident.getValue() + " : "
								+ e.getLocalizedMessage());
					}
					
					// SE debe intentar obtener el campo asociado al validation
					// record. data-target
					// se debe obtener el attr que identificar el target
					// desde la base de conocimiento
					if (node.getAttributes() != null
							&& validationConfig.getTargetAttributeName()!= null
							&& node.getAttributes().getNamedItem(validationConfig.getTargetAttributeName()) != null) {

						Node attrTargetIdent = node.getAttributes().getNamedItem(validationConfig.getTargetAttributeName());
						LOG.debug("The target attribute value = " + attrTargetIdent.getNodeValue());

						Node element = null;
						try {
								element = obtainNodeXPathForValidacionTarget(state.getDocument(), attrTargetIdent.getNodeValue());
						} catch (DOMException e) {
							LOG.error(e.getLocalizedMessage());
						} catch (IOException e) {
							LOG.error(e.getLocalizedMessage());
						} catch (XPathExpressionException e) {
							LOG.error(e.getLocalizedMessage());
						}
						if (element != null) {
							//newRecord.setTargetNode(targetNode);
							newRecord.setTargetHtmlElement(HtmlElement.valueOf(element.getNodeName()));
							keyValue = DOMUtils.generateXPathExpression(element);
							newRecord.setTargetElementXpath(keyValue);
						}
					}
					result.put(keyValue, newRecord);
				}



			}
		}

		return result;
	}
	
	/**
	 * Obtain the node for the xpath expression for input, select and textarea elements with 
	 * the @id or @name atributes that is a sub string of the attributeValue parameter. 
	 * @param attributeValue The attribute value parameter.
	 * @return a Node with contains operator.
	 * @throws XPathExpressionException 
	 */
	private Node obtainNodeXPathForValidacionTarget(Document dom, String attributeValue) throws XPathExpressionException{
		List<String> listXpath = new ArrayList<>();
		String inputId = "//INPUT[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@id) +1) = @id]";
		listXpath.add(inputId);
		String inputName = "//INPUT[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@name) +1) = @name]";
		listXpath.add(inputName);
		String selectId = "//SELECT[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@id) +1) = @id]";
		listXpath.add(selectId);
		String selectName = "//SELECT[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@name) +1) = @name]";
		listXpath.add(selectName);
		String textAreaId = "//TEXTAREA[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@id) +1) = @id]";
		listXpath.add(textAreaId);
		String textAreaName = "//TEXTAREA[substring('"+attributeValue+"', string-length('"+attributeValue+"') - string-length(@name) +1) = @name]";
		listXpath.add(textAreaName);

		Node node = null;
		for (String string : listXpath) {
			node = DomUtils.getElementByXpath(dom,string);
			if(node!=null){
				break;
			}
		}
		return node;
	}

	/**
	 * @param brokenLinksCheckerPlugin
	 *            the brokenLinksCheckerPlugin to set
	 */
	public void setBrokenLinksCheckerPlugin(BrokenLinksCheckerPlugin brokenLinksCheckerPlugin) {
		this.brokenLinksCheckerPlugin = brokenLinksCheckerPlugin;
	}

	/**
	 * @param maxTextSize the maxTextSize to set
	 */
	public void setMaxTextSize(Integer maxTextSize) {
		this.maxTextSize = maxTextSize;
	}

	
}
