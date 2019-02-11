package it.osys.jaxrsodata.queryoptions;

import org.junit.Assert;
import org.junit.Test;

import it.osys.jaxrsodata.queryoptions.QueryOptions;

public class QueryOptionsTest {
	
	/*
	 * Test QueryOptions toString method
	 * 
	 */
	@Test
	public void queryOptionsToStringtest() {

		QueryOptions qo = new QueryOptions();
		qo.count = true;
		qo.filter = "filter";
		qo.orderby = "order";
		qo.search = "search";
		qo.expand = "expand";
		qo.skip = 10;
		qo.top = 10;

		String expectedToString = "QueryOptions [top=" + 10 + ", skip=" + 10 + ", count=" + true
				+ ", expand=expand, orderby=order, search=search, filter=filter]";

		Assert.assertEquals(expectedToString, qo.toString());
	}
}
