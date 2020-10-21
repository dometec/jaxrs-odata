package it.osys.jaxrsodata;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class QueryOptionsParser {

	public static QueryOptions from(UriInfo info) {
		return from(info.getQueryParameters());
	}

	public static QueryOptions from(MultivaluedMap<String, String> queryParameters) {

		QueryOptions queryOptions = new QueryOptions();
		if (queryParameters.containsKey("$top") && !queryParameters.getFirst("$top").isEmpty())
			queryOptions.top = Integer.parseInt(queryParameters.getFirst("$top"));

		if (queryParameters.containsKey("$skip") && !queryParameters.getFirst("$skip").isEmpty())
			queryOptions.skip = Integer.parseInt(queryParameters.getFirst("$skip"));

		if (queryParameters.containsKey("$count") && !queryParameters.getFirst("$count").isEmpty())
			queryOptions.count = Boolean.parseBoolean(queryParameters.getFirst("$count"));

		if (queryParameters.containsKey("$expand") && !queryParameters.getFirst("$expand").isEmpty())
			queryOptions.expand = queryParameters.getFirst("$expand");

		if (queryParameters.containsKey("$orderby") && !queryParameters.getFirst("$orderby").isEmpty())
			queryOptions.orderby = queryParameters.getFirst("$orderby");

		if (queryParameters.containsKey("$filter") && !queryParameters.getFirst("$filter").isEmpty())
			queryOptions.filter = queryParameters.getFirst("$filter");

		if (queryParameters.containsKey("$search") && !queryParameters.getFirst("$search").isEmpty())
			queryOptions.search = queryParameters.getFirst("$search");

		return queryOptions;
	}

}
