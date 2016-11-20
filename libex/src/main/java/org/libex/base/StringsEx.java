package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Defaults;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;

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
		String.format(format, (Object) null);

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
	public static <T> Function<T, String> formatter(
			final String format, Class<? extends T> type) {
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

	private static final Predicate<String> isNullOrEmpty = new Predicate<String>() {

		@Override
		public boolean apply(@Nullable String input) {
			return Strings.isNullOrEmpty(input);
		}
	};

	public static Predicate<String> isNullOrEmpty() {
		return isNullOrEmpty;
	}

	@Nonnull
	public static Function<String, String> replaceAll(final String regex, final String replacement) {
		checkNotNull(regex);
		checkNotNull(replacement);

		return new Function<String, String>() {

			@Override
			@Nonnull
			public String apply(@Nonnull String input) {
				return input.replaceAll(regex, replacement);
			}
		};
	}

	// Commented out because is ObjectsEx
	// public static Predicate<String> equals(@Nullable final String value) {
	// return new Predicate<String>() {
	//
	// @Override
	// public boolean apply(@Nullable String input) {
	// return input != null && input.endsWith(value);
	// }
	// };
	// }

	public static Predicate<String> equalsIgnoreCase(@Nullable final String value) {
		return new Predicate<String>() {

			@Override
			public boolean apply(@Nullable String input) {
				return (value == null)
						? input == null
						: value.equalsIgnoreCase(input);
			}
		};
	}

	public static Predicate<String> matches(@Nullable String regex) {
		final Pattern pattern = Pattern.compile(regex);
		return new Predicate<String>() {

			@Override
			public boolean apply(@Nullable String input) {
				return pattern.matcher(input).matches();
			}
		};
	}

	public static Predicate<String> endsWith(final String value) {
		checkNotNull(value);
		return new Predicate<String>() {

			@Override
			public boolean apply(@Nullable String input) {
				return input != null && input.endsWith(value);
			}
		};
	}

	public static Predicate<String> startsWith(final String value) {
		checkNotNull(value);
		return new Predicate<String>() {

			@Override
			public boolean apply(@Nullable String input) {
				return input != null && input.startsWith(value);
			}
		};
	}

	// Commented out because is ObjectsEx
	// public static Function<Object, String> toString(@Nullable final String
	// useForNull) {
	// return new Function<Object, String>() {
	//
	// @Override
	// @Nullable
	// public String apply(@Nullable Object input) {
	// return input == useForNull ? null : input.toString();
	// }
	// };
	// }
	//
	// private static final Function<Object, String> nullableToString =
	// toString(null);
	//
	// public static Function<Object, String> nullableToString() {
	// return nullableToString;
	// }

	private static final Function<String, String> toLowerCase = new Function<String, String>() {

		@Override
		@Nullable
		public String apply(@Nullable String input) {
			return (input == null) ? null : input.toLowerCase();
		}
	};

	@Nonnull
	public static Function<String, String> toLowerCase() {
		return toLowerCase;
	}

	private static final Function<String, String> toUpperCase = new Function<String, String>() {

		@Override
		@Nullable
		public String apply(@Nullable String input) {
			return (input == null) ? null : input.toUpperCase();
		}
	};

	@Nonnull
	public static Function<String, String> toUpperCase() {
		return toUpperCase;
	}

	@Nullable
	public static String trim(@Nullable String s) {
		return (s == null) ? null : s.trim();
	}

	private static final Function<String, String> toTrimmed = new Function<String, String>() {

		@Override
		@Nullable
		public String apply(@Nullable String input) {
			return trim(input);
		}
	};

	@Nonnull
	public static Function<String, String> toTrimmed() {
		return toTrimmed;
	}

	private StringsEx() {
	}
}
