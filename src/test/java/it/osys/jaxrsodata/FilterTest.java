package it.osys.jaxrsodata;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.Suite;

import it.osys.jaxrsodata.entity.TestEntity;
import it.osys.jaxrsodata.entity.enums.TestEnumEntity;

@Suite
public class FilterTest extends HSQLDBInitialize {

	private List<TestEntity> getFilteredResults(String filter) {
		super.setEntityManager(em);
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.filter = filter;
		return this.get(queryOptions);
	}

	private long getFilteredCount(String filter) {
		super.setEntityManager(em);
		QueryOptions queryOptions = new QueryOptions();
		queryOptions.filter = filter;
		return this.count(queryOptions);
	}

	@Test
	public void noFilterTest() {
		String filter = null;
		List<TestEntity> result = getFilteredResults(filter);
		long count = getFilteredCount(filter);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(4, count);
	}

	@Test
	public void getFieldANDLocalDateTime() {
		String filter = "localDateTimeType ge '2017-01-01T12:00:00' and localDateTimeType le '2017-01-02T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		long count = getFilteredCount(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(2, count);
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void testInternalDate() {
		String filter = "internalDate/lastUpdate ge '2010-01-01T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(4, result.size());
	}

	@Test
	public void testFloatGE() {
		String filter = "length ge 10";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void testFloatLT() {
		String filter = "length lt 5";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection1Test() {
		String filter = "subentities/stringType1 eq 'app1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection2() {
		String filter = "subentities/stringType1 eq 'app2'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection3() {
		String filter = "subentities/stringType1 eq 'app7'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void getFieldANDStrings() {
		String filter = "stringType1 eq 'app1' and stringType4 eq 'token'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldORLocalDateTime() {
		String filter = "localDateTimeType ge '2017-01-01T12:00:00' or localDateTimeType le '2017-01-02T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldORStrings() {
		String filter = "stringType3 eq 'string' or stringType3 eq 'string1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(3), result.get(1).getId());
	}

	@Test
	public void getFieldNOTCONTAINSString() {
		String filter = "not contains(stringType4, '2')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldNOTCONTAINSString1() {
		String filter = "not (contains(stringType4, '2'))";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void testFilterOnBooleanField() {
		String filter = "boolfield eq true";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldNOTCONTAINSInteger() {
		String filter = "not contains(version, '1')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldNOTCONTAINSIntegerWithMultipleResults() {
		String filter = "not contains(version, '3')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldCONTAINSString() {
		String filter = "contains(stringType1, 'app1')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldCONTAINSInteger() {
		String filter = "contains(version, '1')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldCONTAINSIntegerWithMultipleResults() {
		String filter = "contains(localDateTimeType, '01')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldCONTAINSLocalDateTime() {
		String filter = "contains(localDateTimeType, '02')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getSubFieldCONTAINSString() {
		String filter = "contains(address/city, 'citt')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getNullLocalDateTimeField() {
		String filter = "localDateTimeType eq null";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertNull(result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(3), result.get(0).getId());
	}

	@Test
	public void getNullStringField() {
		String filter = "stringType3 eq null";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertNull(result.get(0).getStringType3());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getNotNullStringField() {
		String filter = "stringType3 ne null";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("string", result.get(0).getStringType3());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldEQString() {
		String filter = "stringType1 eq 'app1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("app1", result.get(0).getStringType1());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldWithParentesisString() {
		String filter = "(stringType1 ne null or stringType3 ne null) and stringType4 eq 'token'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("app1", result.get(0).getStringType1());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQStringWithMultipleResults() {
		String filter = "stringType2 eq 'distribution_name'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void getFieldEQInteger() {
		String filter = "version eq 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQLocalDateTime() {
		String filter = "localDateTimeType eq '2017-01-01T13:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-01T13:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQEnum() {
		String filter = "enumType eq 'Enum1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(TestEnumEntity.Enum1, result.get(0).getEnumType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQELong() {
		String filter = "id eq 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQELocalTime() {
		String filter = "localTimeType eq '13:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldEQELocalDate() {
		String filter = "localDateType eq '2017-01-01'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldNEString() {
		String filter = "stringType1 ne 'app1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("app2", result.get(0).getStringType1());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldNEInteger() {
		String filter = "version ne 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(2, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldNELocalDateTime() {
		String filter = "localDateTimeType ne '2017-01-02T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-01T13:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldNEEnum() {
		String filter = "enumType ne 'Enum1'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(TestEnumEntity.Enum2, result.get(0).getEnumType());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldGTInteger() {
		String filter = "version gt 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(2, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalDateTime() {
		String filter = "localDateTimeType gt '2017-01-01T14:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-02T12:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalTime() {
		String filter = "localTimeType gt '15:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(4), result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalDate() {
		String filter = "localDateType gt '2018-01-01'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(4), result.get(0).getId());
	}

	@Test
	public void getFieldGTLong() {
		String filter = "id gt 2";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(3), result.get(0).getId());
	}

	@Test
	public void getFieldGEInteger() {
		String filter = "version ge 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(4, result.size());
		Assert.assertEquals(1, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(2, result.get(1).getVersion());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
		Assert.assertEquals(3, result.get(2).getVersion());
		Assert.assertEquals(Long.valueOf(3), result.get(2).getId());
	}

	@Test
	public void getFieldGELocalDateTime() {
		String filter = "localDateTimeType ge '2017-01-01T13:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-01T13:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(LocalDateTime.parse("2017-01-02T12:00:00"), result.get(1).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldGELocalTime() {
		String filter = "localTimeType ge '14:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldGELocalDate() {
		String filter = "localDateType ge '2017-01-03'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(2), result.get(0).getId());
	}

	@Test
	public void getFieldGELong() {
		String filter = "id ge 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(3), result.get(0).getId());
	}

	@Test
	public void getFieldLTInteger() {
		String filter = "version lt 2";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalDateTime() {
		String filter = "localDateTimeType lt '2017-01-02T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-01T13:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalTime() {
		String filter = "localTimeType lt '14:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalDate() {
		String filter = "localDateType lt '2017-01-03'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLTLong() {
		String filter = "id lt 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLEInteger() {
		String filter = "version le 2";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(1, result.get(0).getVersion());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(2, result.get(1).getVersion());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldLELocalDateTime() {
		String filter = "localDateTimeType le '2017-01-02T12:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(LocalDateTime.parse("2017-01-01T13:00:00"), result.get(0).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
		Assert.assertEquals(LocalDateTime.parse("2017-01-02T12:00:00"), result.get(1).getLocalDateTimeType());
		Assert.assertEquals(Long.valueOf(2), result.get(1).getId());
	}

	@Test
	public void getFieldLELocalTime() {
		String filter = "localTimeType le '13:00:00'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLELocalDate() {
		String filter = "localDateType le '2017-01-03'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void getFieldLELong() {
		String filter = "id le 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void filterElementCollection1() {
		String filter = "ownerids has 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}

	@Test
	public void testInString() {
		String filter = "address/city in ('citta1', 'citta2')";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
		Assert.assertEquals("distribution_name", result.get(1).getStringType2());
	}

	@Test
	public void testInAndHasString() {
		String filter = "address/city in ('citta1', 'citta2') and ownerids has 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
	}

	@Test
	public void testLengthInCollection1a() {
		String filter = "length(ownerids) eq 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testLengthInCollection1b() {
		String filter = "length(ownerids) eq 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(3), result.get(0).getId());
	}

	@Test
	public void testLengthInCollection1c() {
		String filter = "length(ownerids) eq 2";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(1), result.get(0).getId());
	}
	
	@Test
	public void testLengthInCollection1d() {
		String filter = "length(ownerids) eq 1";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(Long.valueOf(3), result.get(0).getId());
	}
	
	@Test
	public void testInOrHasString() {
		String filter = "address/city in ('citta1', 'citta2') or ownerids has 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
	}

	@Test
	public void testInOrHasInteger() {
		String filter = "id in (1, 2) or ownerids has 3";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(3, result.size());
	}

	@Test
	public void getFieldToupperEQString() {
		String filter = "toupper(stringType2) eq 'DISTRIBUTION_NAME'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
		Assert.assertEquals("distribution_name", result.get(1).getStringType2());
	}

	@Test
	public void getFieldTolowerEQString() {
		String filter = "tolower(stringType2) eq 'distribution_name_up'";
		List<TestEntity> result = getFilteredResults(filter);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals("DISTRIBUTION_NAME_UP", result.get(0).getStringType2());
	}

	/*
	 * @Test public void getValueFieldOfMap() { String filter =
	 * "name/value eq = 'Aplicación uno traductor'"; List<TestEntity> result =
	 * getFilteredResults(filter); Assert.assertEquals(1, result.size());
	 * Assert.assertEquals("distribution_name", result.get(0).getStringType2());
	 * }
	 */

	/*
	 * @Test public void getKeyFieldOfMap() { String filter =
	 * "name/key eq 'ES'"; List<TestEntity> result = getFilteredResults(filter);
	 * Assert.assertEquals(2, result.size());
	 * Assert.assertEquals("distribution_name", result.get(0).getStringType2());
	 * Assert.assertEquals("DISTRIBUTION_NAME_UP",
	 * result.get(1).getStringType2()); }
	 */

	/*
	 * @Test public void getKeyAndValueFieldOfMap() { String filter =
	 * "name/key eq 'ES' and name/value eq = 'Aplicación uno traductor'";
	 * List<TestEntity> result = getFilteredResults(filter);
	 * Assert.assertEquals(1, result.size());
	 * Assert.assertEquals("distribution_name", result.get(0).getStringType2());
	 * }
	 */

	@Test
	public void throwExceptionWhenFilterEmpty() {
		String filter = "";

		try {
			getFilteredResults(filter);
		} catch (Exception e) {
			Assert.assertEquals(IllegalStateException.class, e.getClass());
		}
	}

}
