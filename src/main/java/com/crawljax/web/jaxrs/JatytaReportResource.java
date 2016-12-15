/**
 * 
 */
package com.crawljax.web.jaxrs;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.web.jatyta.knowledgebase.KnowledgeBaseFactory;
import com.crawljax.web.jatyta.knowledgebase.db.util.HibernateUtil;
import com.crawljax.web.jatyta.reports.JasperReportsConfiguration;
import com.google.inject.Singleton;

/**
 * Resource to generate Jasper Reports.
 * 
 * @author mgimenez
 * 
 */
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Path("/rest/jatyta/report")
public class JatytaReportResource {

	private static final Logger LOG = LoggerFactory
			.getLogger(JatytaReportResource.class);

	/**
	 * @param servletContext
	 *            The {@link ServletContext} to obtain the jasper file.
	 * @param request
	 *            The {@link HttpServletRequest} to set the {@link JasperPrint}
	 *            into the session.
	 * @return The {@link Response} OK if the {@link JasperPrint} is added
	 *         correctly to the session.
	 */
	@GET
	@Path("/crawlsummary")
	@Produces("application/pdf")
	public Response getCrawlSumaryReport(
			@Context ServletContext servletContext,
			@Context HttpServletRequest request) {

		try {
			String reportName = "JatytaCrawlSummary";

			String reportFileName = servletContext
					.getRealPath(JasperReportsConfiguration.JASPER_FILES_PATH
							+ reportName + ".jasper");
			String pdfFileName = System.getProperty("user.dir")
					+ File.separator + KnowledgeBaseFactory.RESOURCES_PATH
					+ File.separator + reportName + ".pdf";

			JasperPrint jasperPrint = fillReport(reportFileName, request);
			File result = getPdfReport(pdfFileName, jasperPrint);
			ResponseBuilder response = Response.ok((Object) result);
			response.header("Content-Disposition", "attachment; filename="
					+ reportName + ".pdf");
			return response.build();
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
			return Response.serverError().build();
		}
	}

	/**
	 * Fill the filename jasper report and set the {@link JasperPrint} to the
	 * session.
	 * 
	 * @param reportFileName
	 *            The filename of the jasper file.
	 * @param request
	 *            The {@link HttpServletRequest} to set the {@link JasperPrint}
	 *            into the session.
	 * @throws JRException
	 *             If a error occurs with jasper reports.
	 * @throws SQLException
	 *             If a error occurs with the data base.
	 */
	private JasperPrint fillReport(String reportFileName,
			HttpServletRequest request) throws JRException, SQLException {
		Map<String, String[]> params = request.getParameterMap();
		File reportFile = new File(reportFileName);
		if (!reportFile.exists())
			throw new JRRuntimeException("File WebappReport.jasper not found. "
					+ "The report design must be compiled first.");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ReportTitle", "Jatyta Report");
		parameters.put("BaseDir", reportFile.getParentFile());
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			if (values.length == 1) {
				parameters.put(key, values[0]);
			} else if (values.length > 1) {
				parameters.put(key, values);
			}
		}

		Configuration configuration = new Configuration();
		configuration.configure(HibernateUtil.HIBERNATE_CONFIG_FILE);
		ServiceRegistryImplementor serviceRegistry = (ServiceRegistryImplementor) new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		C3P0ConnectionProvider hcp = new C3P0ConnectionProvider();
		hcp.injectServices(serviceRegistry);

		hcp.configure(configuration.getProperties());

		JasperPrint jasperPrint = JasperFillManager.fillReport(reportFileName,
				parameters, hcp.getConnection());

		return jasperPrint;

	}

	/**
	 * Generate a PDF File from {@link JasperPrint} parameter and the fileName.
	 * 
	 * @param fileName
	 *            The file name parameter.
	 * @param jasperPrint
	 *            The {@link JasperPrint} parameter.
	 * @throws ServletException
	 *             If a error occurs with the jasper export.
	 * @return a PDF File
	 */
	private File getPdfReport(String fileName, JasperPrint jasperPrint)
			throws ServletException {
		List<JasperPrint> list = new ArrayList<JasperPrint>(1);
		list.add(jasperPrint);
		JRPdfExporter exporter = new JRPdfExporter(
				DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(SimpleExporterInput.getInstance(list));

		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
				fileName));
		File reportFile = null;
		try {
			exporter.exportReport();
			reportFile = new File(fileName);

		} catch (JRException e) {
			throw new ServletException(e);
		}
		return reportFile;
	}
}
