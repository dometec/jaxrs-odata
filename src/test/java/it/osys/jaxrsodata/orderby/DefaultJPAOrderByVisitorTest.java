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

	/*
	 * Test ASC
	 */
	@Test
	public void getFieldOrderByASC() throws NotImplementedException {
		// Orderby query.
		final String orderby = "version asc";

		// Expected result.
		final int expSize = 3;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;
		final Long exp3Id = 3L;

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
		Assert.assertEquals(exp3Id, result.get(2).getId());
	}

	/*
	 * Test DESC
	 */
	@Test
	public void getFieldOrderByDESC() throws NotImplementedException {
		// Orderby query.
		final String orderby = "version desc";

		// Expected result.
		final int expSize = 3;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;
		final Long exp3Id = 3L;

		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(2).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
		Assert.assertEquals(exp3Id, result.get(0).getId());
	}

	/*
	 * Test IllegalState
	 */
	@SuppressWarnings("unused")
	@Test
	public void throwExceptionWhenOrderByNull() {
		// Orderby query.
		final String orderby = "";

		try {
			List<TestEntity> result = getOrderedResults(orderby);
		} catch (Exception e) {
			Assert.assertEquals(IllegalStateException.class, e.getClass());
		}
	}

}
