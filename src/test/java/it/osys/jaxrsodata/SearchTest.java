package it.osys.jaxrsodata;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import it.osys.jaxrsodata.entity.TestEntity;

public class SearchTest extends HSQLDBInitialize {

	@Test
	public void searchFiltersWhenFieldsConfigured() {
		super.setEntityManager(em);
		this.setSearchFields(List.of("stringType1"));

		QueryOptions queryOptions = new QueryOptions();
		queryOptions.search = "app1";

		List<TestEntity> result = this.get(queryOptions);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assertions.assertEquals(1, this.count(queryOptions));
	}

	@Test
	public void searchIsCaseInsensitive() {
		super.setEntityManager(em);
		this.setSearchFields(List.of("stringType1"));

		QueryOptions queryOptions = new QueryOptions();
		queryOptions.search = "APP1";

		List<TestEntity> result = this.get(queryOptions);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void searchIgnoredAndWarnsWhenNoFieldsConfigured() {
		super.setEntityManager(em);
		// no setSearchFields(...) call: search fields are left empty on purpose

		QueryOptions queryOptions = new QueryOptions();
		queryOptions.search = "app2";

		PrintStream originalErr = System.err;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		List<TestEntity> result;
		long count;
		try {
			System.setErr(new PrintStream(captured, true, StandardCharsets.UTF_8));
			result = this.get(queryOptions);
			count = this.count(queryOptions);
		} finally {
			System.setErr(originalErr);
		}

		// $search is ignored at the query level: all rows are returned
		Assertions.assertEquals(4, result.size());
		Assertions.assertEquals(4, count);

		// ...and a warning that points to setSearchFields(...) is logged
		String log = captured.toString(StandardCharsets.UTF_8);
		Assertions.assertTrue(log.contains("WARN"), "expected a WARN log, got: " + log);
		Assertions.assertTrue(log.contains("$search"), "warning should mention $search, got: " + log);
		Assertions.assertTrue(log.contains("setSearchFields"),
				"warning should point to setSearchFields(...), got: " + log);
	}

}
