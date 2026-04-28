package it.osys.jaxrsodata.exceptions;

/**
 * Some OData function are not implemented.
 *
 * @author Domenico Briganti
 */
@SuppressWarnings("serial")
public class FormatExceptionException extends RuntimeException {

	/**
	 * Instantiates a new format exception exception.
	 *
	 * @param message the message
	 */
	public FormatExceptionException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new format exception exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public FormatExceptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
