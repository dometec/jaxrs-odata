package it.osys.jaxrsodata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 * The Class QueryOptionsParser.
 * 
 * @author Domenico Briganti
 */
public class QueryOptionsParser {

	/**
	 * From.
	 *
	 * @param info the info
	 * @return the query options
	 */
	public static QueryOptions from(UriInfo info) {
		return from(info.getQueryParameters());
	}

	/**
	 * From.
	 *
	 * @param queryParameters the query parameters
	 * @return the query options
	 */
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

	/**
	 * From.
	 *
	 * @param info the info
	 * @param fieldId the identifier
	 * @return the query options
	 */
	public static QueryOptions from(UriInfo info, String fieldId) {
		return from(info.getQueryParameters(), fieldId);
	}

	/**
	 * From. Using this method involves adding a sort on the identifier field to solve Odata problems for sorts on non-key fields
	 * Sorting on a non-key field results in errors in displaying the returned records
	 *
	 * @param queryParameters the query parameters
	 * @param fieldId the identifier
	 * @return the query options
	 */
	public static QueryOptions from(MultivaluedMap<String, String> queryParameters, String fieldId) {

		QueryOptions queryOptions = new QueryOptions();

		queryOptions = from(queryParameters);

		if (queryOptions.orderby != null && !queryOptions.orderby.trim().equals("")) {
			Pattern pattern = Pattern.compile("(?<= |\\b)" + fieldId + "(?= |\\b)");
			Matcher match = pattern.matcher(queryOptions.orderby);

			if (!match.find()) {
				queryOptions.orderby += ", " + fieldId + " asc";
			}
		}

		return queryOptions;
	}

}
