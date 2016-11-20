package org.libex.collect;

import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class CollectionsEx {

    private static final Function<Collection<?>, Integer> TO_SIZE = new Function<Collection<?>, Integer>() {

        @Override
        public Integer apply(
                final Collection<?> input)
        {
            return input == null ? 0 : input.size();
        }
    };

    /**
     * @return a {@link Function} that returns the size of the passed {@link Collection}. The {@link Function} will
     *         return
     *         0 if the passed {@link Collection} is null.
     */
    @Nonnull
    public static Function<Collection<?>, Integer> toSize()
    {
        return TO_SIZE;
    }

    private static final Predicate<Collection<?>> IS_EMPTY = new Predicate<Collection<?>>() {

        @Override
        public boolean apply(
                final Collection<?> input)
        {
            return input == null || input.isEmpty();
        }

    };

    /**
     * @return a {@link Predicate} that returns true if the passed {@link Collection} is null or empty
     */
    @Nonnull
    public static Predicate<Collection<?>> isEmpty()
    {
        return IS_EMPTY;
    }

    /**
     * @return a Function that converts a Collection to a Stream
     */
    public static <T> java.util.function.Function<Collection<T>, Stream<T>> toStream()
    {
        return p -> p.stream();
    }

    private CollectionsEx() {
    }
}
