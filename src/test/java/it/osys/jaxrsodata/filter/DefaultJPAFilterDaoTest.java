package it.osys.jaxrsodata.filter;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.osys.jaxrsodata.HSQLDBInitialize;
import it.osys.jaxrsodata.entity.TestEntity;
import it.osys.jaxrsodata.entity.enums.TestEnumEntity;
import it.osys.jaxrsodata.exceptions.NotImplementedException;
import it.osys.jaxrsodata.queryoptions.QueryOptions;

public class DefaultJPAFilterDaoTest extends HSQLDBInitialize {

	private List<TestEntity> getFilteredResults(String filter) throws NotImplementedException {

		QueryOptions queryOptions = new QueryOptions();
		this.setEntityManager(em);

		queryOptions.filter = filter;

		return this.getAll(new DefaultJPAFilterVisitor<TestEntity>(), queryOptions);
	}

	/*
	 * Test AND
	 */
	@Test
	public void getFieldANDLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType ge '2017-01-01T12:00:00' and localDateTimeType le '2017-01-02T12:00:00'";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void testInternalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "internalDate/lastUpdate ge '2010-01-01T12:00:00'";

		// Expected result.
		final int expSize = 4;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection1() throws NotImplementedException {
		// Filter query.
		final String filter = "subentities/stringType1 eq 'app1'";

		// Expected result.
		final int expSize = 2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection2() throws NotImplementedException {
		// Filter query.
		final String filter = "subentities/stringType1 eq 'app2'";

		// Expected result.
		final int expSize = 1;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
	}

	@Test
	public void testConditionOnSubEntityCollection3() throws NotImplementedException {
		// Filter query.
		final String filter = "subentities/stringType1 eq 'app7'";

		// Expected result.
		final int expSize = 0;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
	}

	@Test
	public void getFieldANDStrings() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType1 eq 'app1' and stringType4 eq 'token'";

		// Expected result.
		final int expSize = 1;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	/*
	 * Test OR
	 */
	@Test
	public void getFieldORLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType ge '2017-01-01T12:00:00' or localDateTimeType le '2017-01-02T12:00:00'";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void getFieldORStrings() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType3 eq 'string' or stringType3 eq 'string1'";

		// Expected result.
		final int expSize = 3;
		final Long exp1Id = 2L;
		final Long exp2Id = 3L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	/*
	 * Test NOTCONTAINS
	 */
	@Test
	public void getFieldNOTCONTAINSString() throws NotImplementedException {
		// Filter query.
		final String filter = "not contains(stringType4, '2')";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	/*
	 * Test NOTCONTAINS
	 */
	@Test
	public void getFieldNOTCONTAINSString1() throws NotImplementedException {
		// Filter query.
		final String filter = "not (contains(stringType4, '2'))";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	/*
	 * Test on Boolean field
	 */
	@Test
	public void testFilterOnBooleanField() throws NotImplementedException {
		// Filter query.
		final String filter = "boolfield eq true";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	@Test
	public void getFieldNOTCONTAINSInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "not contains(version, '1')";

		// Expected result.
		final int expSize = 3;
		final Long exp1Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	@Test
	public void getFieldNOTCONTAINSIntegerWithMultipleResults() throws NotImplementedException {
		// Filter query.
		final String filter = "not contains(version, '3')";

		// Expected result.
		final int expSize = 3;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	/*
	 * Test CONTAINS
	 */
	@Test
	public void getFieldCONTAINSString() throws NotImplementedException {
		// Filter query.
		final String filter = "contains(stringType1, 'app1')";

		// Expected result.
		final int expSize = 1;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	@Test
	public void getFieldCONTAINSInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "contains(version, '1')";

		// Expected result.
		final int expSize = 1;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	@Test
	public void getFieldCONTAINSIntegerWithMultipleResults() throws NotImplementedException {
		// Filter query.
		final String filter = "contains(localDateTimeType, '01')";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final Long exp2Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void getFieldCONTAINSLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "contains(localDateTimeType, '02')";

		// Expected result.
		final int expSize = 1;
		final Long exp1Id = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	@Test
	public void getSubFieldCONTAINSString() throws NotImplementedException {
		// Filter query.
		final String filter = "contains(address/city, 'citt')";

		// Expected result.
		final int expSize = 4;
		final Long exp1Id = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1Id, result.get(0).getId());
	}

	/*
	 * Test NULL and NOTNULL
	 */
	@Test
	public void getNullLocalDateTimeField() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType eq null";

		// Expected result.
		final Long expId = 3L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(null, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getNullStringField() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType3 eq null";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(null, result.get(0).getStringType3());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getNotNullStringField() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType3 ne null";

		// Expected result.
		final Long expId = 2L;
		final String stringType = "string";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(stringType, result.get(0).getStringType3());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test EQUALS
	 */
	@Test
	public void getFieldEQString() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType1 eq 'app1'";

		// Expected result.
		final Long expId = 1L;
		final String expDevType = "app1";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expDevType, result.get(0).getStringType1());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test EQUALS
	 */
	@Test
	public void getFieldWithParentesisString() throws NotImplementedException {
		// Filter query.
		final String filter = "(stringType1 ne null or stringType3 ne null) and stringType4 eq 'token'";

		// Expected result.
		final Long expId = 1L;
		final String expDevType = "app1";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expDevType, result.get(0).getStringType1());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQStringWithMultipleResults() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType2 eq 'distribution_name'";

		// Expected result.
		final int expSize = 2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
	}

	@Test
	public void getFieldEQInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version eq 1";

		// Expected result.
		final Long expId = 1L;
		final int version = 1;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(version, result.get(0).getVersion());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType eq '2017-01-01T13:00:00'";

		// Expected result.
		final Long expId = 1L;
		final LocalDateTime lastAttach = LocalDateTime.parse("2017-01-01T13:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQEnum() throws NotImplementedException {
		// Filter query.
		final String filter = "enumType eq 'Enum1'";

		// Expected result.
		final Long expId = 1L;
		final TestEnumEntity os = TestEnumEntity.Enum1;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(os, result.get(0).getEnumType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQELong() throws NotImplementedException {
		// Filter query.
		final String filter = "id eq 1";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQELocalTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localTimeType eq '13:00:00'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldEQELocalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateType eq '2017-01-01'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test NOTEQUALS
	 */
	@Test
	public void getFieldNEString() throws NotImplementedException {
		// Filter query.
		final String filter = "stringType1 ne 'app1'";

		// Expected result.
		final Long expId = 2L;
		final String expDevType = "app2";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(expDevType, result.get(0).getStringType1());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldNEInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version ne 1";

		// Expected result.
		final Long expId = 2L;
		final int version = 2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(version, result.get(0).getVersion());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldNELocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType ne '2017-01-02T12:00:00'";

		// Expected result.
		final Long expId = 1L;
		final LocalDateTime lastAttach = LocalDateTime.parse("2017-01-01T13:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldNEEnum() throws NotImplementedException {
		// Filter query.
		final String filter = "enumType ne 'Enum1'";

		// Expected result.
		final Long expId = 2L;
		final TestEnumEntity os = TestEnumEntity.Enum2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(os, result.get(0).getEnumType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test GREATER THEN
	 */
	@Test
	public void getFieldGTInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version gt 1";

		// Expected result.
		final Long expId = 2L;
		final int version = 2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(version, result.get(0).getVersion());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType gt '2017-01-01T14:00:00'";

		// Expected result.
		final Long expId = 2L;
		final LocalDateTime lastAttach = LocalDateTime.parse("2017-01-02T12:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localTimeType gt '15:00:00'";

		// Expected result.
		final Long expId = 4L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGTLocalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateType gt '2018-01-01'";

		// Expected result.
		final Long expId = 4L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGTLong() throws NotImplementedException {
		// Filter query.
		final String filter = "id gt 2";

		// Expected result.
		final Long expId = 3L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test GREATER THEN EQUALS
	 */
	@Test
	public void getFieldGEInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version ge 1";

		// Expected result.
		final int expSize = 4;
		final Long exp1Id = 1L;
		final int exp1version = 1;
		final Long exp2Id = 2L;
		final int exp2version = 2;
		final Long exp3Id = 3L;
		final int exp3version = 3;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1version, result.get(0).getVersion());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2version, result.get(1).getVersion());
		Assert.assertEquals(exp2Id, result.get(1).getId());
		Assert.assertEquals(exp3version, result.get(2).getVersion());
		Assert.assertEquals(exp3Id, result.get(2).getId());
	}

	@Test
	public void getFieldGELocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType ge '2017-01-01T13:00:00'";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final LocalDateTime exp1lastAttach = LocalDateTime.parse("2017-01-01T13:00:00");
		final Long exp2Id = 2L;
		final LocalDateTime exp2lastAttach = LocalDateTime.parse("2017-01-02T12:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2lastAttach, result.get(1).getLocalDateTimeType());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void getFieldGELocalTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localTimeType ge '14:00:00'";

		// Expected result.
		final Long expId = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGELocalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateType ge '2017-01-03'";

		// Expected result.
		final Long expId = 2L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldGELong() throws NotImplementedException {
		// Filter query.
		final String filter = "id ge 3";

		// Expected result.
		final Long expId = 3L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test LESS THEN
	 */
	@Test
	public void getFieldLTInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version lt 2";

		// Expected result.
		final Long expId = 1L;
		final int version = 1;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(version, result.get(0).getVersion());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType lt '2017-01-02T12:00:00'";

		// Expected result.
		final Long expId = 1L;
		final LocalDateTime lastAttach = LocalDateTime.parse("2017-01-01T13:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals(lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localTimeType lt '14:00:00'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLTLocalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateType lt '2017-01-03'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLTLong() throws NotImplementedException {
		// Filter query.
		final String filter = "id lt 3";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	/*
	 * Test LESS THEN EQUALS
	 */
	@Test
	public void getFieldLEInteger() throws NotImplementedException {
		// Filter query.
		final String filter = "version le 2";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final int exp1version = 1;
		final Long exp2Id = 2L;
		final int exp2version = 2;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1version, result.get(0).getVersion());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2version, result.get(1).getVersion());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void getFieldLELocalDateTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateTimeType le '2017-01-02T12:00:00'";

		// Expected result.
		final int expSize = 2;
		final Long exp1Id = 1L;
		final LocalDateTime exp1lastAttach = LocalDateTime.parse("2017-01-01T13:00:00");
		final Long exp2Id = 2L;
		final LocalDateTime exp2lastAttach = LocalDateTime.parse("2017-01-02T12:00:00");

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(expSize, result.size());
		Assert.assertEquals(exp1lastAttach, result.get(0).getLocalDateTimeType());
		Assert.assertEquals(exp1Id, result.get(0).getId());
		Assert.assertEquals(exp2lastAttach, result.get(1).getLocalDateTimeType());
		Assert.assertEquals(exp2Id, result.get(1).getId());
	}

	@Test
	public void getFieldLELocalTime() throws NotImplementedException {
		// Filter query.
		final String filter = "localTimeType le '13:00:00'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLELocalDate() throws NotImplementedException {
		// Filter query.
		final String filter = "localDateType le '2017-01-03'";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void getFieldLELong() throws NotImplementedException {
		// Filter query.
		final String filter = "id le 3";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(3, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void filterElementCollection1() throws NotImplementedException {
		// Filter query.
		final String filter = "ownerids has 3";

		// Expected result.
		final Long expId = 1L;

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals(expId, result.get(0).getId());
	}

	@Test
	public void testInString() throws NotImplementedException {
		final String filter = "address/city in ('citta1', 'citta2')";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
		Assert.assertEquals("distribution_name", result.get(1).getStringType2());
	}

	@Test
	public void testInAndHasString() throws NotImplementedException {
		final String filter = "address/city in ('citta1', 'citta2') and ownerids has 3";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(1, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
	}
	
	@Test
	public void testInOrHasString() throws NotImplementedException {
		final String filter = "address/city in ('citta1', 'citta2') or ownerids has 3";
		
		List<TestEntity> result = getFilteredResults(filter);
		
		Assert.assertEquals(3, result.size());
	}
	
	@Test
	public void testInOrHasInteger() throws NotImplementedException {
		final String filter = "id in (1, 2) or ownerids has 3";
		
		List<TestEntity> result = getFilteredResults(filter);
		
		Assert.assertEquals(3, result.size());
	}

	/*
	 * Test TOUPPER
	 */
	@Test
	public void getFieldToupperEQString() throws NotImplementedException {

		final String filter = "toupper(stringType2) eq 'DISTRIBUTION_NAME'";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("distribution_name", result.get(0).getStringType2());
		Assert.assertEquals("distribution_name", result.get(1).getStringType2());
	}

	/*
	 * Test TOLOWER
	 */
	@Test
	public void getFieldTolowerEQString() throws NotImplementedException {

		final String filter = "tolower(stringType2) eq 'distribution_name_up'";

		List<TestEntity> result = getFilteredResults(filter);

		Assert.assertEquals(2, result.size());
		Assert.assertEquals("DISTRIBUTION_NAME_UP", result.get(0).getStringType2());
	}

	/*
	 * Test NotImplementedType
	 */
	@SuppressWarnings("unused")
	@Test
	public void throwExceptionWhenNotImplementedType() {
		// Orderby query.
		final String filter = "notImplementedType eq 'strangeType'";

		try {
			List<TestEntity> result = getFilteredResults(filter);
		} catch (Exception e) {
			Assert.assertEquals(NotImplementedException.class, e.getClass());
		}
	}

	/*
	 * Test IllegalState
	 */
	@SuppressWarnings("unused")
	@Test
	public void throwExceptionWhenFilterNull() {
		// Orderby query.
		final String filter = "";

		try {
			List<TestEntity> result = getFilteredResults(filter);
		} catch (Exception e) {
			Assert.assertEquals(IllegalStateException.class, e.getClass());
		}
	}

}
