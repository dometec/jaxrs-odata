package it.osys.jaxrsodata;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import it.osys.jaxrsodata.queryoptions.QueryOptions;

public class QueryOptionsFilter implements ContainerRequestFilter {

	public static final ThreadLocal<QueryOptions> threadQueryOption = new ThreadLocal<QueryOptions>();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		threadQueryOption.set(from(requestContext.getUriInfo()));
	}

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

		if (queryParameters.containsKey("$search") && !queryParameters.getFirst("$search").isEmpty())
			queryOptions.search = queryParameters.getFirst("$search");

		if (queryParameters.containsKey("$expand") && !queryParameters.getFirst("$expand").isEmpty())
			queryOptions.expand = queryParameters.getFirst("$expand");

		if (queryParameters.containsKey("$orderby") && !queryParameters.getFirst("$orderby").isEmpty())
			queryOptions.orderby = queryParameters.getFirst("$orderby");

		if (queryParameters.containsKey("$filter") && !queryParameters.getFirst("$filter").isEmpty())
			queryOptions.filter = queryParameters.getFirst("$filter");

		return queryOptions;
	}

}
