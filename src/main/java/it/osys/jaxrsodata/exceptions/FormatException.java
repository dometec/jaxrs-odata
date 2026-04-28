package it.osys.jaxrsodata.exceptions;

/**
 * Exception thrown when an OData expression is syntactically valid but not supported
 * or cannot be converted to a valid JPA Criteria predicate/order.
 *
 * <p>This is the preferred exception type. {@link FormatExceptionException} is kept
 * for backward compatibility.</p>
 */
@SuppressWarnings("serial")
public class FormatException extends RuntimeException {

	public FormatException(String message) {
		super(message);
	}

	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}
}

