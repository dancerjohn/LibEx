package org.libex.collect;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

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
	 */
	@Nonnull
	public static <T, U> Optional<U> findFirst(@Nullable T argument,
			Iterator<? extends Function<? super T, Optional<U>>> functions) {

		while (functions.hasNext()) {
			Function<? super T, Optional<U>> function = functions.next();
			Optional<U> result = function.apply(argument);
			if (result.isPresent()) {
				return result;
			}
		}

		return Optional.absent();
	}

	private IteratorsEx() {
	}
}
