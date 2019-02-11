package it.osys.jaxrsodata.queryoptions;

/**
 * 
 * OData parameters and default values
 * 
 * @author Domenico Briganti
 *
 */
public class QueryOptions {

	public int top = 500;
	public int skip = 0;
	public boolean count = false;
	public String expand = null;
	public String orderby = null;
	public String search = null;
	public String filter = null;

	@Override
	public String toString() {
		return "QueryOptions [top=" + top + ", skip=" + skip + ", count=" + count + ", expand=" + expand + ", orderby=" + orderby
				+ ", search=" + search + ", filter=" + filter + "]";
	}

}