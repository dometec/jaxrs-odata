package it.osys.jaxrsodata;

import java.util.List;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import it.osys.jaxrsodata.entity.TestEntity;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.Random.class)
public class OrderTest extends HSQLDBInitialize {

	private List<TestEntity> getOrderedResults(String order) {
		super.setEntityManager(em);

		/*
		 * It's important to clear the cache since the entity can be cached and
		 * the value of Formula field changed at runtime need to be refreshed
		 * from database (only if you access to formula field)
		 */
		em.unwrap(Session.class).clear();

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

/*	@Test
	public void getFieldOrderByDESC() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "version desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1l, result.get(3).getId().longValue());
		Assert.assertEquals(2l, result.get(2).getId().longValue());
		Assert.assertEquals(3l, result.get(1).getId().longValue());
		Assert.assertEquals(4l, result.get(0).getId().longValue());
	}*/

	@Test
	public void getMultipleOrderBy() {
		String orderby = "stringType1 desc, stringType4 asc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(4l, result.get(0).getId().longValue());
		Assert.assertEquals(2l, result.get(1).getId().longValue());
		Assert.assertEquals(3l, result.get(2).getId().longValue());
		Assert.assertEquals(1l, result.get(3).getId().longValue());
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
	public void getOrderByDESCInnerField() {
		String orderby = "address/city desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals("citta3", result.get(0).getAddress().getCity());
		Assert.assertEquals("citta25", result.get(1).getAddress().getCity());
		Assert.assertEquals("citta2", result.get(2).getAddress().getCity());
		Assert.assertEquals("citta1", result.get(3).getAddress().getCity());
	}

/*	@Test
	public void getOrderByASCMapField1() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "transName asc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());

		Assert.assertNull(result.get(0).getEnName());
		Assert.assertNull(result.get(1).getEnName());
		Assert.assertEquals("Application two integration", result.get(2).getEnName());
		Assert.assertEquals("Application one translator", result.get(3).getEnName());

		Assert.assertNull(result.get(0).getTransName());
		Assert.assertNull(result.get(1).getTransName());
		Assert.assertEquals("Applicazione due integrazione", result.get(2).getTransName());
		Assert.assertEquals("Applicazione uno traduttore", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByDECMapField1() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "transName desc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());

		Assert.assertNull(result.get(0).getEnName());
		Assert.assertNull(result.get(1).getEnName());
		Assert.assertEquals("Application one translator", result.get(2).getEnName());
		Assert.assertEquals("Application two integration", result.get(3).getEnName());

		Assert.assertNull(result.get(0).getTransName());
		Assert.assertNull(result.get(1).getTransName());
		Assert.assertEquals("Applicazione uno traduttore", result.get(2).getTransName());
		Assert.assertEquals("Applicazione due integrazione", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByASCMapField2() {
		CurrentLanguageInterceptor.setLang("ES");
		String orderby = "transName asc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());

		Assert.assertEquals("Application two integration", result.get(0).getEnName());
		Assert.assertNull(result.get(1).getEnName());
		Assert.assertNull(result.get(2).getEnName());
		Assert.assertEquals("Application one translator", result.get(3).getEnName());

		Assert.assertNull(result.get(0).getTransName());
		Assert.assertNull(result.get(1).getTransName());
		Assert.assertEquals("Aplicación dos integración", result.get(2).getTransName());
		Assert.assertEquals("Aplicación uno traductor", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByDESCMapField2() {
		CurrentLanguageInterceptor.setLang("ES");
		String orderby = "transName desc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assert.assertEquals(4, result.size());

		Assert.assertEquals("Application two integration", result.get(0).getEnName());
		Assert.assertNull(result.get(1).getEnName());
		Assert.assertEquals("Application one translator", result.get(2).getEnName());
		Assert.assertNull(result.get(3).getEnName());

		Assert.assertNull(result.get(0).getTransName());
		Assert.assertNull(result.get(1).getTransName());
		Assert.assertEquals("Aplicación uno traductor", result.get(2).getTransName());
		Assert.assertEquals("Aplicación dos integración", result.get(3).getTransName());

	}*/

}
