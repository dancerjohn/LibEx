package org.libex.enums;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.data.sql.ColumnTest;
import org.libex.data.sql.ColumnTest.TestColumns;
import org.libex.test.TestBase;

@RunWith(Theories.class)
public class EnumsExTest extends TestBase {

	@DataPoints
	public static final ColumnTest.TestColumns[] columns = ColumnTest.TestColumns.values();

	@Before
	public void setUp() throws Exception {
	}

	@Theory
	public void testToName(TestColumns column) {
		// test
		String name = EnumsEx.toName().apply(column);

		// verify
		assertThat(name, equalTo(column.name()));
	}

	@Test
	public void testNulls() {
		// expect
		expectedException.expect(NullPointerException.class);

		// test
		EnumsEx.toName().apply(null);
	}

}
