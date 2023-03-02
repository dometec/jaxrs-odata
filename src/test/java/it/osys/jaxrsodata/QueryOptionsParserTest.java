package it.osys.jaxrsodata;

import java.util.Arrays;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class QueryOptionsParserTest {

	@Test
	void testFromUriInfo() {

		MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
		map.put("$top", Arrays.asList("1"));
		map.put("$skip", Arrays.asList("10"));
		map.put("$count", Arrays.asList("true"));
		map.put("$expand", Arrays.asList("e1"));
		map.put("$orderby", Arrays.asList("o1"));
		map.put("$filter", Arrays.asList("f1"));
		map.put("$search", Arrays.asList("s1"));
		UriInfo mock = new UriInfoMock(map);

		QueryOptions queryOptions = QueryOptionsParser.from(mock);
		Assert.assertEquals(queryOptions.top, 1);
		Assert.assertEquals(queryOptions.skip, 10);
		Assert.assertTrue(queryOptions.count);
		Assert.assertEquals(queryOptions.expand, "e1");
		Assert.assertEquals(queryOptions.orderby, "o1");
		Assert.assertEquals(queryOptions.search, "s1");
		Assert.assertNotNull(queryOptions.toString());

	}

}
