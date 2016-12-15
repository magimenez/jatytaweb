package com.crawljax.web.jatyta.plugins;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.condition.NotRegexCondition;
import com.crawljax.condition.invariant.Invariant;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.HostInterface;
import com.crawljax.core.plugin.OnFireEventSuccessPlugin;
import com.crawljax.core.plugin.OnInvariantViolationPlugin;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.plugin.PreStateCrawlingPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateVertex;
import com.crawljax.plugins.crawloverview.CrawlOverviewException;
import com.crawljax.plugins.crawloverview.model.CandidateElementPosition;
import com.crawljax.web.jatyta.exception.JatytaException;
import com.crawljax.web.jatyta.knowledgebase.db.service.JatytaService;
import com.crawljax.web.jatyta.model.dao.JatytaBrokenLinksConfigurationDAO;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenLinksConfiguration;
import com.crawljax.web.jatyta.model.entities.JatytaBrokenState;
import com.crawljax.web.jatyta.plugins.util.BrokenLink;
import com.crawljax.web.jatyta.plugins.util.StateBroken;
import com.crawljax.web.jatyta.plugins.util.dom.HtmlElement;
import com.crawljax.web.jatyta.plugins.util.form.FormUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author betopindu
 */
public class BrokenLinksCheckerPlugin
		implements OnInvariantViolationPlugin, OnFireEventSuccessPlugin, PostCrawlingPlugin, PreStateCrawlingPlugin {
	// private static final String ERROR_PAGE_PATERN =
	// "schema-org.jatyta.errorPagePatern.text.true";
	private static final String BROKEN_STATES_FOLDER_NAME = "brokenstates";
	private static final String BROKEN_STATES_FILE_NAME = "brokenstates.html";
	private static final String COLOR_BROKEN_LINK = "red";

	private final ConcurrentMap<String, StateBroken> stateBrokenkList = Maps.newConcurrentMap();
	private BrokenLink lastBrokenLink = null;

	private final HostInterface hostInterface;
	private final VelocityEngine ve;

	private static final Logger LOG = LoggerFactory.getLogger(BrokenLinksCheckerPlugin.class);

	/**
	 * The {@link JatytaService} to persistence KB data.
	 */
	private final JatytaService jatytaService;

	/**
	 * The Crawljax configuration id.
	 */
	private final String configurationId;

	/**
	 * The Crawljax record id.
	 */
	private final Integer crawlRecordId;

	/**
	 * The map that save the style value to render the markup of broken links.
	 */
	private ConcurrentMap<Integer, Map<String, String>> candidateElementsStyle = Maps.newConcurrentMap();

	/**
	 * The map that save the image screenshot of broken links elements.
	 */
	private ConcurrentMap<Integer, Map<String, byte[]>> candidateElementsImage = Maps.newConcurrentMap();
	
	/**
	 * The default constructor.
	 * 
	 * @param builder
	 *            The {@link CrawljaxConfigurationBuilder} to load the
	 *            invariants.
	 * @param hostInterface
	 *            The {@link HostInterface} to define the working directory.
	 * @param configId
	 *            The Crawljax Configuration id to obtain the associated broken
	 *            links patterns.
	 * @param crawlRecordId
	 *            The Crawljax Record id.
	 * @param jatytaService
	 *            The {@link JatytaService} to obtain the data of patterns.
	 * @throws JatytaException
	 */
	public BrokenLinksCheckerPlugin(CrawljaxConfigurationBuilder builder, HostInterface hostInterface, String configId,
			Integer crawlRecordId, JatytaService jatytaService) throws JatytaException {
		LOG.info("Initialized the broken links checker plugin");
		this.jatytaService = jatytaService;
		this.configurationId = configId;
		this.crawlRecordId = crawlRecordId;
		this.hostInterface = hostInterface;
		loadInvariants(builder);
		ve = new VelocityEngine();

	}

	private void configureVelocity() {
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	}

	private void loadInvariants(CrawljaxConfigurationBuilder builder) throws JatytaException {

		// Instancia de la base de conocimiento
		// KnowledgeBase knowledgeBase = KnowledgeBaseFactory.getInstance();

		// Obtener los strings parametrizados como error de página
		// List<String> valueList =
		// knowledgeBase.getValueList(ERROR_PAGE_PATERN);
		JatytaBrokenLinksConfigurationDAO dao = (JatytaBrokenLinksConfigurationDAO) jatytaService
				.getDAO(new JatytaBrokenLinksConfiguration());
		List<String> valueList = dao.getBrokenLinksPatternsByConfigId(configurationId);

		if (valueList != null && valueList.size() > 0) {
			LOG.info("Agregando invariantes...");
			for (String value : valueList) {
				// Agregar el invariante que verifica que no esté presente el
				// texto
				builder.crawlRules().addInvariant(value, new NotRegexCondition(value));
			}
		}
	}

	@Override
	public void onInvariantViolation(Invariant invariant, CrawlerContext context) {

		StateVertex stateCaller = context.getCurrentState();

		if (lastBrokenLink != null) {
			if (lastBrokenLink.getStateCaller().equals(stateCaller)) {
				return;
			}
		}

		String invariantViolated = invariant.getDescription();

		lastBrokenLink = new BrokenLink(invariantViolated, stateCaller);

		LOG.info("Invariante violada!: " + invariantViolated);
	}

	
	
	/* (non-Javadoc)
	 * @see com.crawljax.core.plugin.OnFireEventSuccessPlugin#onFireEventSuccess(com.crawljax.core.CrawlerContext, com.crawljax.core.state.StateVertex, com.crawljax.core.state.StateVertex, java.util.List)
	 */
	@Override
	public void onFireEventSuccess(CrawlerContext context, StateVertex source, StateVertex target,
			List<Eventable> pathToSuccess) {
		//2016-07-19 mgimenez, correcion para los estados clonados.
		if (lastBrokenLink != null && lastBrokenLink.getStateCaller().equals(source)) {

			// Obtener el Xpath que condujo al enlace roto
			StateVertex stateCaller = lastBrokenLink.getStateCaller();
			Eventable eventable = getEventableByCandidateElementInState(context, stateCaller, target);
			
			String xpath = (eventable == null) ? null : eventable.getIdentification().getValue();

			// Setear el estado roto y el xpath
			lastBrokenLink.setStateBroken(target);
			lastBrokenLink.setXpath(xpath);
			// Se obtiene el style para el render del componente.
			Map<String, String> candidates = candidateElementsStyle.get(stateCaller.getId());
			String styleValue = candidates.get(xpath);
			if (styleValue == null) {
				styleValue = "";
			}
			lastBrokenLink.setStyleValue(styleValue);
			// Se obtiene la imagen del componente.
			Map<String, byte[]> images = candidateElementsImage.get(stateCaller.getId());
			byte[] img = images.get(xpath);
			lastBrokenLink.setImage(img);
			
			StateBroken stateBroken = lastBrokenLink.build();
			//se indica si el estado con error corresponde a un link.
			if(HtmlElement.A.getValue().equals(eventable.getElement().getTag())){
				stateBroken.setLink(true);
			}
			//mgimenez 30/06/2016 se cambio el has code por el name del estado.
			stateBrokenkList.putIfAbsent(stateBroken.getStateBroken(), stateBroken);

			lastBrokenLink = null;
		}
		
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
			StateVertex vertex) {

		Map<String, String> mapInput = new HashMap<String, String>();
		Map<String, byte[]> mapImage = new HashMap<String, byte[]>();
		// iterar por los candidatos y guardar su style value y su imagen.
		for (CandidateElement item : candidateElements) {
			if (context.getBrowser().elementExists(item.getIdentification())
				&& context.getBrowser().isVisible(item.getIdentification())
				&& context.getBrowser().getWebElement(item.getIdentification()).isDisplayed()) {
				
				// obtener web element.
				WebElement webElement = context.getBrowser().getWebElement(
						item.getIdentification());
				CandidateElementPosition elem = getElementPosition(
						webElement, item.getIdentification().getValue());
				String styleValue = getStyleToDrawElement(elem);
				mapInput.put(item.getIdentification().getValue(), styleValue);
				try {
					byte[] image = FormUtils.getElementScreenShot(webElement,
							context.getBrowser());
					mapImage.put(item.getIdentification().getValue(), image);
				} catch (IOException|RasterFormatException e) {
					LOG.error("Error in obtain screenshot element "
							+item.getIdentification().getValue()+" : "
					+e.getLocalizedMessage());
				}
			}
		}
		// agrega el listado de candidatos al map
		candidateElementsStyle.putIfAbsent(vertex.getId(), mapInput);
		// agrega el listado imagenes de candidatos al map
		candidateElementsImage.putIfAbsent(vertex.getId(), mapImage);
	}

	private Eventable getEventableByCandidateElementInState(CrawlerContext context, StateVertex source,
			StateVertex target) {

		StateFlowGraph sfg = context.getSession().getStateFlowGraph();
		String stateBrokenName = target.getName();

		for (Eventable eventable : sfg.getOutgoingClickables(source)) {
			String targetStateName = eventable.getTargetStateVertex().getName();
			if (targetStateName.equals(stateBrokenName)) {
				return eventable;
			}
		}
		return null;
	}

	/**
	 * Generated the report.
	 */
	@Override
	public void postCrawling(CrawlSession session, ExitStatus exitStatus) {
		LOG.debug("postCrawling");

		List<Map<String, String>> stateBrokenInfoList = Lists.newArrayListWithCapacity(stateBrokenkList.size());

		VelocityContext context = new VelocityContext();
		context.put("stateBrokenkList", stateBrokenkList);
		for (StateBroken state : stateBrokenkList.values()) {
			
			Map<String, String> stateBrokenInfo = new HashMap<String, String>();
			stateBrokenInfo.put("stateId", "" + state.getId());
			stateBrokenInfo.put("stateBroken", state.getStateBroken());
			stateBrokenInfo.put("urlBroken", state.getUrlBroken());
			stateBrokenInfo.put("xpath", state.getXpath());
			stateBrokenInfo.put("stateCaller", state.getStateCaller());

			stateBrokenInfoList.add(stateBrokenInfo);

			JatytaBrokenState bs = new JatytaBrokenState();
			bs.setIdCrawlRecord(this.crawlRecordId);
			bs.setStateBroken(state.getStateBrokenId());
			bs.setStateCaller(state.getStateCallerId());
			bs.setUrlBroken(state.getUrlBroken());
			bs.setXpath(state.getXpath());
			bs.setStyleValue(state.getStyleValue());
			bs.setImage(state.getImage());

			try {
				if(state.isLink()){
					//solo persiste si el xpath corresponde a un enlace <A></A>
					jatytaService.saveEntity(bs);	
				}
				
				
			} catch (JatytaException e) {
				LOG.error(e.getLocalizedMessage());
			}

		}
		context.put("elements", stateBrokenInfoList);
		writeHtmlForBrokenStates(context);

	}

	private void writeHtmlForBrokenStates(VelocityContext context) {
		File outputFolder = hostInterface.getOutputDirectory();
		File states = new File(outputFolder, BROKEN_STATES_FOLDER_NAME);
		boolean created = states.mkdir();
		checkArgument(created, "Could not create broken states dir");
		configureVelocity();

		File file = new File(states, BROKEN_STATES_FILE_NAME);
		String templateName = "state.html";

		try {
			Template templatee = ve.getTemplate(templateName);
			FileWriter writer = new FileWriter(file);
			templatee.merge(context, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new CrawlOverviewException("Could not write output broken state", e);
		}
	}

	/**
	 * Return the {@link CandidateElementPosition} from the {@link WebElement}
	 * and Xpath expression parameter
	 * 
	 * @author mgimenez
	 * @param webElement
	 *            the {@link WebElement} to obtain the location and size.
	 * @param xpath
	 *            The xpath expression of the element.
	 * @return The {@link CandidateElementPosition} to use for draw.
	 */
	private CandidateElementPosition getElementPosition(WebElement webElement, String xpath) {
		Point location = webElement.getLocation();
		Dimension size = webElement.getSize();
		CandidateElementPosition renderedCandidateElement = new CandidateElementPosition(xpath, location, size);
		if (location.getY() < 0) {
			LOG.warn("Weird positioning {} for {}", webElement.getLocation(), renderedCandidateElement.getXpath());
		}
		return renderedCandidateElement;
	}

	/**
	 * Return in a String with style value to render the mark of the broken
	 * link.
	 * 
	 * @author mgimenez
	 * @param element
	 *            The {@link CandidateElementPosition} to obtain the css
	 *            position.
	 * @return A String of format like "position: absolute; z-index: 20; left:
	 *         319px; top: 216px; width: 17px; height: 12px; border: 2px solid
	 *         red; cursor: normal;"
	 */
	private String getStyleToDrawElement(CandidateElementPosition element) {

		String result = "position: absolute; z-index: 20;" + " left: " + (element.getLeft() - 3) + "px;" + " top: "
				+ (element.getTop() - 3) + "px;" + " width: " + (element.getWidth() + 2) + "px;" + " height: "
				+ (element.getHeight() + 2) + "px;" + " border: 2px solid " + COLOR_BROKEN_LINK + "; cursor: pointer;";

		return result;

	}
	
	/**
	 * Verifies if the stateVertex parameter exist in the brokenState List.
	 * @param state The state to verify if is a broken state.
	 * @return true, if the state name is in the list, false otherwise.
	 */
	public boolean isBrokenState(StateVertex state){
		if(stateBrokenkList.containsKey(state.getName())){
			return true;
		}else{
			return false;
		}
		
	}

}
