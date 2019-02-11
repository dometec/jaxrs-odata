package it.osys.jaxrsodata.exceptions;

/**
 * Some OData function are not implemented
 * 
 * @author Domenico Briganti
 *
 */
@SuppressWarnings("serial")
public class NotImplementedException extends Exception {

	public NotImplementedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotImplementedException(String message) {
		super(message);
	}

	public NotImplementedException(Throwable cause) {
		super(cause);
	}

	public NotImplementedException() {
		super();
	}
}
