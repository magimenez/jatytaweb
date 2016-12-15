package com.crawljax.web.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaCrawlConfigurationDAO;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenLinksConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaCrawlConfiguration;
import com.crawljax.web.jatyta.plugins.util.http.HttpError;
import com.crawljax.web.model.Configuration;
import com.crawljax.web.model.Configurations;
import com.crawljax.web.model.Plugins;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/configurations")
public class ConfigurationsResource {

	private final Configurations configurations;
	private final Plugins plugins;
	/**
	 * For create jatyta Configuration by default.
	 */
	private final JatytaService jatytaService;

	@Inject
	ConfigurationsResource(Configurations configurations, Plugins plugins, 
			JatytaService jatytaService) {
		this.configurations = configurations;
		this.plugins = plugins;
		this.jatytaService = jatytaService;
	}

	@GET
	public Response getConfigurations() {
		return Response.ok(configurations.getConfigList()).build();
	}

	@POST
	public Response addConfiguration(Configuration config) {
		config = configurations.add(config);
		//23/07/2016 mgimenez, verificar si ya existe configuracion de jatyta.
		try {
			JatytaCrawlConfigurationDAO dao = (JatytaCrawlConfigurationDAO) 
					jatytaService.getDAO(new JatytaCrawlConfiguration());
			JatytaCrawlConfiguration jatyta = 
					dao.getConfigurationByCrawljaxConfigId(config.getId());
			if(jatyta==null){
				//guardar configuracion por defecto
				jatyta = new JatytaCrawlConfiguration();
				jatyta.setConfigurationId(config.getId());
				jatytaService.saveEntity(jatyta);
				//guardar broken link por defecto.
				for (HttpError error : HttpError.values()) {
					JatytaBrokenLinksConfiguration brokenLinkConfig = 
							new JatytaBrokenLinksConfiguration();
					brokenLinkConfig.setPattern(error.getValue());
					brokenLinkConfig.setConfigurationId(config.getId());
					jatytaService.saveEntity(brokenLinkConfig);
				}
			}
		} catch (JatytaException e) {
			return Response.serverError().build();
		}
		return Response.ok(config).build();
	}

	@GET
	@Path("/new/{id}")
	public Response getNewConfiguration(@PathParam("id") String id) {
		Configuration config = new Configuration();
		if(id != null && !id.isEmpty()) {
			config = configurations.getCopyOf(id);
		}
		return Response.ok(config).build();
	}

	@GET
	@Path("/new")
	public Response getNewConfiguration() {
		Configuration config = new Configuration();
		return Response.ok(config).build();
	}

	@GET
	@Path("{id}")
	public Response getConfiguration(@PathParam("id") String id) {
		Response r;
		Configuration config = configurations.findByID(id);
		if (config != null)
			r = Response.ok(config).build();
		else
			r = Response.serverError().build();
		return r;
	}

	@PUT
	@Path("{id}")
	public Response updateConfiguration(Configuration config) {
		config = configurations.update(config);
		//TODO mgimenez, aca se debe actualizar las configuraciones de jatyta.
		return Response.ok(config).build();
	}

	@DELETE
	@Path("{id}")
	public Response removeConfiguration(Configuration config) {
		config = configurations.remove(config);
		return Response.ok(config).build();
	}
	
}
