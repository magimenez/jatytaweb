package com.crawljax.web.jatyta.plugins.util;

import javax.annotation.concurrent.Immutable;

import com.crawljax.core.state.StateVertex;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * 
 * @author betopindu
 */

@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
public class StateBroken {

	private final String stateBroken;
	private Integer stateBrokenId = null;
	private final String urlBroken;
	private final String xpath;
	private final String stateCaller;
	private Integer stateCallerId = null;
	private final int id;
	private final String styleValue;
	private final byte[] image;
	private boolean isLink = false;

	public StateBroken(BrokenLink brokenLink) {
		StateVertex stateBroken = brokenLink.getStateBroken();
		StateVertex stateCaller = brokenLink.getStateCaller();
		this.stateBroken = stateBroken.getName();
		this.stateBrokenId = stateBroken.getId();
		this.urlBroken = stateBroken.getUrl();
		this.xpath = brokenLink.getXpath();
		this.stateCaller = stateCaller.getName();
		this.stateCallerId = stateCaller.getId();
		this.id = stateBroken.getId();
		this.styleValue = brokenLink.getStyleValue();
		this.image = brokenLink.getImage();
	}

	@JsonCreator
	public StateBroken(@JsonProperty("stateBroken") String stateBroken,
			@JsonProperty("urlBroken") String urlBroken,
			@JsonProperty("xpath") String xpath,
			@JsonProperty("stateCaller") String stateCaller,
			@JsonProperty("id") int id,
			@JsonProperty("styleValue") String styleValue,
			@JsonProperty("image") byte[] image) {
		super();
		this.stateBroken = stateBroken;
		this.urlBroken = urlBroken;
		this.xpath = xpath;
		this.stateCaller = stateCaller;
		this.id = id;
		this.styleValue = styleValue;
		this.image = image;
	}

	public String getStateBroken() {
		return stateBroken;
	}

	public String getUrlBroken() {
		return urlBroken;
	}

	public String getXpath() {
		return xpath;
	}

	public String getStateCaller() {
		return stateCaller;
	}

	public int getId() {
		return id;
	}

	/**
	 * @return the stateBrokenId
	 */
	public Integer getStateBrokenId() {
		return stateBrokenId;
	}

	/**
	 * @param stateBrokenId
	 *            the stateBrokenId to set
	 */
	public void setStateBrokenId(Integer stateBrokenId) {
		this.stateBrokenId = stateBrokenId;
	}

	/**
	 * @return the stateCallerId
	 */
	public Integer getStateCallerId() {
		return stateCallerId;
	}

	/**
	 * @param stateCallerId
	 *            the stateCallerId to set
	 */
	public void setStateCallerId(Integer stateCallerId) {
		this.stateCallerId = stateCallerId;
	}

	/**
	 * @return the styleValue
	 */
	public String getStyleValue() {
		return styleValue;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}
	
	/**
	 * @return the isLink
	 */
	public boolean isLink() {
		return isLink;
	}

	/**
	 * @param isLink the isLink to set
	 */
	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(stateBroken, urlBroken, xpath, stateCaller, id);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof StateBroken) {
			StateBroken that = (StateBroken) object;
			return Objects.equal(this.id, that.id)
					&& Objects.equal(this.stateBroken, that.stateBroken)
					&& Objects.equal(this.urlBroken, that.urlBroken)
					&& Objects.equal(this.xpath, that.xpath)
					&& Objects.equal(this.stateCaller, that.stateCaller);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("stateBroken", stateBroken)
				.add("id", id).add("urlBroken", urlBroken).add("xpath", xpath)
				.add("stateCaller", stateCaller).toString();
	}
}