package com.crawljax.web.jatyta.plugins.util;

import com.crawljax.core.state.StateVertex;


/**
 * 
 * @author betopindu
 */
public class BrokenLink {
	private String invariantViolated;
	private StateVertex stateCaller;
	private StateVertex stateBroken;
	private String xpath;
	private String styleValue;
	private byte[] image;
	
	public BrokenLink() {

	}

	public BrokenLink(String invariantViolated, StateVertex stateCaller) {
		this.invariantViolated = invariantViolated;
		this.stateCaller = stateCaller;
	}
	
	public String getInvariantViolated() {
		return invariantViolated;
	}
	
	public void setInvariantViolated(String invariantViolated) {
		this.invariantViolated = invariantViolated;
	}

	public StateVertex getStateCaller() {
		return stateCaller;
	}
	
	public void setStateCaller(StateVertex stateCaller) {
		this.stateCaller = stateCaller;
	}
	
	public StateVertex getStateBroken() {
		return stateBroken;
	}
	
	public void setStateBroken(StateVertex stateBroken) {
		this.stateBroken = stateBroken;
	}
	
	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	public StateBroken build() {
		return new StateBroken(this);
	}

	/**
	 * @return the styleValue
	 */
	public String getStyleValue() {
		return styleValue;
	}

	/**
	 * @param styleValue the styleValue to set
	 */
	public void setStyleValue(String styleValue) {
		this.styleValue = styleValue;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	
}