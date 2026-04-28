package it.osys.jaxrsodata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;

/**
 * The Class QueryOptionsParser.
 * 
 * @author Domenico Briganti
 */
public class QueryOptionsParser {

	private static final int DEFAULT_TOP = 500;
	private static final int DEFAULT_SKIP = 0;

	private static int parsePositiveInt(String raw, String paramName, int defaultValue) {
		if (raw == null)
			return defaultValue;
		String v = raw.trim();
		if (v.isEmpty())
			return defaultValue;
		try {
			int parsed = Integer.parseInt(v);
			return parsed > 0 ? parsed : defaultValue;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid integer value for query param " + paramName + ": " + raw, e);
		}
	}

	private static int parseNonNegativeInt(String raw, String paramName, int defaultValue) {
		if (raw == null)
			return defaultValue;
		String v = raw.trim();
		if (v.isEmpty())
			return defaultValue;
		try {
			int parsed = Integer.parseInt(v);
			return parsed >= 0 ? parsed : defaultValue;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid integer value for query param " + paramName + ": " + raw, e);
		}
	}

	/**
	 * From.
	 *
	 * @param info the info
	 * @return the query options
	 */
	public static QueryOptions from(UriInfo info) {
		if (info == null)
			throw new IllegalArgumentException("UriInfo must not be null");
		return from(info.getQueryParameters());
	}

	/**
	 * From.
	 *
	 * @param queryParameters the query parameters
	 * @return the query options
	 */
	public static QueryOptions from(MultivaluedMap<String, String> queryParameters) {

		if (queryParameters == null)
			throw new IllegalArgumentException("queryParameters must not be null");

		QueryOptions queryOptions = new QueryOptions();
		if (queryParameters.containsKey("$top"))
			queryOptions.top = parsePositiveInt(queryParameters.getFirst("$top"), "$top", DEFAULT_TOP);

		if (queryParameters.containsKey("$skip"))
			queryOptions.skip = parseNonNegativeInt(queryParameters.getFirst("$skip"), "$skip", DEFAULT_SKIP);

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
		if (info == null)
			throw new IllegalArgumentException("UriInfo must not be null");
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

		QueryOptions queryOptions = from(queryParameters);

		if (fieldId != null && !fieldId.trim().isEmpty() && queryOptions.orderby != null && !queryOptions.orderby.trim().equals("")) {
			String quotedFieldId = Pattern.quote(fieldId.trim());
			Pattern pattern = Pattern.compile("(?<= |\\b)" + quotedFieldId + "(?= |\\b)");
			Matcher match = pattern.matcher(queryOptions.orderby);

			if (!match.find()) {
				queryOptions.orderby += ", " + fieldId + " asc";
			}
		}

		return queryOptions;
	}

}
