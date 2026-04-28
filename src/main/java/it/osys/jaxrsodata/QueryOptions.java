package it.osys.jaxrsodata;

/**
 * OData parameters and default values.
 *
 * <p>All fields are public for ease of use. Callers are responsible for
 * providing valid values: {@code top} must be &gt; 0, {@code skip} must be
 * &ge; 0. Setting {@code top} to 0 or a negative value may result in no
 * results being returned, depending on the JPA provider.</p>
 *
 * @author Domenico Briganti
 */
public class QueryOptions {

	public static final int DEFAULT_TOP = 500;

	/** The top. */
	public int top = 500;
	
	/** The skip. */
	public int skip = 0;
	
	/** The count. */
	public boolean count = false;
	
	/** The expand. */
	public String expand = null;
	
	/** The orderby. */
	public String orderby = null;
	
	/** The search. */
	public String search = null;
	
	/** The filter. */
	public String filter = null;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "QueryOptions [top=" + top + ", skip=" + skip + ", count=" + count + ", expand=" + expand + ", orderby=" + orderby
				+ ", search=" + search + ", filter=" + filter + "]";
	}

	/**
	 * Normalizes numeric fields to safe defaults.
	 *
	 * <p>Ensures {@code skip >= 0} and {@code top > 0}. If {@code top <= 0},
	 * it is reset to {@link #DEFAULT_TOP}.</p>
	 *
	 * @return this instance
	 */
	public QueryOptions normalize() {
		if (skip < 0)
			skip = 0;
		if (top <= 0)
			top = DEFAULT_TOP;
		return this;
	}

}