package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class FunctionsEx {

    /**
     * Creates a {@link Function} that wraps a provided {@link Function}. When the
     * returned is passed {@code null} it returns {@code null}. Otherwise it returns
     * the result of the wrapped {@link Function}.
     * 
     * @param wrapped
     *            {@link Function} to wrap
     * @return null-safe {@link Function}
     * 
     * @param <I>
     *            input type
     * @param <O>
     *            output type
     */
    public static <I, O> Function<I, O> nullableOf(final Function<I, O> wrapped)
    {
        return new Function<I, O>() {

            @Override
            public O apply(final I input)
            {
                if (input == null) {
                    return null;
                } else {
                    return wrapped.apply(input);
                }
            }
        };
    }

    /**
     * Converts the passed Guava {@code Function} to a {@code java.unit.function.Function}
     * 
     * @param <I>
     *            the {@code Function} input parameter type
     * @param <O>
     *            the {@code Function} output type
     * @param guava
     *            the Guava {@code Function} to convert
     * @return the passed {@code Function} wrapped as a {@code java.unit.function.Function}
     */
    public static <I, O> java.util.function.Function<I, O> fromGuava(
            final Function<I, O> guava)
    {
        checkNotNull(guava);
        return o -> guava.apply(o);
    }

    /**
     * Converts the passed Guava {@code Function} to a {@code java.unit.function.Function}
     * 
     * @param <I>
     *            the {@code Function} input parameter type
     * @param <O>
     *            the {@code Function} output type
     * @param guava
     *            the Guava {@code Function} to convert
     * @return the passed {@code Function} wrapped as a {@code java.unit.function.Function}
     */
    public static <I, O> java.util.function.Function<I, O> toJava(
            final Function<I, O> guava)
    {
        checkNotNull(guava);
        return o -> guava.apply(o);
    }

    /**
     * Converts the passed Java {@code Function} to a Guava {@code Function}
     * 
     * @param <I>
     *            the {@code Function} input parameter type
     * @param <O>
     *            the {@code Function} output type
     * @param input
     *            the Java {@code Function} to convert
     * @return the passed {@code java.unit.function.Function} wrapped as a Guava {@code Function}
     */
    public static <I, O> Function<I, O> fromJava(
            final java.util.function.Function<I, O> input)
    {
        checkNotNull(input);
        return o -> input.apply(o);
    }

    /**
     * Converts the passed Java {@code Function} to a Guava {@code Function}
     * 
     * @param <I>
     *            the {@code Function} input parameter type
     * @param <O>
     *            the {@code Function} output type
     * @param input
     *            the Java {@code Function} to convert
     * @return the passed {@code java.unit.function.Function} wrapped as a Guava {@code Function}
     */
    public static <I, O> Function<I, O> toGuava(
            final java.util.function.Function<I, O> input)
    {
        checkNotNull(input);
        return o -> input.apply(o);
    }

    private FunctionsEx()
    {
    }
}
