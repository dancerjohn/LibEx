package org.libex.data.sql;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@ParametersAreNonnullByDefault
public class SQLColumnMapper<InsertType> {

	private final String tableName;
	private final ImmutableList<Column<? super InsertType>> insertColumns;
	private final String insertStatement;

	public SQLColumnMapper(String tableName, Iterable<? extends Column<? super InsertType>> insertColumns) {
		this.tableName = tableName;
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

	public String generateSelectStatement(Column<?> column1,
			Column<?>... columns) {
		List<Column<?>> list = Lists.<Column<?>> newArrayList(column1);
		list.addAll(newArrayList(columns));
		return generateSelectStatement(list);
	}

	public String generateSelectStatement(Iterable<? extends Column<?>> columns) {
		return SQLGenerator.generateSelectStatement(tableName, transform(columns, Column.toColumnName));
	}
}
