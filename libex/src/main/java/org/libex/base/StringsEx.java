package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Defaults;
import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class StringsEx {

	/**
	 * Creates a Function that formats the object passed to the Function using
	 * the passed format. This method uses {@code String.format(format, input)}
	 * 
	 * @param format
	 *            the format to use to convert the instance passed to the
	 *            function to a string
	 * @return a Function that formats the object passed to the Function using
	 *         the passed format
	 */
	@Nonnull
	public static Function<Object, String> formatter(final String format) {
		checkNotNull(format);

		return new Function<Object, String>() {

			@Override
			@Nonnull
			public String apply(@Nullable Object arg0) {
				return String.format(format, arg0);
			}
		};
	}

	/**
	 * Creates a type-specific Function that formats the object passed to the
	 * Function using the passed format. This method uses
	 * {@code String.format(format, input)}. This version should generally be
	 * used if the format expects a non-Object instance such as an Integer.
	 * 
	 * @param format
	 *            the format to use to convert the instance passed to the
	 *            function to a string
	 * @param type
	 *            used only at compile time to produce a type-specific generic
	 *            Function
	 * @return a Function that formats the object passed to the Function using
	 *         the passed format
	 */
	@Nonnull
	public static <T> Function<? extends T, String> formatter(
			final String format, Class<T> type) {
		checkNotNull(format);
		checkNotNull(type);
		String.format(format, Defaults.defaultValue(type));

		return new Function<T, String>() {

			@Override
			@Nonnull
			public String apply(@Nullable T arg0) {
				return String.format(format, arg0);
			}
		};
	}

	private StringsEx() {
	}
}
