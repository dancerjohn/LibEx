package org.libex.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Optional;

@ThreadSafe
@ParametersAreNonnullByDefault
public final class OptionalsEx {

	@Nonnull
	public static <U> Function<U, Optional<U>> fromNullable() {
		return new Function<U, Optional<U>>() {

			@Override
			@Nonnull
			public Optional<U> apply(@Nullable U arg0) {
				return Optional.fromNullable(arg0);
			}
		};
	}

	@Nonnull
	public static <U> Function<Optional<? extends U>, U> toValueOrNull() {
		return new Function<Optional<? extends U>, U>() {

			@Override
			@Nullable
			public U apply(@Nonnull Optional<? extends U> arg0) {
				return arg0.orNull();
			}
		};
	}

	@Nonnull
	public static <U> Function<Optional<U>, U> toValueOr(final U defaultValue) {
		return new Function<Optional<U>, U>() {

			@Override
			@Nullable
			public U apply(@Nonnull Optional<U> arg0) {
				return arg0.or(defaultValue);
			}
		};
	}

	private OptionalsEx() {
	}
}
