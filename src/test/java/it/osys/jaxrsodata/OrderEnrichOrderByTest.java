package it.osys.jaxrsodata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.Random.class)
public class OrderEnrichOrderByTest {

	@Test
	public void testOrderByEnrichmentWithNull() {
		String orderby = null;
		Assertions.assertNull(enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithBlank() {
		String orderby = "";
		Assertions.assertEquals("", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithOneParameter() {
		String orderby = "name desc";
		Assertions.assertEquals("name desc, id asc", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithTwoParameter() {
		String orderby = "name desc, address asc";
		Assertions.assertEquals("name desc, address asc, id asc", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithTwoParameterAndId() {
		String orderby = "name desc, address asc,id desc";
		Assertions.assertEquals("name desc, address asc,id desc", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithTwoParameterAndIdInTheMiddle() {
		String orderby = "name desc, id asc, address asc";
		Assertions.assertEquals("name desc, id asc, address asc", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithThreeParameterAndSlashAndIdInTheMiddle() {
		String orderby = "name/id desc, address asc";
		Assertions.assertEquals("name/id desc, address asc", enrichOrderBy(orderby,"id"));
	}

	@Test
	public void testOrderByEnrichmentWithThreeParameterAndSlash() {
		String orderby = "name/address desc, plant asc";
		Assertions.assertEquals("name/address desc, plant asc, id asc", enrichOrderBy(orderby,"id"));
	}
	
	@Test
	public void testOrderByEnrichmentWithFieldSimilarToId() {
		String orderby = "name/address desc, plantid asc";
		Assertions.assertEquals("name/address desc, plantid asc, id asc", enrichOrderBy(orderby,"id"));
	}
	
	@Test
	public void testOrderByEnrichmentWithFieldSimilarToId2() {
		String orderby = "name/address desc, plant_id asc";
		Assertions.assertEquals("name/address desc, plant_id asc, id asc", enrichOrderBy(orderby,"id"));
	}
	
	@Test
	public void testOrderByEnrichmentWithFieldSimilarToId3() {
		String orderby = "name/address desc,plantid asc";
		Assertions.assertEquals("name/address desc,plantid asc, id asc", enrichOrderBy(orderby,"id"));
	}
	
	@Test
	public void testOrderByEnrichmentWithFieldSimilarToId4() {
		String orderby = "name/address desc,plant_id asc";
		Assertions.assertEquals("name/address desc,plant_id asc, id asc", enrichOrderBy(orderby,"id"));
	}
	
	@Test
	public void testOrderByEnrichmentWithFieldSimilarToIdAndId() {
		String orderby = "name/address desc,plant_id asc,id desc";
		Assertions.assertEquals("name/address desc,plant_id asc,id desc", enrichOrderBy(orderby,"id"));
	}

	public String enrichOrderBy(String orderby, String fieldId) {	
		if (orderby != null && !orderby.isBlank() ) {
			 Pattern pattern = Pattern.compile("(?<= |\\b)" + fieldId + "(?= |\\b)");
			 Matcher match = pattern.matcher(orderby);
			 
			 if(!match.find()) {
					return orderby += ", "+fieldId+" asc";				 
			 }
		}
		return orderby;
	}

}
