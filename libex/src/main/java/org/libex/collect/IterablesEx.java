package org.libex.collect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

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
	 */
	@Nonnull
	public static <T, U> Optional<U> findFirst(@Nullable T argument,
			Iterable<? extends Function<? super T, Optional<U>>> functions) {
		return IteratorsEx.findFirst(argument, functions.iterator());
	}

	private IterablesEx() {
	}
}
