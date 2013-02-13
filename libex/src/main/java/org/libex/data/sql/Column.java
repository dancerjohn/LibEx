package org.libex.data.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
public interface Column<T> {

	public static final Function<Column<?>, String> toColumnName = new Function<Column<?>, String>() {

		@Override
		@Nonnull
		public String apply(@Nonnull Column<?> arg0) {
			return arg0.getColumnName();
		}
	};

	@Nonnull
	String getColumnName();

	@Nullable
	Object getColumnValue(T insertInstance);
}
