/**
 * 
 */
package com.crawljax.web.jatyta.plugins.util.http;

/**
 * Enum for common HTTP Errors Code.
 * @author mgimenez
 *
 */
public enum HttpError {
	
	UNAUTHORIZED("HTTP ERROR 401"),
	BAD_REQUEST("HTTP ERROR 400"),
	FORBIDDEN("HTTP ERROR 403"),
	NOT_FOUND("HTTP ERROR 404"),
	INTERNAL_SERVER_ERROR("HTTP ERROR 500");
	private final String value;

	private HttpError(final String value) {
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
