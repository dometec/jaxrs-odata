package it.osys.jaxrsodata;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/*
 * QueryOptionsParser extension to sort data with a unique field concatenated to the choice of the selected field
 */
public class QueryOptionsParserEnrichOrderByWithIdentifer extends QueryOptionsParser {

	public static QueryOptions from(UriInfo info, String identifier) {
		return from(info.getQueryParameters(), identifier);
	}

	public static QueryOptions from(MultivaluedMap<String, String> queryParameters, String identifier) {

		QueryOptions queryOptions = new QueryOptions();

		queryOptions = from(queryParameters);

		if (queryOptions.orderby != null && !queryOptions.orderby.isBlank() && !queryOptions.orderby.contains(identifier)) {
			queryOptions.orderby = queryOptions.orderby + ", " + identifier + " asc";
		}

		return queryOptions;
	}
}