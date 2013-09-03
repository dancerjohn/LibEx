package org.libex.data.sql;

import static com.google.common.base.Functions.*;
import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;
import org.libex.test.theories.suppliers.TestOn;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@RunWith(Theories.class)
public class SQLGeneratorTest extends TestBase {

	private Iterable<String> columnNames = newArrayList("column1", "column2");

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNulls() {
		nullPointerTester.setDefault(String.class, "table");

		nullPointerTester.testAllPublicStaticMethods(SQLGenerator.class);
	}

	@Test
	public void testGenerateInsertStatement_emptyTableName() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		SQLGenerator.generateInsertStatement("", columnNames);
	}

	@Test
	public void testGenerateInsertStatement_emptyColumn() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		SQLGenerator.generateInsertStatement("blah", Lists.<String> newArrayList());
	}

	@Theory
	public void testGenerateInsertStatement_variousTableNames(@TestOn(strings = { "1", "sdfsdf_dfd" }) String tableName) {
		// test
		String statement = SQLGenerator.generateInsertStatement(tableName, columnNames);

		// verify
		Iterator<String> it = columnNames.iterator();
		String expected = String.format("insert into %s (%s,%s) values (?,?)",
				tableName, it.next(), it.next());
		assertThat(statement, equalTo(expected));
	}

	@DataPoint
	public static List<String> oneValue = newArrayList("first");

	@DataPoint
	public static List<String> threeValue = newArrayList("first", "second", "third");

	@Theory
	public void testGenerateInsertStatement_variousColumnNames(List<String> columnNames) {
		// test
		String statement = SQLGenerator.generateInsertStatement("tableName", columnNames);

		// verify
		Joiner joiner = Joiner.on(",");
		String columns = joiner.join(columnNames);
		String qs = joiner.join(transform(columnNames, constant("?")));

		String expected = String.format("insert into tableName (%s) values (%s)",
				columns, qs);
		assertThat(statement, equalTo(expected));
	}

	@Test
	public void testGenerateSelectStatement_emptyTableName() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		SQLGenerator.generateSelectStatement("", columnNames);
	}

	@Test
	public void testGenerateSelectStatement_emptyColumn() {
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// test
		SQLGenerator.generateSelectStatement("blah", Lists.<String> newArrayList());
	}

	@Theory
	public void testGenerateSelectStatement_variousTableNames(@TestOn(strings = { "1", "sdfsdf_dfd" }) String tableName) {
		// test
		String statement = SQLGenerator.generateSelectStatement(tableName, columnNames);

		// verify
		Iterator<String> it = columnNames.iterator();
		String expected = String.format("select %s,%s from %s",
				it.next(), it.next(), tableName);
		assertThat(statement, equalTo(expected));
	}

	@Theory
	public void testGenerateSelectStatement_variousColumnNames(List<String> columnNames) {
		// test
		String statement = SQLGenerator.generateSelectStatement("tableName", columnNames);

		// verify
		Joiner joiner = Joiner.on(",");
		String columns = joiner.join(columnNames);

		String expected = String.format("select %s from tableName",
				columns);
		assertThat(statement, equalTo(expected));
	}

}
