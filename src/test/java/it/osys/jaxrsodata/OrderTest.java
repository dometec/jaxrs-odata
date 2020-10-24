package it.osys.jaxrsodata;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import it.osys.jaxrsodata.entity.TestEntity;

@RunWith(JUnitPlatform.class)
public class OrderTest extends HSQLDBInitialize {

	private List<TestEntity> getOrderedResults(String order) {
		super.setEntityManager(em);
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.orderby = order;
		return this.get(queryOptions);
	}

	@Test
	public void getFieldOrderByASC() {
		String orderby = "version asc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1l, result.get(0).getId().longValue());
		Assert.assertEquals(2l, result.get(1).getId().longValue());
		Assert.assertEquals(3l, result.get(2).getId().longValue());
		Assert.assertEquals(4l, result.get(3).getId().longValue());
	}

	@Test
	public void getFieldOrderByDESC() {
		String orderby = "version desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1l, result.get(3).getId().longValue());
		Assert.assertEquals(2l, result.get(2).getId().longValue());
		Assert.assertEquals(3l, result.get(1).getId().longValue());
		Assert.assertEquals(4l, result.get(0).getId().longValue());
	}

	@Test
	public void getOrderByASCInnerField() {
		String orderby = "address/city asc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals("citta1", result.get(0).getAddress().getCity());
		Assert.assertEquals("citta2", result.get(1).getAddress().getCity());
		Assert.assertEquals("citta25", result.get(2).getAddress().getCity());
		Assert.assertEquals("citta3", result.get(3).getAddress().getCity());
	}

	@Test
	public void getOrderByDescInnerField() {
		String orderby = "address/city desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals("citta3", result.get(0).getAddress().getCity());
		Assert.assertEquals("citta25", result.get(1).getAddress().getCity());
		Assert.assertEquals("citta2", result.get(2).getAddress().getCity());
		Assert.assertEquals("citta1", result.get(3).getAddress().getCity());
	}

}
