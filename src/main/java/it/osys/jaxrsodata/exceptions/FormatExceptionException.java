package it.osys.jaxrsodata.exceptions;

/**
 * Some OData function are not implemented
 * 
 * @author Domenico Briganti
 *
 */
@SuppressWarnings("serial")
public class FormatExceptionException extends RuntimeException {

	public FormatExceptionException(String message) {
		super(message);
	}

}
