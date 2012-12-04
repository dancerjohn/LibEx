package org.libex.collect;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Predicate;
import com.google.common.collect.Constraint;

@NotThreadSafe
@ParametersAreNonnullByDefault
public final class ConstraintsEx {

	public static <T> Constraint<T> constrainedWith(final Predicate<T> predicate) {
		return new Constraint<T>() {

			@Override
			public T checkElement(T input) {
				if (!predicate.apply(input)) {
					throw new IllegalArgumentException();
				}
				return input;
			}
		};
	}

	public static <T> Constraint<T> constrainedWith(final Predicate<T> predicate, final String message) {
		return new Constraint<T>() {

			@Override
			public T checkElement(T input) {
				if (!predicate.apply(input)) {
					throw new IllegalArgumentException(message);
				}
				return input;
			}
		};
	}

	private ConstraintsEx() {
	}
}
