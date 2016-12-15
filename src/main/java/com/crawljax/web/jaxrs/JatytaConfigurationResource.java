/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.w3c.dom.html.HTMLElement;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaBrokenLinksConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormValueRecordDAO;
import com.crawljax.web.jatyta.model.dao.JatytaValidationConfigurationDAO;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenLinksConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaFormConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaFormValueRecord;
import com.crawljax.web.jatyta.model.entities.JatytaValidationConfiguration;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElementAttribute;
import com.crawljax.web.jatyta.plugins.util.form.FormValueType;
import com.crawljax.web.jatyta.plugins.util.http.HttpError;
import com.crawljax.web.jatyta.plugins.util.validation.ValidationConfiguration;
import com.crawljax.web.model.Configuration;
import com.crawljax.web.model.Configurations;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Resource to obtain the configuration of jatyta.
 * 
 * @author mgimenez
 * 
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/jatyta/config")
public class JatytaConfigurationResource {

	private final JatytaService jatytaService;
	private final Configurations configurations;

	@Inject
	JatytaConfigurationResource(JatytaService jatytaService,
			Configurations configurations) {
		this.configurations = configurations;
		this.jatytaService = jatytaService;
	}

	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link HtmlElement}.
	 * 
	 * @return The {@link Response} with the {@link List} of {@link HtmlElement}
	 *         .
	 */
	@GET
	@Path("/htmlelements")
	public Response getHtmlElements() {

		return Response.ok(HtmlElement.values()).build();

	}

	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link ValidationConfiguration} associated to the configId parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link ValidationConfiguration} associated to the
	 */
	@GET
	@Path("/validationbyconfigid")
	public Response getValidationConfigurations(
			@QueryParam("configId") String configId) {
		try {
			JatytaValidationConfigurationDAO dao = (JatytaValidationConfigurationDAO) jatytaService
					.getDAO(new JatytaValidationConfiguration());
			List<JatytaValidationConfiguration> result = dao
					.getValidationConfigurationsByConfigId(configId);
			return Response.ok(result).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return a new {@link JatytaValidationConfiguration} object.
	 * 
	 * @return The {@link Response} with a empty object of
	 *         {@link JatytaValidationConfiguration}.
	 */
	@GET
	@Path("/newvalidation")
	public Response getNewValidationConfiguration() {
		JatytaValidationConfiguration validation =
				new JatytaValidationConfiguration();
		return Response.ok(validation).build();
	}

	/**
	 * Save or update the {@link JatytaValidationConfiguration} parameter.
	 * 
	 * @param validation
	 *            The {@link JatytaValidationConfiguration} to save or update.
	 * @return The {@link Response} with the
	 *         {@link JatytaValidationConfiguration} update o saved, or
	 *         ServerError if {@link JatytaException} occurs.
	 */
	@PUT
	@Path("/updatevalidation")
	public Response updateValidationConfiguration(
			JatytaValidationConfiguration validation) {
		try {
			validation = (JatytaValidationConfiguration) jatytaService
					.saveEntity(validation);
			return Response.ok(validation).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Delete the {@link JatytaValidationConfiguration} parameter.
	 * 
	 * @param validation
	 *            The {@link JatytaValidationConfiguration} to be deleted.
	 * @return The {@link Response} with the
	 *         {@link JatytaValidationConfiguration} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deletevalidation")
	public Response deleteValidationConfiguration(
			JatytaValidationConfiguration validation) {
		try {
			jatytaService.deleteEntity(validation);
			return Response.ok(validation).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return a new {@link JatytaCrawlConfiguration} object by default.
	 * 
	 * @param configId
	 *            The Crawljac configuration id.
	 * @return The {@link Response} with a empty object of
	 *         {@link JatytaCrawlConfiguration}.
	 */
	@GET
	@Path("/newconfiguration")
	public Response getNewConfiguration(@QueryParam("configId") String configId) {
		JatytaCrawlConfiguration config = new JatytaCrawlConfiguration();
		/* mgimenez: 22/07/16, se modifico la entidad con los valores por defecto.
		config.setConfigurationId(configId);
		config.setAjustJSFAutoGeneratedId(true);
		config.setWhiteBoxTest(false);
		config.setSchema(null);
		config.setFormValuesFilter(FormValueType.BOTH.getValue());
		config.setMaxValuesForFormInput(0);
		config.setFormSubmitElement(HtmlElement.BUTTON.getValue().toLowerCase());
		config.setMaxNumberFieldSize(new Integer(1));
		config.setMaxTextFieldSize(new Integer(1));*/
		return Response.ok(config).build();
	}

	/**
	 * Return in the {@link Response} the associated to the configId parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link ValidationConfiguration} associated to the
	 */
	@GET
	@Path("/configurationbyconfigid")
	public Response getConfigurationByConfigId(
			@QueryParam("configId") String configId) {
		try {
			JatytaCrawlConfigurationDAO dao = 
					(JatytaCrawlConfigurationDAO) jatytaService
					.getDAO(new JatytaCrawlConfiguration());
			JatytaCrawlConfiguration config = dao
					.getConfigurationByCrawljaxConfigId(configId);

			return Response.ok(config).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return the estimated time of crawl by {@link Configuration} and maximum
	 * form values parameters.
	 * 
	 * @param id
	 *            The id of the Crawljax {@link Configuration}
	 * @param maxValues
	 *            The maximum number of values per field form.
	 * @return A {@link BigDecimal} with the estimated crawl time in
	 *         milliseconds.
	 */
	@GET
	@Path("/estimatedtime")
	public Response getEstimatedTime(@QueryParam("id") String id,
			@QueryParam("maxvalues") Integer maxValues) {
		Response r;
		Configuration config = configurations.findByID(id);
		JatytaFormValueRecordDAO dao;
		if (config != null) {
			try {
				dao = (JatytaFormValueRecordDAO) jatytaService
						.getDAO(new JatytaFormValueRecord());
				BigDecimal result = dao.calculateTimeEstimatedForCrawl(config,
						maxValues);
				return Response.ok(result).build();
			} catch (JatytaException e) {
				e.printStackTrace();
				r = Response.serverError().build();
			}
		} else {
			r = Response.serverError().build();
		}
		return r;
	}

	/**
	 * Save or update the {@link JatytaCrawlConfiguration} parameter.
	 * 
	 * @param configuration
	 *            The {@link JatytaCrawlConfiguration} to save or update.
	 * @return The {@link Response} with the {@link JatytaCrawlConfiguration}
	 *         update o saved, or ServerError if {@link JatytaException} occurs.
	 */
	@PUT
	@Path("/updateconfiguration")
	public Response updateConfiguration(JatytaCrawlConfiguration configuration) {
		try {
			configuration = (JatytaCrawlConfiguration) jatytaService
					.saveEntity(configuration);
			return Response.ok(configuration).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return a new {@link JatytaBrokenLinksConfiguration} object.
	 * 
	 * @return The {@link Response} with a empty object of
	 *         {@link JatytaBrokenLinksConfiguration}.
	 */
	@GET
	@Path("/newbrokenlinksconfig")
	public Response getBrokenLinksConfiguration() {
		JatytaBrokenLinksConfiguration config = 
				new JatytaBrokenLinksConfiguration();
		return Response.ok(config).build();
	}

	/**
	 * Save or update the {@link JatytaBrokenLinksConfiguration} parameter.
	 * 
	 * @param brokenLinkConfig
	 *            The {@link JatytaBrokenLinksConfiguration} to save or update.
	 * @return The {@link Response} with the
	 *         {@link JatytaBrokenLinksConfiguration} update o saved, or
	 *         ServerError if {@link JatytaException} occurs.
	 */
	@PUT
	@Path("/updatebrokenlinksconfig")
	public Response updateBrokenLinksConfiguration(
			JatytaBrokenLinksConfiguration brokenLinkConfig) {
		try {
			brokenLinkConfig = (JatytaBrokenLinksConfiguration) jatytaService
					.saveEntity(brokenLinkConfig);
			return Response.ok(brokenLinkConfig).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Delete the {@link JatytaBrokenLinksConfiguration} parameter.
	 * 
	 * @param brokenLinksConfig
	 *            The {@link JatytaBrokenLinksConfiguration} to be deleted.
	 * @return The {@link Response} with the
	 *         {@link JatytaBrokenLinksConfiguration} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deletebrokenlinksconfig")
	public Response deleteBrokenLinksConfiguration(
			JatytaBrokenLinksConfiguration brokenLinksConfig) {
		try {
			jatytaService.deleteEntity(brokenLinksConfig);
			return Response.ok(brokenLinksConfig).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link JatytaBrokenLinksConfiguration} associated to the configId
	 * parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaBrokenLinksConfiguration} associated to the
	 */
	@GET
	@Path("/brokenlinksconfigbyconfigid")
	public Response getBrokenLinksConfigurations(
			@QueryParam("configId") String configId) {
		try {
			JatytaBrokenLinksConfigurationDAO dao = 
					(JatytaBrokenLinksConfigurationDAO) jatytaService
					.getDAO(new JatytaBrokenLinksConfiguration());
			List<JatytaBrokenLinksConfiguration> result = dao
					.getBrokenLinksConfigurationsByConfigId(configId);
			return Response.ok(result).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link JatytaBrokenLinksConfiguration} by default for  the configId
	 * parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaBrokenLinksConfiguration} by default.
	 */
	@GET
	@Path("/brokenlinksbydefault")
	public Response getDefaultBrokenLinksConfigurations(
			@QueryParam("configId") String configId) {
		try {
			List<JatytaBrokenLinksConfiguration> result = 
					new ArrayList<JatytaBrokenLinksConfiguration>();
			for (HttpError error : HttpError.values()) {
				JatytaBrokenLinksConfiguration brokenLinkConfig = 
						new JatytaBrokenLinksConfiguration();
				brokenLinkConfig.setPattern(error.getValue());
				brokenLinkConfig.setConfigurationId(configId);
				result.add(brokenLinkConfig);
			}
			return Response.ok(result).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link JatytaFormConfiguration} associated to the configId
	 * parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaBrokenLinksConfiguration} associated to the
	 */
	@GET
	@Path("/formconfigbyconfigid")
	public Response getFormConfigurations(
			@QueryParam("configId") String configId) {
		try {
			JatytaFormConfigurationDAO dao = 
					(JatytaFormConfigurationDAO) jatytaService
					.getDAO(new JatytaFormConfiguration());
			List<JatytaFormConfiguration> result = dao
					.getFormConfigurationsByConfigId(configId);
			return Response.ok(result).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Return in the {@link Response} the {@link List} of the
	 * {@link JatytaBrokenLinksConfiguration} by default for  the configId
	 * parameter.
	 * 
	 * @param configId
	 *            The Crawljax config id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaBrokenLinksConfiguration} by default.
	 */
	@GET
	@Path("/formbydefault")
	public Response getDefaultFormConfigurations(
			@QueryParam("configId") String configId) {
		try {
			List<JatytaFormConfiguration> result = 
					new ArrayList<JatytaFormConfiguration>();
			
			JatytaFormConfiguration formConfig = 
				new JatytaFormConfiguration();
			formConfig.setConfigurationId(configId);
			result.add(formConfig);
			
			return Response.ok(result).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	
	/**
	 * Save or update the {@link JatytaFormConfiguration} parameter.
	 * 
	 * @param formConfig
	 *            The {@link JatytaFormConfiguration} to save or update.
	 * @return The {@link Response} with the
	 *         {@link JatytaFormConfiguration} update o saved, or
	 *         ServerError if {@link JatytaException} occurs.
	 */
	@PUT
	@Path("/updateformconfig")
	public Response updateFormConfiguration(
			JatytaFormConfiguration formConfig) {
		try {
			formConfig = (JatytaFormConfiguration) jatytaService
					.saveEntity(formConfig);
			return Response.ok(formConfig).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Delete the {@link JatytaFormConfiguration} parameter.
	 * 
	 * @param formConfig
	 *            The {@link JatytaFormConfiguration} to be deleted.
	 * @return The {@link Response} with the
	 *         {@link JatytaFormConfiguration} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deleteformconfig")
	public Response deleteFormConfiguration(
			JatytaFormConfiguration formConfig) {
		try {
			jatytaService.deleteEntity(formConfig);
			return Response.ok(formConfig).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Return a new {@link JatytaFormConfiguration} object.
	 * 
	 * @return The {@link Response} with a empty object of
	 *         {@link JatytaFormConfiguration}.
	 */
	@GET
	@Path("/newformconfig")
	public Response getNewFormConfiguration() {
		JatytaFormConfiguration config = 
				new JatytaFormConfiguration();
		return Response.ok(config).build();
	}

}
