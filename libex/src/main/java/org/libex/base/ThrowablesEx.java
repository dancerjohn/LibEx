package org.libex.base;

import com.google.common.base.Function;
import com.google.common.base.Throwables;

/**
 * Utilities for working with {@link Throwable}s
 */
public final class ThrowablesEx {

    private ThrowablesEx()
    {
    }

    private static final Function<Throwable, String> AS_STRING = new Function<Throwable, String>() {

        @Override
        public String apply(final Throwable throwable)
        {
            return Throwables.getStackTraceAsString(throwable);
        }
    };

    private static final Function<Throwable, String> MESSAGE = new Function<Throwable, String>() {

        @Override
        public String apply(final Throwable throwable)
        {
            return throwable.getMessage();
        }
    };

    /**
     * Returns a {@link Function} that when called will convert a
     * {@link Throwable}'s stacktrace to a {@link String}
     *
     * @return a {@link Function} that when called will convert a
     *         {@link Throwable}'s stacktrace to a {@link String}
     */
    public static Function<Throwable, String> stackTraceAsString()
    {
        return AS_STRING;
    }

    /**
     * Returns a {@link Function} that when called will return a {@link String}
     * equal to that returned by calling {@link Throwable#getMessage()}
     *
     * @return a {@link Function} that when called will return a {@link String}
     *         equal to that returned by calling {@link Throwable#getMessage()}
     */
    public static Function<Throwable, String> message()
    {
        return MESSAGE;
    }

}
