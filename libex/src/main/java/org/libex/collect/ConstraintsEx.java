package org.libex.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Constraint;

/**
 * Utility methods for {@link Constraint}
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class ConstraintsEx {

	/**
	 * Creates a Constraint wrapping the passed Predicate. The Constraint will
	 * throw an IllegalArgumentException if the predicate returns false for the
	 * value passed to the Constraint
	 * 
	 * @param predicate
	 *            the predicate to wrap
	 * @return a Constraint wrapping the passed Predicate
	 * 
	 * @throws NullPointerException
	 *             if predicate is null
	 */
	@Nonnull
	public static <T> Constraint<T> constrainedWith(final Predicate<T> predicate) {
		return constrainedWith(predicate, "");
	}

	/**
	 * Creates a Constraint wrapping the passed Predicate. The Constraint will
	 * throw an IllegalArgumentException containing the passed message if the
	 * predicate returns false for the value passed to the Constraint
	 * 
	 * @param predicate
	 *            the predicate to wrap
	 * @param message
	 *            the message to include in any thrown exception
	 * @return a Constraint wrapping the passed Predicate
	 * 
	 * @throws NullPointerException
	 *             if predicate or message is null
	 */
	public static <T> Constraint<T> constrainedWith(
			final Predicate<T> predicate, final String message) {
		return constrainedWith(predicate, Suppliers.ofInstance(message));
	}

	/**
	 * Creates a Constraint wrapping the passed Predicate. The Constraint will
	 * throw an IllegalArgumentException if the predicate returns false for the
	 * value passed to the Constraint
	 * 
	 * @param predicate
	 *            the predicate to wrap
	 * @param messageSupplier
	 *            a supplier of the message to include in any thrown exception
	 * @return a Constraint wrapping the passed Predicate
	 * 
	 * @throws NullPointerException
	 *             if predicate or messageSupplier is null
	 */
	public static <T> Constraint<T> constrainedWith(
			final Predicate<T> predicate, final Supplier<String> messageSupplier) {
		checkNotNull(predicate, "predicate");
		checkNotNull(messageSupplier, "messageSupplier");

		return new Constraint<T>() {

			@Override
			public T checkElement(T input) {
				if (!predicate.apply(input)) {
					throw new IllegalArgumentException(messageSupplier.get());
				}
				return input;
			}
		};
	}

	private ConstraintsEx() {
	}
}
