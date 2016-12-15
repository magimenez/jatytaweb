/**
 * 
 */
package com.crawljax.web.jatyta.exception;

/**
 * @author mgimenez Class to caught exception of the Application.
 */
public class JatytaException extends Exception {
	public static final String _PERSISTENCE_DELETE_ERROR_MESSAGE ="Error al querer eliminar datos en la Base de datos";
	public static final String _PERSISTENCE_INSERT_ERROR_MESSAGE ="Error al querer guardar datos en la Base de datos";
	public static final String _PERSISTENCE_UPDATE_ERROR_MESSAGE ="Error al querer actualizar datos en la Base de datos";
	public static final String _PERSISTENCE_SELECT_ERROR_MESSAGE ="Error al querer obtener datos en la Base de datos";
	public static final String _PERSISTENCE_DAO_ERROR_MESSAGE ="Error al querer obtener el DAO de la entidad parametro";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JatytaException() {
		super();
	}

	public JatytaException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JatytaException(String message, Throwable cause) {
		super(message, cause);
	}

	public JatytaException(String message) {
		super(message);
	}

	public JatytaException(Throwable cause) {
		super(cause);
	}

}
