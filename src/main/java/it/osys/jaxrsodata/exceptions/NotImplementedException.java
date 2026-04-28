package it.osys.jaxrsodata.exceptions;

/**
 * Some OData function are not implemented.
 *
 * @author Domenico Briganti
 */
@SuppressWarnings("serial")
public class NotImplementedException extends RuntimeException {

	/**
	 * Instantiates a new not implemented exception.
	 *
	 * @param message the message
	 */
	public NotImplementedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new not implemented exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}
