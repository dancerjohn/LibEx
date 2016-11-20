package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

        return new Function<Object, String>() {

            @Override
            @Nonnull
            public String apply(@Nullable final Object arg0) {
                return String.format(format, arg0);
            }
        };
    }

    /**
     * Creates a type-specific Function that formats the object passed to the
     * Function using the passed format. This method uses {@code String.format(format, input)}. This version should
     * generally be
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
     * 
     * @param <T>
     *            class that the Function should accept
     */
    @Nonnull
    public static <T> Function<? extends T, String> formatter(
            final String format, final Class<T> type) {
        checkNotNull(format);
        checkNotNull(type);
        String.format(format, Defaults.defaultValue(type));

        return new Function<T, String>() {

            @Override
            @Nonnull
            public String apply(@Nullable final T arg0) {
                return String.format(format, arg0);
            }
        };
    }

    private static final Function<String, String> toLowerCase = new Function<String, String>() {

        @Override
        @Nullable
        public String apply(@Nullable final String input) {
            return toLowerCase(input);
        }
    };

    public static String toLowerCase(
            @Nullable final String input)
    {
        return (input == null) ? null : input.toLowerCase();
    }

    /**
     * @return a null-safe Function that converts a string to its lower case value
     */
    @Nonnull
    public static Function<String, String> toLowerCase() {
        return toLowerCase;
    }

    private static final Function<String, String> toUpperCase = new Function<String, String>() {

        @Override
        @Nullable
        public String apply(@Nullable final String input) {
            return toUpperCase(input);
        }
    };

    public static String toUpperCase(
            @Nullable final String input)
    {
        return (input == null) ? null : input.toUpperCase();
    }

    /**
     * @return a null-safe Function that converts a string to its upper case value
     */
    @Nonnull
    public static Function<String, String> toUpperCase() {
        return toUpperCase;
    }

    /**
     * Creates a {@link Predicate} that returns {@code true} when evaluated
     * against a {@link String} that is not {@code null} and starts with the
     * supplied {@code prefix}
     *
     * @param prefix
     *            the desired beginning of evaluated {@link String}s
     * @return a {@link Predicate} that returns {@code true} when evaluated
     *         against a {@link String} that is not {@code null} and starts with
     *         the supplied {@code prefix}
     */
    public static Predicate<String> startsWith(final String prefix) {
        checkNotNull(prefix);
        return new Predicate<String>() {

            @Override
            public boolean apply(@Nullable final String input) {
                return input != null && input.startsWith(prefix);
            }
        };
    }

    /**
     * Creates a {@link Predicate} that returns {@code true} when evaluated
     * against a {@link String} that is not {@code null} and ends with the
     * supplied {@code suffix}
     *
     * @param suffix
     *            the desired ending of evaluated {@link String}s
     * @return a {@link Predicate} that returns {@code true} when evaluated
     *         against a {@link String} that is not {@code null} and ends with
     *         the supplied {@code suffix}
     */
    public static Predicate<String> endsWith(final String suffix) {
        checkNotNull(suffix);
        return new Predicate<String>() {

            @Override
            public boolean apply(@Nullable final String input) {
                return input != null && input.endsWith(suffix);
            }
        };
    }

    /**
     * Creates a {@link Predicate} that returns {@code true} when evaluated
     * against a {@link String} that is not {@code null} and contains the
     * supplied {@code substring}
     *
     * @param substring
     *            the desired sub-string of evaluated {@link String}s
     * @return a {@link Predicate} that returns {@code true} when evaluated
     *         against a {@link String} that is not {@code null} and contains
     *         the supplied {@code substring}
     */
    public static Predicate<String> contains(final String substring) {
        checkNotNull(substring);
        return new Predicate<String>() {

            @Override
            public boolean apply(@Nullable final String input) {
                return input != null && input.contains(substring);
            }
        };
    }

    public static Function<String, String> replaceAll(final String regex, final String replacement) {
        checkNotNull(regex);
        checkNotNull(replacement);
        final Pattern pattern = Pattern.compile(regex);

        return new Function<String, String>() {

            @Override
            @Nullable
            public String apply(@Nullable final String input) {
                if (input == null) {
                    return null;
                } else {
                    String result = pattern.matcher(input).replaceAll(replacement);
                    return result;
                }
            }
        };
    }

    private static final Predicate<String> isNullOrEmpty = new Predicate<String>() {

        @Override
        public boolean apply(@Nullable final String input) {
            return Strings.isNullOrEmpty(input);
        }
    };

    public static final Predicate<String> isNullOrEmpty() {
        return isNullOrEmpty;
    }

    private static final Function<String, String> emptyToNull = new Function<String, String>() {

        @Override
        @Nullable
        public String apply(
                @Nullable final String input)
        {
            return Strings.isNullOrEmpty(input) ? null : input;
        }
    };

    /**
     * @return a null-safe Function that converts a string to null if it is empty
     */
    @Nonnull
    public static Function<String, String> emptyToNull()
    {
        return emptyToNull;
    }

    public static final Predicate<String> equalsIgnoreCase(@Nullable final String match) {
        return new Predicate<String>() {

            @Override
            public boolean apply(@Nullable final String input) {
                return match == null ? input == null : match.equalsIgnoreCase(input);
            }
        };
    }

    private static final Function<String, Integer> length = new Function<String, Integer>() {

        @Override
        public Integer apply(final String input) {
            if (Strings.isNullOrEmpty(input)) {
                return 0;
            } else {
                return input.length();
            }
        }

        @Override
        public String toString() {
            return "String Length Function";
        }
    };

    public static Function<String, Integer> length() {
        return length;
    }

    @Nonnull
    public static Function<String, String> limit(final int maxCharacters) {
        return new Function<String, String>() {

            @Override
            @Nullable
            public String apply(final String input) {
                return limit(input, maxCharacters);
            }

            @Override
            public String toString() {
                return "String Limit Function";
            }
        };
    }

    @Nullable
    public static String limit(@Nullable final String input, final int maxCharacters) {
        if (input == null || input.length() <= maxCharacters) {
            return input;
        } else {
            return input.substring(0, maxCharacters);
        }
    }

    public static InputStream toInputStream(final String string) {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    private StringsEx() {
    }
}
