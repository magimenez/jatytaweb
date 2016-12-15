/**
 * 
 */
package com.crawljax.core.plugin;

import java.util.List;

import com.crawljax.core.CrawlerContext;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateVertex;

/**
 * Plugin type that is called every time event that was requested to 
 * fire success firing.
 * @author mgimenez
 * @since 12/07/2016
 */
public interface OnFireEventSuccessPlugin extends Plugin {

	/**
	 * Method that is called when an event that was requested to fire success 
	 * firing.
	 * <p>
	 * This method can be called from multiple threads with different 
	 * {@link CrawlerContext}
	 * </p>
	 * 
	 * @param context
	 *            The per crawler context.
	 * @param source
	 *            the {@link StateVertex} source of the event.
	 * @param target the {@link StateVertex} target of the event.           
	 * @param pathToSuccess
	 *            the list of eventable lead TO this success eventable, 
	 *            the eventable excluded.
	 */
	void onFireEventSuccess(CrawlerContext context, StateVertex source, 
			StateVertex target, List<Eventable> pathToSuccess);
}
