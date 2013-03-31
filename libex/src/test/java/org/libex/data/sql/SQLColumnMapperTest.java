package org.libex.data.sql;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.hamcrest.collection.IsArrayContainingInOrder;
import org.junit.Before;
import org.junit.Test;
import org.libex.data.sql.ColumnTest.SubClass;
import org.libex.data.sql.ColumnTest.TestColumns;
import org.libex.test.TestBase;

import com.google.common.collect.Lists;

public class SQLColumnMapperTest extends TestBase {

	private final String tableName = "MY table";
	private SQLColumnMapper<SubClass> mapper = new SQLColumnMapper<SubClass>(tableName,
			newArrayList(TestColumns.values()));

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNulls() {
		nullPointerTester.testAllPublicConstructors(SQLColumnMapper.class);
		nullPointerTester.testAllPublicInstanceMethods(mapper);
	}

	@Test
	public void testGenerateInsertStatement_emptyTableName() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		new SQLColumnMapper<SubClass>("",
				newArrayList(TestColumns.values()));
	}

	@Test
	public void testGenerateInsertStatement_emptyColumn() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		new SQLColumnMapper<SubClass>("blah", Lists.<TestColumns> newArrayList());
	}

	@Test
	public void testGetInsertStatement() {
		// test
		String insertStatement = mapper.getInsertStatement();

		// verify
		String expected = String.format("insert into %s (%s,%s) values (?,?)",
				tableName, TestColumns.FIELD1.name(), TestColumns.FIELD2.name());
		assertThat(insertStatement, equalTo(expected));
	}

	@Test
	public void testGetInsertStatementValues() {
		// test
		Object[] result = mapper.getInsertStatementValues(ColumnTest.bean);

		// verify
		assertThat(result, IsArrayContainingInOrder.<Object> arrayContaining(
				ColumnTest.bean.field1,
				ColumnTest.bean.field2));
	}

	@Test
	public void testGenerateSelecteStatement_oneColumn() {
		// test
		String select = mapper.generateSelectStatement(TestColumns.FIELD2);

		// verify
		assertThat(select, equalTo("select FIELD2 from " + tableName));
	}

	@Test
	public void testGenerateSelecteStatement_multipleColumn() {
		// test
		String select = mapper.generateSelectStatement(TestColumns.FIELD2, TestColumns.FIELD1);

		// verify
		assertThat(select, equalTo("select FIELD2,FIELD1 from " + tableName));
	}

}
