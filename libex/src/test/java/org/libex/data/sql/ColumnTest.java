package org.libex.data.sql;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ColumnTest {

	public static class InsertBean {
		public String field1;
		public String field2;
	}

	public static class SubClass extends InsertBean {
	}

	public static enum TestColumns implements Column<InsertBean> {

		FIELD1, FIELD2;

		@Override
		@Nonnull
		public String getColumnName() {
			return this.name();
		}

		@Override
		@Nullable
		public Object getColumnValue(InsertBean insertInstance) {
			if (this == FIELD1) {
				return insertInstance.field1;
			} else {
				return insertInstance.field2;
			}
		}
	}

	public static final SubClass bean = new SubClass();

	static {
		bean.field1 = "field1Value";
		bean.field2 = "field 2 value";
	}

	@DataPoints
	public static final TestColumns[] columns = TestColumns.values();

	@Theory
	public void testToColumnName(TestColumns column) {
		// test
		String value = Column.toColumnName.apply(column);

		// verify
		assertThat(value, equalTo(column.getColumnName()));
	}
}
