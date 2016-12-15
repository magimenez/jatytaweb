/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.dom;

/**
 * @author mgimenez
 * 
 *         HTML Element suported by Jatyta.
 */
public enum HtmlElement {

	/**
	 * A html element.
	 */
	A("A"),
	/**
	 * ABBR html element.
	 */
	ABBR("ABBR"),
	/**
	 * ADDRESS html element.
	 */
	ADDRESS("ADDRESS"),
	/**
	 * AREA html element.
	 */
	AREA("AREA"),
	/**
	 * ARTICLE html element.
	 */
	ARTICLE("ARTICLE"),
	/**
	 * ASIDE html element.
	 */
	ASIDE("ASIDE"),
	/**
	 * AUDIO html element.
	 */
	AUDIO("AUDIO"),
	/**
	 * BUTTON html element.
	 */
	BUTTON("BUTTON"),
	/**
	 * CANVAS html element.
	 */
	CANVAS("CANVAS"),
	/**
	 * DETAILS html element.
	 */
	DETAILS("DETAILS"),
	/**
	 * DIV html element.
	 */
	DIV("DIV"),
	/**
	 * FIGURE html element.
	 */
	FIGURE("FIGURE"),
	/**
	 * FOOTER html element.
	 */
	FOOTER("FOOTER"),
	/**
	 * FORM html element.
	 */
	FORM("FORM"),
	/**
	 * HEADER html element.
	 */
	HEADER("HEADER"),
	/**
	 * IMG html element.
	 */
	IMG("IMG"),
	/**
	 * INPUT html element.
	 */
	INPUT("INPUT"),
	/**
	 * LABEL html element.
	 */
	LABEL("LABEL"),
	/**
	 * LI html element.
	 */
	LI("LI"),
	/**
	 * NAV html element.
	 */
	NAV("NAV"),
	/**
	 * OL html element.
	 */
	OL("OL"),
	/**
	 * SECTION html element.
	 */
	SECTION("SECTION"),
	/**
	 * SELECT html element.
	 */
	SELECT("SELECT"),
	/**
	 * SPAN html element.
	 */
	SPAN("SPAN"),
	/**
	 * SUMMARY html element.
	 */
	SUMMARY("SUMMARY"),
	/**
	 * TABLE html element.
	 */
	TABLE("TABLE"),
	/**
	 * TD html element.
	 */
	TD("TD"),
	/**
	 * TEXTAREA html element.
	 */
	TEXTAREA("TEXTAREA"),
	/**
	 * TH html element.
	 */
	TH("TH"),
	/**
	 * TR html element.
	 */
	TR("TR"),
	/**
	 * UL html element.
	 */
	UL("UL"),
	/**
	 * VIDEO html element.
	 */
	VIDEO("VIDEO");

	private final String value;

	private HtmlElement(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}
}
