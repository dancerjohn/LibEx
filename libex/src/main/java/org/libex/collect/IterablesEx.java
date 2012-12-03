package org.libex.collect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Optional;

@NotThreadSafe
@ParametersAreNonnullByDefault
public final class IterablesEx {

	@Nonnull
	public static <T, U> Optional<U> findFirst(@Nullable T argument,
			Iterable<? extends Function<? super T, Optional<U>>> functions) {
		return IteratorsEx.findFirst(argument, functions.iterator());
	}

	private IterablesEx() {
	}
}
