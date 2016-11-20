package org.libex.collect;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.transform;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.libex.base.FoldFunction;

import com.google.common.base.Function;
import com.google.common.base.Optional;

/**
 * Utilities on Iterable instances.
 * 
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class IterablesEx {

	    /**
     * Finds first result that is not Absent. Iterates over the set of Functions
     * returning the result of the first Function that returns a present
     * Optional. If no function returns a Present, an Absent is returned.
     * 
     * @param argument
     *            the value to pass to each Function
     * @param functions
     *            the set of Functions
     * @return the result of the first Function that returns a present Optional.
     *         If no function returns a Present, an Absent is returned.
     * 
     * @param <T>
     *            input type
     * @param <U>
     *            output type
     */
	@Nonnull
	public static <T, U> Optional<U> findFirst(@Nullable final T argument,
			final Iterable<? extends Function<? super T, Optional<U>>> functions) {
		return IteratorsEx.findFirst(argument, functions.iterator());
	}

    @Nonnull
    public static <A, B> B foldLeft(final Iterable<? extends A> iterable,
            @Nullable final B initialValue,
            final FoldFunction<? super A, B> foldFunction) {
        return IteratorsEx.foldLeft(iterable.iterator(), initialValue, foldFunction);
    }

    /**
     * Creates a {@link Function} that takes a {@link Function} from A to B and creates a {@link Function} from
     * {@code Iterable<A>} to {@code Iterable<B>}. The created {@link Function} will return {@code null} if passed
     * {@code null}
     * 
     * @param delegate
     *            the wrapped {@link Function}
     * @return the created {@link Iterable} {@link Function}
     * 
     * @param <T>
     *            input type
     * @param <U>
     *            output type
     */
    public static <T, U> Function<Iterable<T>, Iterable<U>> asCollectionFunction(
            final Function<? super T, ? extends U> delegate)
    {
        checkNotNull(delegate);

        return new Function<Iterable<T>, Iterable<U>>() {

            @Override
            @Nullable
            public Iterable<U> apply(
                    @Nullable final Iterable<T> input)
            {
                if (input == null) {
                    return null;
                } else {
                    return transform(input, delegate);
                }
            }
        };
    }

    public static <T> Function<? extends Iterable<T>, T> toFirst(final T defaultValue)
    {
        return new Function<Iterable<T>, T>() {

            @Override
            public T apply(
                    final Iterable<T> input)
            {
                return getFirst(input, defaultValue);
            }
        };
    }

    public static <T> Function<? extends Iterable<T>, T> toLast(
            final T defaultValue)
    {
        return new Function<Iterable<T>, T>() {

            @Override
            public T apply(
                    final Iterable<T> input)
            {
                return getLast(input, defaultValue);
            }
        };
    }

    /**
     * @return a Function that converts an Iterable to a Stream
     */
    public static <T> java.util.function.Function<Iterable<T>, Stream<T>> toStream()
    {
        return p -> StreamSupport.stream(p.spliterator(), false);
    }

	private IterablesEx() {
	}
}
