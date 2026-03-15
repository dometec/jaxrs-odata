package it.osys.jaxrsodata;

import java.util.List;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import it.osys.jaxrsodata.entity.TestEntity;

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
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals(1l, result.get(0).getId().longValue());
		Assertions.assertEquals(2l, result.get(1).getId().longValue());
		Assertions.assertEquals(3l, result.get(2).getId().longValue());
		Assertions.assertEquals(4l, result.get(3).getId().longValue());
	}

/*	@Test
	public void getFieldOrderByDESC() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "version desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals(1l, result.get(3).getId().longValue());
		Assertions.assertEquals(2l, result.get(2).getId().longValue());
		Assertions.assertEquals(3l, result.get(1).getId().longValue());
		Assertions.assertEquals(4l, result.get(0).getId().longValue());
	}*/

	@Test
	public void getMultipleOrderBy() {
		String orderby = "stringType1 desc, stringType4 asc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals(4l, result.get(0).getId().longValue());
		Assertions.assertEquals(2l, result.get(1).getId().longValue());
		Assertions.assertEquals(3l, result.get(2).getId().longValue());
		Assertions.assertEquals(1l, result.get(3).getId().longValue());
	}

	@Test
	public void getOrderByASCInnerField() {
		String orderby = "address/city asc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals("citta1", result.get(0).getAddress().getCity());
		Assertions.assertEquals("citta2", result.get(1).getAddress().getCity());
		Assertions.assertEquals("citta25", result.get(2).getAddress().getCity());
		Assertions.assertEquals("citta3", result.get(3).getAddress().getCity());
	}

	@Test
	public void getOrderByDESCInnerField() {
		String orderby = "address/city desc";
		List<TestEntity> result = getOrderedResults(orderby);
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals("citta3", result.get(0).getAddress().getCity());
		Assertions.assertEquals("citta25", result.get(1).getAddress().getCity());
		Assertions.assertEquals("citta2", result.get(2).getAddress().getCity());
		Assertions.assertEquals("citta1", result.get(3).getAddress().getCity());
	}

/*	@Test
	public void getOrderByASCMapField1() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "transName asc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assertions.assertEquals(4, result.size());

		Assertions.assertNull(result.get(0).getEnName());
		Assertions.assertNull(result.get(1).getEnName());
		Assertions.assertEquals("Application two integration", result.get(2).getEnName());
		Assertions.assertEquals("Application one translator", result.get(3).getEnName());

		Assertions.assertNull(result.get(0).getTransName());
		Assertions.assertNull(result.get(1).getTransName());
		Assertions.assertEquals("Applicazione due integrazione", result.get(2).getTransName());
		Assertions.assertEquals("Applicazione uno traduttore", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByDECMapField1() {
		CurrentLanguageInterceptor.setLang("IT");
		String orderby = "transName desc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assertions.assertEquals(4, result.size());

		Assertions.assertNull(result.get(0).getEnName());
		Assertions.assertNull(result.get(1).getEnName());
		Assertions.assertEquals("Application one translator", result.get(2).getEnName());
		Assertions.assertEquals("Application two integration", result.get(3).getEnName());

		Assertions.assertNull(result.get(0).getTransName());
		Assertions.assertNull(result.get(1).getTransName());
		Assertions.assertEquals("Applicazione uno traduttore", result.get(2).getTransName());
		Assertions.assertEquals("Applicazione due integrazione", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByASCMapField2() {
		CurrentLanguageInterceptor.setLang("ES");
		String orderby = "transName asc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assertions.assertEquals(4, result.size());

		Assertions.assertEquals("Application two integration", result.get(0).getEnName());
		Assertions.assertNull(result.get(1).getEnName());
		Assertions.assertNull(result.get(2).getEnName());
		Assertions.assertEquals("Application one translator", result.get(3).getEnName());

		Assertions.assertNull(result.get(0).getTransName());
		Assertions.assertNull(result.get(1).getTransName());
		Assertions.assertEquals("Aplicación dos integración", result.get(2).getTransName());
		Assertions.assertEquals("Aplicación uno traductor", result.get(3).getTransName());
	}*/

/*	@Test
	public void getOrderByDESCMapField2() {
		CurrentLanguageInterceptor.setLang("ES");
		String orderby = "transName desc";
		List<TestEntity> result = getOrderedResults(orderby);

		Assertions.assertEquals(4, result.size());

		Assertions.assertEquals("Application two integration", result.get(0).getEnName());
		Assertions.assertNull(result.get(1).getEnName());
		Assertions.assertEquals("Application one translator", result.get(2).getEnName());
		Assertions.assertNull(result.get(3).getEnName());

		Assertions.assertNull(result.get(0).getTransName());
		Assertions.assertNull(result.get(1).getTransName());
		Assertions.assertEquals("Aplicación uno traductor", result.get(2).getTransName());
		Assertions.assertEquals("Aplicación dos integración", result.get(3).getTransName());

	}*/

}
