package it.osys.jaxrsodata.orderby;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.osys.jaxrsodata.HSQLDBInitialize;
import it.osys.jaxrsodata.entity.TestEntity;
import it.osys.jaxrsodata.exceptions.NotImplementedException;
import it.osys.jaxrsodata.filter.DefaultJPAFilterVisitor;
import it.osys.jaxrsodata.queryoptions.QueryOptions;

public class DefaultJPAOrderByVisitorTest extends HSQLDBInitialize {

	private List<TestEntity> getOrderedResults(String order) throws NotImplementedException {
		QueryOptions queryOptions = new QueryOptions();
		this.setEntityManager(em);
		queryOptions.orderby = order;
		return this.getAll(new DefaultJPAFilterVisitor<TestEntity>(), queryOptions);
	}

	@Test
	public void getFieldOrderByASC() throws NotImplementedException {

		String orderby = "version asc";

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1l, result.get(0).getId().longValue());
		Assert.assertEquals(2l, result.get(1).getId().longValue());
		Assert.assertEquals(3l, result.get(2).getId().longValue());
		Assert.assertEquals(4l, result.get(3).getId().longValue());
	}

	@Test
	public void getFieldOrderByDESC() throws NotImplementedException {

		String orderby = "version desc";

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1l, result.get(3).getId().longValue());
		Assert.assertEquals(2l, result.get(2).getId().longValue());
		Assert.assertEquals(3l, result.get(1).getId().longValue());
		Assert.assertEquals(4l, result.get(0).getId().longValue());
	}

	@Test
	public void getOrderByASCInnerField() throws NotImplementedException {

		String orderby = "address/city asc";

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());
		Assert.assertEquals("citta1", result.get(0).getAddress().getCity());
		Assert.assertEquals("citta2", result.get(1).getAddress().getCity());
		Assert.assertEquals("citta25", result.get(2).getAddress().getCity());
		Assert.assertEquals("citta3", result.get(3).getAddress().getCity());
	}

	@Test
	public void getOrderByDescInnerField() throws NotImplementedException {

		String orderby = "address/city desc";

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());
		Assert.assertEquals("citta3", result.get(0).getAddress().getCity());
		Assert.assertEquals("citta25", result.get(1).getAddress().getCity());
		Assert.assertEquals("citta2", result.get(2).getAddress().getCity());
		Assert.assertEquals("citta1", result.get(3).getAddress().getCity());
	}

}
