package it.osys.jaxrsodata;

/**
 * OData parameters and default values.
 *
 * @author Domenico Briganti
 */
public class QueryOptions {

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

}