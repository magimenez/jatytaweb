/**
 * 
 */
package com.crawljax.web.jatyta.model.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.crawljax.web.jatyta.knowledgebase.db.model.entities.Schema;
import com.crawljax.web.jatyta.knowledgebase.db.util.JatytaAnnotation;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementAttribute;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;

/**
 * @author mgimenez
 * 
 */
@JatytaAnnotation(keyName = "configurationId")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class JatytaCrawlConfiguration implements Serializable, EntityInterface<Long> {

	private static final long serialVersionUID = 1L;

	private Long id;
	/**
	 * Configuration id from Crawljax Configuration
	 */
	private String configurationId;
	/**
	 * Schema by default is NULL.
	 */
	private Schema schema =  null;
	/**
	 * White Box test by default is FALSE.
	 */
	private Boolean whiteBoxTest = false;
	/**
	 * Ajust JSF auto Generated id by default is FALSE;
	 */
	private Boolean ajustJSFAutoGeneratedId = false;
	/**
	 * Filter of form values, by default is BOTH.
	 */
	private String formValuesFilter = FormValueType.BOTH.getValue();
	/**
	 * Maximum values for form input by default is 3.
	 */
	private Integer maxValuesForFormInput = 3;
	/**
	 * Form submit element, by default is input.
	 */
	private String formSubmitElement = HtmlElement.INPUT.getValue().toLowerCase();
	/**
	 * The form submit element attribute for identification, by default type.
	 */
	private String formSubmitAttribute = HtmlElementAttribute.TYPE.getValue();
	/**
	 * The form submit element attribute value, by default submit.
	 */
	private String formSubmitAttributeValue = "submit";
	/**
	 * Form submit element text, by default is NULL.
	 */
	private String formSubmitText = null;
	/**
	 * The maximum size of characters for random text generated, by default 8.
	 */
	private Integer maxTextFieldSize = 8;
	/**
	 * The maximum size of digits for ramdom numbers generated, by default 6.
	 */
	private Integer maxNumberFieldSize = 6;
	/**
	 * The xpath for identification of title page, by default NULL.
	 */
	private String titleFormUnderXpath = null;
	/**
	 * The date value pattern for date values, by default dd-MM-yyyy
	 */
	private String dateValuePattern= "dd-MM-yyyy";
	/**
	 * The xpath for identification of the input for usermane login, by default 
	 * //input[@name='username']
	 */
	private String loginUserNameXpath = "//input[@name='username']";
	/**
	 * The username value for login, by default is username.
	 */
	private String loginUserNameValue = "username";
	/**
	 * The xpath for identification of the input for password login, by default 
	 * //input[@name='password']
	 */
	private String loginPasswordXpath = "//input[@name='password']";
	/**
	 * The password value for login, by default is password.
	 */
	private String loginPasswordValue = "password";
	/**
	 * The xpath for identification of submit login, by default 
	 * //input[@type='submit']
	 */
	private String loginSubmitXpath = "//input[@type='submit']";

	/**
	 *  HTML tag to identify menu items
	 */
	private String menuItemTag = HtmlElement.A.getValue();
	
	/**
	 * Xpath to identify the html element that contains the menu of the tested
	 * web application.
	 */
	private String menuUnderXpath = "//div[@id=menu]";

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the configurationId
	 */
	@Column(name = "configurationId", nullable = false, unique = true)
	public String getConfigurationId() {
		return configurationId;
	}

	/**
	 * @param configurationId
	 *            the configurationId to set
	 */
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}

	/**
	 * @return the schema
	 */
	@ManyToOne
	@JoinColumn(name = "idSchema", nullable = true)
	public Schema getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	/**
	 * @return the whiteBoxTest
	 */
	@Column(name = "whiteBoxTest", nullable = false)
	public Boolean getWhiteBoxTest() {
		return whiteBoxTest;
	}

	/**
	 * @param whiteBoxTest
	 *            the whiteBoxTest to set
	 */
	public void setWhiteBoxTest(Boolean whiteBoxTest) {
		this.whiteBoxTest = whiteBoxTest;
	}

	/**
	 * @return the ajustJSFAutoGeneratedId
	 */
	@Column(name = "ajustJSFAutoGeneratedId", nullable = false)
	public Boolean getAjustJSFAutoGeneratedId() {
		return ajustJSFAutoGeneratedId;
	}

	/**
	 * @param ajustJSFAutoGeneratedId
	 *            the ajustJSFAutoGeneratedId to set
	 */
	public void setAjustJSFAutoGeneratedId(Boolean ajustJSFAutoGeneratedId) {
		this.ajustJSFAutoGeneratedId = ajustJSFAutoGeneratedId;
	}

	/**
	 * @return the formValuesFilter
	 */
	@Column(name = "formValuesFilter", nullable = false)
	public String getFormValuesFilter() {
		return formValuesFilter;
	}

	/**
	 * @param formValuesFilter
	 *            the formValuesFilter to set
	 */
	public void setFormValuesFilter(String formValuesFilter) {
		this.formValuesFilter = formValuesFilter;
	}

	/**
	 * @return the maxValuesForFormInput
	 */
	@Column(name = "maxValuesForFormInput", nullable = false)
	public Integer getMaxValuesForFormInput() {
		return maxValuesForFormInput;
	}

	/**
	 * @param maxValuesForFormInput
	 *            the maxValuesForFormInput to set
	 */
	public void setMaxValuesForFormInput(Integer maxValuesForFormInput) {
		this.maxValuesForFormInput = maxValuesForFormInput;
	}

	/**
	 * @return the formSubmitElement
	 */
	@Column(name = "formSubmitElement", nullable = false)
	public String getFormSubmitElement() {
		return formSubmitElement;
	}

	/**
	 * @param formSubmitElement
	 *            the formSubmitElement to set
	 */
	public void setFormSubmitElement(String formSubmitElement) {
		this.formSubmitElement = formSubmitElement;
	}

	/**
	 * @return the formSubmitAttribute
	 */
	@Column(name = "formSubmitAttribute", nullable = true)
	public String getFormSubmitAttribute() {
		return formSubmitAttribute;
	}

	/**
	 * @param formSubmitAttribute
	 *            the formSubmitAttribute to set
	 */
	public void setFormSubmitAttribute(String formSubmitAttribute) {
		this.formSubmitAttribute = formSubmitAttribute;
	}

	/**
	 * @return the formSubmitAttributeValue
	 */
	@Column(name = "formSubmitAttributeValue", nullable = true)
	public String getFormSubmitAttributeValue() {
		return formSubmitAttributeValue;
	}

	/**
	 * @param formSubmitAttributeValue
	 *            the formSubmitAttributeValue to set
	 */
	public void setFormSubmitAttributeValue(String formSubmitAttributeValue) {
		this.formSubmitAttributeValue = formSubmitAttributeValue;
	}

	/**
	 * @return the formSubmitText
	 */
	@Column(name = "formSubmitText", nullable = true)
	public String getFormSubmitText() {
		return formSubmitText;
	}

	/**
	 * @param formSubmitText
	 *            the formSubmitText to set
	 */
	public void setFormSubmitText(String formSubmitText) {
		this.formSubmitText = formSubmitText;
	}

	/**
	 * @return the maxTextFieldSize
	 */
	@Column(name = "maxTextFieldSize", nullable = true)
	public Integer getMaxTextFieldSize() {
		return maxTextFieldSize;
	}

	/**
	 * @param maxTextFieldSize
	 *            the maxTextFieldSize to set
	 */
	public void setMaxTextFieldSize(Integer maxTextFieldSize) {
		this.maxTextFieldSize = maxTextFieldSize;
	}

	/**
	 * @return the maxNumberFieldSize
	 */
	@Column(name = "maxNumberFieldSize", nullable = true)
	public Integer getMaxNumberFieldSize() {
		return maxNumberFieldSize;
	}

	/**
	 * @param maxNumberFieldSize
	 *            the maxNumberFieldSize to set
	 */
	public void setMaxNumberFieldSize(Integer maxNumberFieldSize) {
		this.maxNumberFieldSize = maxNumberFieldSize;
	}

	/**
	 * @return the titleFormUnderXpath
	 */
	@Column(name = "titleFormUnderXpath", nullable = true)
	public String getTitleFormUnderXpath() {
		return titleFormUnderXpath;
	}

	/**
	 * @param titleFormUnderXpath
	 *            the titleFormUnderXpath to set
	 */
	public void setTitleFormUnderXpath(String titleFormUnderXpath) {
		this.titleFormUnderXpath = titleFormUnderXpath;
	}

	/**
	 * Return the pattern of date field values.
	 * 
	 * @return The pattern of date field values.
	 */
	@Column(name = "dateValuePattern", nullable = true)
	public String getDateValuePattern() {
		return dateValuePattern;
	}

	/**
	 * Set the dateValuePattern.
	 * 
	 * @param dateValuePattern
	 *            The pattern of form input values for dates fields.
	 */
	public void setDateValuePattern(String dateValuePattern) {
		this.dateValuePattern = dateValuePattern;
	}

	

	/**
	 * @return the loginUserNameXpath
	 */
	@Column(name = "loginUserNameXpath", nullable = true)
	public String getLoginUserNameXpath() {
		return loginUserNameXpath;
	}

	/**
	 * @param loginUserNameXpath the loginUserNameXpath to set
	 */
	public void setLoginUserNameXpath(String loginUserNameXpath) {
		this.loginUserNameXpath = loginUserNameXpath;
	}

	/**
	 * @return the loginUserNameValue
	 */
	@Column(name = "loginUserNameValue", nullable = true)
	public String getLoginUserNameValue() {
		return loginUserNameValue;
	}

	/**
	 * @param loginUserNameValue the loginUserNameValue to set
	 */
	public void setLoginUserNameValue(String loginUserNameValue) {
		this.loginUserNameValue = loginUserNameValue;
	}

	/**
	 * @return the loginPasswordXpath
	 */
	@Column(name = "loginPasswordXpath", nullable = true)
	public String getLoginPasswordXpath() {
		return loginPasswordXpath;
	}

	/**
	 * @param loginPasswordXpath the loginPasswordXpath to set
	 */
	public void setLoginPasswordXpath(String loginPasswordXpath) {
		this.loginPasswordXpath = loginPasswordXpath;
	}

	/**
	 * @return the loginPasswordValue
	 */
	@Column(name = "loginPasswordValue", nullable = true)
	public String getLoginPasswordValue() {
		return loginPasswordValue;
	}

	/**
	 * @param loginPasswordValue the loginPasswordValue to set
	 */
	public void setLoginPasswordValue(String loginPasswordValue) {
		this.loginPasswordValue = loginPasswordValue;
	}

	/**
	 * @return the loginSubmitXpath
	 */
	@Column(name = "loginSubmitXpath", nullable = true)
	public String getLoginSubmitXpath() {
		return loginSubmitXpath;
	}

	/**
	 * @param loginSubmitXpath the loginSubmitXpath to set
	 */
	public void setLoginSubmitXpath(String loginSubmitXpath) {
		this.loginSubmitXpath = loginSubmitXpath;
	}
	
	

	/**
	 * @return the menuItemTag
	 */
	@Column(name = "menuItemTag", nullable = true, length=255)
	public String getMenuItemTag() {
		return menuItemTag;
	}

	/**
	 * @param menuItemTag the menuItemTag to set
	 */
	public void setMenuItemTag(String menuItemTag) {
		this.menuItemTag = menuItemTag;
	}

	/**
	 * @return the menuUnderXpath
	 */
	@Column(name = "menuUnderXpath", nullable = true, length=255)
	public String getMenuUnderXpath() {
		return menuUnderXpath;
	}

	/**
	 * @param menuUnderXpath the menuUnderXpath to set
	 */
	public void setMenuUnderXpath(String menuUnderXpath) {
		this.menuUnderXpath = menuUnderXpath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#
	 * getKeyName ()
	 */
	@Override
	@Transient
	public String getKeyName() {

		return "" + configurationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crawljax.web.yatyta.knowledgebase.db.model.dao.EntityInterface#
	 * setKeyName (java.lang.String)
	 */
	@Override
	public void setKeyName(String keyName) {
		if (keyName != null && configurationId.isEmpty()) {
			configurationId = keyName;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " id = " + getId() + ", configurationId = " 
				+ getConfigurationId() + ", formValuesFilter = "
				+ getFormValuesFilter() + ", schema = " + getSchema() 
				+ ", whiteBoxTest = " + getWhiteBoxTest()
				+ ", ajustJSFAutoGeneratedId = " 
				+ getAjustJSFAutoGeneratedId();
	}
}