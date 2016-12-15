/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.ItemPropDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.ItemTypeDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.PropNameDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.PropValueDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.dao.SchemaDAO;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemProp;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.ItemType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.NativeType;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropName;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.PropValue;
import com.crawljax.web.jatyta.knowledgebase.db.model.entities.Schema;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Resource to obtain data from Knowledge Base.
 * 
 * @author mgimenez
 * 
 */
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/jatyta/kb")
public class JatytaKnowledgeBaseResource {

	public static final Integer PAGE_SIZE = 10;
	
	private final JatytaService jatytaService;

	@Inject
	JatytaKnowledgeBaseResource(JatytaService jatytaService) {
		this.jatytaService = jatytaService;
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/schemas")
	public Response getSchemas() {
		try {
			return Response.ok(jatytaService.getAllEntityValues(new Schema()))
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}
	
	/**
	 * Return the Page  list of {@link Schema} in the KnowledgeBase
	 * @param number The page number
	 * @param search The search condition.
	 * @return The {@link List} of {@link Schema}.
	 */
	@GET
	@Path("/schemas/{page}")
	public Response getSchemas(@PathParam("page") Integer number, 
			@QueryParam("search") String search ){
		try {
			String condition = "";
			if(search!=null && !search.isEmpty()){
				condition = " upper(schemaName) like upper('%"+search+"%')";	
			}
			PaginationResponse pr = JatytaResourceHelper.getPagination(
					jatytaService, new Schema(), number, condition, 
					PAGE_SIZE);
			return Response
					.ok(pr)
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/schema")
	public Response getSchema(@QueryParam("id") int id) {
		Schema schema;
		try {
			schema = (Schema) jatytaService.getEntityByID(new Schema(),
					new Long(id));
			return Response.ok(schema).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/newschema")
	public Response getNewSchema() {
		Schema schema = new Schema();
		return Response.ok(schema).build();
	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	@PUT
	@Path("/updateschema")
	public Response updateSchema(Schema schema) {
		try {
			schema = (Schema) jatytaService.saveEntity(schema);
			return Response.ok(schema).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Delete the {@link Schema} parameter and the {@link ItemType},
	 * {@link ItemProp}, and {@link PropValue} associated.
	 * 
	 * @param schema
	 *            The {@link Schema} to be deleted.
	 * @return The {@link Schema} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deleteschema")
	public Response deleteSchema(Schema schema) {
		try {
			SchemaDAO dao = (SchemaDAO) jatytaService.getDAO(schema);
			dao.deleteSchemaAndDataAssociated(schema.getId());
			return Response.ok(schema).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/itemtypes")
	public Response getItemTypes() {
		try {
			return Response
					.ok(jatytaService.getAllEntityValues(new ItemType()))
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param idSchema
	 * @return
	 */
	@GET
	@Path("/itemtypesbyschema")
	public Response getItemTypesBySchema(@QueryParam("idSchema") Long idSchema) {
		try {
			return Response.ok(
					jatytaService.getEntityByHqlCondition(new ItemType(),
							" schema.id = " + idSchema)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}
	
	/**
	 * Return the Page list {@link ItemType} of {@link Schema} 
	 * parameter in the KnowledgeBase
	 * @param number The page number.
	 * @param idSchema The id for schema filter.
	 * @param search The condition filter.
	 * @return The {@link Pagination} with {@link ItemType} entities.
	 */
	@GET
	@Path("/itemtypesbyschema/{page}")
	public Response getItemTypesBySchema(@PathParam("page") Integer number, 
			@QueryParam("idSchema") Long idSchema,
			@QueryParam("search") String search ){
		try {
			String condition = " schema.id = " + idSchema;
			if(search!=null && !search.isEmpty()){
				condition = condition + 
						" and upper(typeName) like upper('%"+search+"%')";	
			}
			PaginationResponse pr = JatytaResourceHelper.getPagination(
					jatytaService, new ItemType(), number, condition, 
					PAGE_SIZE);
			return Response
					.ok(pr)
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/itemtype")
	public Response getItemType(@QueryParam("id") Long id) {

		try {
			ItemType itemType = (ItemType) jatytaService.getEntityByID(
					new ItemType(), new Long(id));

			return Response.ok(itemType).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * 
	 * @param itemType
	 * @return
	 */
	@PUT
	@Path("/updateitemtype")
	public Response updateItemType(ItemType itemType) {
		try {
			itemType = (ItemType) jatytaService.saveEntity(itemType);
			return Response.ok(itemType).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/newitemtype")
	public Response getNewItemType() {
		ItemType itemType = new ItemType();
		return Response.ok(itemType).build();
	}

	/**
	 * Delete the {@link ItemType} parameter and the {@link ItemProp}, and
	 * {@link PropValue} associated.
	 * 
	 * @param itemtype
	 *            The {@link ItemType} to be deleted.
	 * @return The {@link ItemType} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deleteitemtype")
	public Response deleteItemType(ItemType itemtype) {
		try {
			ItemTypeDAO dao = (ItemTypeDAO) jatytaService.getDAO(itemtype);
			dao.deleteItemTypeAndDataAssociated(itemtype.getIdItemType());
			return Response.ok(itemtype).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/itemprops")
	public Response getItemProps() {
		try {
			return Response
					.ok(jatytaService.getAllEntityValues(new ItemProp()))
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param idItemType
	 * @return
	 */
	@GET
	@Path("/itempropsbyitemtype")
	public Response getItemPropsByItemType(
			@QueryParam("idItemType") Long idItemType) {
		try {
			return Response.ok(
					jatytaService.getEntityByHqlCondition(new ItemProp(),
							" itemType.id = " + idItemType)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}
	
	/**
	 * Return the Page list  of {@link ItemProp} for {@link ItemType}
	 *  parameter in the KnowledgeBase
	 * @param number The page number.
	 * @param idItemType The id for {@link ItemType} filter.
	 * @param search The condition filter.
	 * @return The {@link Pagination} with {@link ItemProp} entities.
	 */
	@GET
	@Path("/itempropsbyitemtype/{page}")
	public Response getItemPropsByItemType(@PathParam("page") Integer number, 
			@QueryParam("idItemType") Long idItemType,
			@QueryParam("search") String search ){
		try {
			String condition =  " itemType.id = " + idItemType;
			if(search!=null && !search.isEmpty()){
				condition =condition + " and upper(propName.name) "
						+ " like upper('%"+search+"%')";	
			}
			PaginationResponse pr = JatytaResourceHelper.getPagination(
					jatytaService, new ItemProp(), number, condition, 
					PAGE_SIZE);
			return Response
					.ok(pr)
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param itemProp
	 * @return
	 */
	@PUT
	@Path("/updateitemprop")
	public Response updateItemProp(ItemProp itemProp) {
		try {
			itemProp = (ItemProp) jatytaService.saveEntity(itemProp);
			return Response.ok(itemProp).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/newitemprop")
	public Response getNewItemProp() {
		ItemProp itemProp = new ItemProp();
		return Response.ok(itemProp).build();
	}

	/**
	 * Delete the {@link ItemProp} parameter and the {@link PropValue}
	 * associated.
	 * 
	 * @param itemprop
	 *            The {@link ItemProp} to be deleted.
	 * @return The {@link ItemProp} deleted, or ServerError if
	 *         {@link JatytaException} occurs.
	 */
	@DELETE
	@Path("/deleteitemprop")
	public Response deleteItemProp(ItemProp itemprop) {
		try {
			ItemPropDAO dao = (ItemPropDAO) jatytaService.getDAO(itemprop);
			dao.deleteItemPropAndDataAssociated(itemprop.getIdItemProp());
			return Response.ok(itemprop).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return the list of {@link PropName} in the KnowledgeBase
	 * 
	 * @return The {@link List} of {@link PropName}.
	 */
	@GET
	@Path("/propnames")
	public Response getPropNames() {
		try {
			return Response
					.ok(jatytaService.getAllEntityValues(new PropName()))
					.build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}


	/**
	 * Return the list of {@link PropName} in the KnowledgeBase
	 * @param number The page number.
	 * @param search The search condition.
	 * @return The {@link List} of {@link PropName}.
	 */
	@GET
	@Path("/propnames/{page}")
	public Response getPropNames(@PathParam("page") Integer number, 
			@QueryParam("search") String search ){
		try {

			String condition = "";
			if(search!=null && !search.isEmpty()){
				condition = " upper(name) like upper('%"+search+"%')";	
			}
			
			PaginationResponse pr = JatytaResourceHelper.getPagination(
					jatytaService, new PropName(), number, condition, 
					PAGE_SIZE);
			return Response
					.ok(pr)
					.build();

		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the {@link PropName} in the KnowledgeBase with the id parameter
	 * 
	 * @param id
	 *            The idPropName of the {@link PropName}
	 * @return The {@link PropName}.
	 */
	@GET
	@Path("/propname")
	public Response getPropName(@QueryParam("id") Long id) {
		try {
			PropName propName = (PropName) jatytaService.getEntityByID(
					new PropName(), id);
			return Response.ok(propName).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return a new Instance of {@link PropName}
	 * 
	 * @return A new instance of {@link PropName}.
	 */
	@GET
	@Path("/newpropname")
	public Response getNewPropName() {
		PropName propName = new PropName();
		return Response.ok(propName).build();
	}

	/**
	 * Save or update the {@link PropName} parameter.
	 * 
	 * @param propName
	 *            The {@link PropName} to save or update.
	 * @return The {@link PropName} saved or updated.
	 */
	@PUT
	@Path("/updatepropname")
	public Response updatePropName(PropName propName) {
		try {
			propName = (PropName) jatytaService.saveEntity(propName);
			return Response.ok(propName).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Delete the {@link PropName} from knowledge base and the {@link PropValue}
	 * associated.
	 * 
	 * @param propName
	 *            The {@link PropName} to delete.
	 * @return The deleted {@link PropName}.
	 */
	@DELETE
	@Path("/deletepropname")
	public Response deletePropName(PropName propName) {
		try {

			PropNameDAO dao = (PropNameDAO) jatytaService.getDAO(propName);
			dao.deletePropnameAndValues(propName.getIdPropName());
			return Response.ok(propName).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return the {@link List} of all {@link PropValue} from knowledge base.
	 * 
	 * @return The {@link List} of {@link PropValue}.
	 */
	@GET
	@Path("/propvalues")
	public Response getPropValues() {
		try {
			return Response.ok(
					jatytaService.getAllEntityValues(new PropValue())).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the list of {@link PropValue} associated to the {@link ItemProp}
	 * with the id parameter.
	 * 
	 * @param idItemProp
	 *            The id of the {@link ItemProp}
	 * @return The {@link List} of {@link PropValue}.
	 */
	@GET
	@Path("/propvaluesbyitemprop")
	public Response getPropValuesByItemProp(
			@QueryParam("idItemProp") Long idItemProp) {
		try {
			return Response.ok(
					jatytaService.getEntityByHqlCondition(new PropValue(),
							" propName.id in "
									+ " (select ip.propName.id from "
									+ "ItemProp ip where ip.id = " + idItemProp
									+ " )")).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return the list of {@link PropValue} associated to the {@link PropName}
	 * with the id parameter.
	 * 
	 * @param idPropName
	 *            The id of the {@link PropName}
	 * @return The {@link List} of {@link PropValue}.
	 */
	@GET
	@Path("/propvaluesbypropname")
	public Response getPropValuesByPropName(
			@QueryParam("idPropName") Long idPropName) {
		try {
			return Response.ok(
					jatytaService.getEntityByHqlCondition(new PropValue(),
							" propName.id = " + idPropName)).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Return a new {@link PropValue} instance.
	 * 
	 * @return The {@link PropValue} instance.
	 */
	@GET
	@Path("/newpropvalue")
	public Response getNewPriopValue() {
		PropValue propValue = new PropValue();
		return Response.ok(propValue).build();
	}

	/**
	 * Return the {@link PropValue} instance with the id parameter.
	 * 
	 * @param id
	 *            The id parameter.
	 * @return The {@link PropValue} instance.
	 */
	@GET
	@Path("/propvalue")
	public Response getPropValue(@QueryParam("idPropValues") Long id) {
		try {
			PropValue propValue = (PropValue) jatytaService.getEntityByID(
					new PropValue(), new Long(id));
			return Response.ok(propValue).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Save or update the {@link PropValue} parameter.
	 * 
	 * @param propValue
	 *            The {@link PropValue} parameter.
	 * @return The updated {@link PropValue}.
	 */
	@PUT
	@Path("/updatepropvalue")
	public Response updatePropValue(PropValue propValue) {
		try {
			propValue = (PropValue) jatytaService.saveEntity(propValue);
			return Response.ok(propValue).build();
		} catch (JatytaException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).
					entity(e.getLocalizedMessage()).build();
		}
	}

	/**
	 * Delete the {@link PropValue} parameter from knowledge base.
	 * 
	 * @param propValue
	 *            The {@link PropValue} parameter.
	 * @return The deleted {@link PropValue}.
	 */
	@DELETE
	@Path("/deletepropvalue")
	public Response deleteItemProp(PropValue propValue) {
		try {
			jatytaService.deleteEntity(propValue);
			return Response.ok(propValue).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * Return the {@link List} of all {@link NativeType} in the knowledge base.
	 * 
	 * @return The {@link List} of {@link NativeType}.
	 */
	@GET
	@Path("/nativetypes")
	public Response getNativeTypes() {
		try {
			return Response.ok(
					jatytaService.getAllEntityValues(new NativeType())).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/nativetype")
	public Response getNativeType(@QueryParam("id") Long id) {

		try {
			NativeType nativeType = (NativeType) jatytaService.getEntityByID(
					new NativeType(), new Long(id));
			return Response.ok(nativeType).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}

	}

	/**
	 * 
	 * @param nativeType
	 * @return
	 */
	@PUT
	@Path("/updatenativetype")
	public Response updatePropValue(NativeType nativeType) {
		try {
			nativeType = (NativeType) jatytaService.saveEntity(nativeType);
			return Response.ok(nativeType).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

	/**
	 * Persist the {@link List} of {@link PropValue} and the associated
	 * {@link PropName}. If the {@link PropName} associated or {@link PropValue}
	 * exists, don't persist the object.
	 * 
	 * @param propValues
	 *            The {@link List} of {@link PropValue} to persist.
	 * @return
	 */
	@PUT
	@Path("/savepropnamesandvalues")
	public Response savePropNamesAndValues(List<PropValue> propValues) {
		try {

			PropValueDAO dao = (PropValueDAO) jatytaService
					.getDAO(new PropValue());
			Map<String, Integer> result = dao
					.savePropNamesAndPropValues(propValues);
			return Response.ok(result).build();
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
	}

}
