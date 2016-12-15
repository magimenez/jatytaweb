/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.Schema;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlConfigurationDAO;
import com.crawljax.web.jatyta.model.dao.JatytaFormValueRecordDAO;
import com.crawljax.web.jatyta.model.dao.JatytaStateNameDAO;
import com.crawljax.web.jatyta.model.entities.EntityInterface;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenState;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlRecord;
import com.crawljax.web.jatyta.model.entities.JatytaFormValueRecord;
import com.crawljax.web.jatyta.model.entities.JatytaStateName;
import com.crawljax.web.jatyta.model.entities.JatytaValidationRecord;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Resource to obtain the record data from Jatyta Plugins.
 * 
 * @author mgimenez
 * 
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/jatyta/record")
public class JatytaRecordResource {
	private final JatytaService jatytaService;
	public static final Integer STATENAMES_PAGE_SIZE = 100;

	@Inject
	JatytaRecordResource(JatytaService jatytaService) {
		this.jatytaService = jatytaService;
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/crawls")
	public Response getCrawlRecords() {
		try {
			return Response.ok(jatytaService.getAllEntityValues(
					new JatytaCrawlRecord())).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the {@link List} of {@link JatytaFormValueRecord} associated with
	 * the Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @param state The id of the state associated, if null, return all states.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaFormValueRecord}.
	 */
	@GET
	@Path("/formvalues")
	public Response getFormValueRecords(@QueryParam("id") Long id,
			@QueryParam("state") Integer state) {
		try {
			String hql = " idCrawlRecord = " + id;
			if(state!=null){
				hql = hql + " and stateAsociated = "+state;
			}
			List<EntityInterface> result = jatytaService.getEntityByHqlCondition(
					new JatytaFormValueRecord(), hql);
			return Response.ok(result).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}
	
	/**
	 * Return the {@link List} of {@link JatytaStateName} associated to 
	 * {@link JatytaFormValueRecord} with the Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaStateName}.
	 */
	@GET
	@Path("/formstates")
	public Response getFormStates(@QueryParam("id") int id) {
		try {
			JatytaStateNameDAO dao = (JatytaStateNameDAO) jatytaService.getDAO(
					new JatytaStateName());
			return Response.ok(dao.getFormStates(id)).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the {@link List} of {@link JatytaValidationRecord} associated with
	 * the Crawljax record parameter and validationState parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @param state The state id for validationState. If null, return all 
	 * validationStates records.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaValidationRecord}.
	 */
	@GET
	@Path("/validations")
	public Response getValidationRecords(@QueryParam("crawlId") Long id, 
			@QueryParam("state") Integer state) {
		try {
			String hql = " idCrawlRecord = " + id;
			if(state!=null){
				hql = hql + " and validationState = "+state;
			}
			return Response.ok(jatytaService.getEntityByHqlCondition(
					new JatytaValidationRecord(), hql)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the {@link List} of validation states associated to the
	 * {@link JatytaValidationRecord} with the Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @return The {@link Response} with the {@link List} of
	 *         {@link JatytaStateName}
	 */
	@GET
	@Path("/validationstates")
	public Response getValidationStates(@QueryParam("crawlId") int id) {
		try {
			JatytaStateNameDAO dao = (JatytaStateNameDAO) jatytaService.getDAO(
					new JatytaStateName());
			return Response.ok(dao.getValidationStates(id)).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the {@link List} of {@link JatytaBrokenState} associated with the
	 * Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @return The {@link Response} with the {@link List} of jaty
	 */
	@GET
	@Path("/brokenstates")
	public Response getBrokenLinkRecords(@QueryParam("crawlId") Long id) {
		try {
			String hql = " idCrawlRecord = " + id;
			return Response.ok(jatytaService.getEntityByHqlCondition(
					new JatytaBrokenState(), hql)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}
	
	/**
	 * Return the {@link List} of {@link JatytaStateName}  associated with the
	 * Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @param number The page number
	 * @param search The search criteria.
	 * @return The {@link Response} with the {@link List} of 
	 * {@link JatytaStateName}
	 */
	@GET
	@Path("/statenames/{page}")
	public Response getStateNamesRecords( 
			@PathParam("page") Integer number,
			@QueryParam("crawlId") Long id,
			@QueryParam("search") String search) {
		try {
			String condition = " idCrawlRecord = " + id;
			if(search!=null && !search.isEmpty()){
				condition = condition + 
						" and upper(name) like upper('%"+search+"%')";	
			}
			PaginationResponse pr = JatytaResourceHelper.getPagination(
					jatytaService, new JatytaStateName(), number, condition, 
					STATENAMES_PAGE_SIZE);
			return Response.ok(pr).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}
	/**
	 * Return the {@link List} of {@link JatytaStateName}  associated with the
	 * Crawljax record parameter.
	 * 
	 * @param id
	 *            The Crawljax record id.
	 * @return The {@link Response} with the {@link List} of 
	 * {@link JatytaStateName}
	 */
	@GET
	@Path("/statenames")
	public Response getStateNamesRecords(@QueryParam("crawlId") Long id) {
		try {
			String hql = " idCrawlRecord = " + id;
			return Response.ok(jatytaService.getEntityByHqlCondition(
					new JatytaStateName(), hql)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Returns the {@link List} of {@link JatytaFormValueRecord} with group by
	 * name, for the Crawljax Record Id parameter
	 * 
	 * @param id
	 *            The Crawljax Record Id parameter
	 * 
	 * @return In the {@link Response} a list of {@link JatytaFormValueRecord},
	 *         or ServerError if {@link JatytaException} occurs.
	 */
	@GET
	@Path("/formvaluesgroupby")
	public Response getFormValuesGroupBy(@QueryParam("id") Long id) {
		try {

			JatytaFormValueRecordDAO dao = (JatytaFormValueRecordDAO) 
					jatytaService.getDAO(new JatytaFormValueRecord());
			List<JatytaFormValueRecord> result = 
					dao.getFormValuesRecordGroupBy(id);

			// Obtain the jatyta configuration
			JatytaCrawlConfigurationDAO dao2 = (JatytaCrawlConfigurationDAO) 
					jatytaService
					.getDAO(new JatytaCrawlConfiguration());

			if (dao2.getAjustJSFElementIdByCrawljaxRecordId(id)) {
				JatytaFormValueRecordDAO.ajustFormValuesNameForJSF(result);
			}

			return Response.ok(result).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

}
