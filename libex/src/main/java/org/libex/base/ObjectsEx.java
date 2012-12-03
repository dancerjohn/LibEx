package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class ObjectsEx {
	

	@Nonnull
	public static final Function<Object, String> toString = new Function<Object, String>() {

		@Override
		@Nullable
		public String apply(@Nullable Object arg0) {
			return ObjectsEx.toString(arg0);
		}
	};

	@Nullable
	public static String toString(@Nullable Object object) {
		return (object == null) ? null : object.toString();
	}

	@Nonnull
	public static Function<Object, String> toStringOrDefault(@Nonnull final String defaultValue) {
		checkNotNull(defaultValue);

		return new Function<Object, String>() {

			@Override
			@Nonnull
			public String apply(@Nullable Object arg0) {
				return ObjectsEx.toString(arg0, defaultValue);
			}
		};
	}

	@Nonnull
	public static String toString(@Nullable Object object, @Nonnull String defaultValue) {
		checkNotNull(defaultValue);

		return (object == null) ? defaultValue : object.toString();
	}

	private ObjectsEx() {
	}
}
