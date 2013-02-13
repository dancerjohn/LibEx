package org.libex.data.sql;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
public class SQLColumnMapper<InsertType> {

	private final ImmutableList<Column<? super InsertType>> insertColumns;
	private final String insertStatement;

	public SQLColumnMapper(String tableName, Iterable<? extends Column<? super InsertType>> insertColumns) {
		this.insertColumns = ImmutableList.copyOf(insertColumns);
		this.insertStatement = SQLGenerator.generateInsertStatement(tableName,
				transform(insertColumns, Column.toColumnName));
	}

	public String getInsertStatement() {
		return insertStatement;
	}

	public Object[] getInsertStatementValues(InsertType insertInstance) {
		checkNotNull(insertInstance);

		List<Object> values = newArrayList();
		for (Column<? super InsertType> column : insertColumns) {
			values.add(column.getColumnValue(insertInstance));
		}

		return values.toArray();
	}

}
