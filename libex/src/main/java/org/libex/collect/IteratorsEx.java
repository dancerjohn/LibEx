package org.libex.collect;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.libex.base.FoldFunction;

import com.google.common.base.Function;
import com.google.common.base.Optional;

/**
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class IteratorsEx {

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
			final Iterator<? extends Function<? super T, Optional<U>>> functions) {

		while (functions.hasNext()) {
			Function<? super T, Optional<U>> function = functions.next();
			Optional<U> result = function.apply(argument);
			if (result.isPresent()) {
				return result;
			}
		}

		return Optional.absent();
	}

    @Nonnull
    public static <A, B> B foldLeft(final Iterator<? extends A> iterator,
            @Nullable final B initialValue,
            final FoldFunction<? super A, B> foldFunction) {
        B result = initialValue;
        while (iterator.hasNext()) {
            A value = iterator.next();
            result = foldFunction.apply(result, value);
        }
        return result;
    }

	private IteratorsEx() {
	}
}
