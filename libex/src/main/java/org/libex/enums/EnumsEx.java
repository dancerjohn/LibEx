package org.libex.enums;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

@ThreadSafe
@ParametersAreNonnullByDefault
public final class EnumsEx {

	private static final Function<Enum<?>, String> toName = new Function<Enum<?>, String>() {

		/**
		 * @throws NullPointerException
		 *             if null is passed
		 */
		@Override
		@Nonnull
		public String apply(@Nonnull Enum<?> arg0) {
			return arg0.name();
		}
	};

	/**
	 * @return a {@link Function} that returns the name of a passed {@link Enum}
	 *         instance
	 * @throws NullPointerException
	 *             if null is passed to the returned {@link Function}
	 */
	public static Function<Enum<?>, String> toName() {
		return toName;
	}

	private EnumsEx() {
	}
}
