package org.libex.base;

import static com.google.common.base.Preconditions.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * A wrapper for a field that should only be set once and can be finalized.
 * After the wrapped value has been set once to a non-null value or it has been
 * finalized via {@link #finalize()}, attempting to set it again will throw an
 * IllegalStateException. For a thread-safe implementation see
 * {@link org.libex.concurrent.ConcurrentSettableOnce}.
 * 
 * @author John Butler
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
public class FinalizableSettableOnce<T> extends SettableOnce<T> {

	/**
	 * @return a new empty {@link SettableOnce}
	 */
	public static <T> FinalizableSettableOnce<T> empty() {
		return new FinalizableSettableOnce<T>("Field cannot be set more than once");
	}

	/**
	 * @param name
	 *            the name of the field to be included in error messages
	 * @return a new empty {@link SettableOnce}
	 * @throws NullPointerException
	 *             if name is null
	 */
	public static <T> FinalizableSettableOnce<T> withName(String name) {
		checkNotNull(name);
		return new FinalizableSettableOnce<T>("Field " + name + " cannot be set more than once");
	}

	/**
	 * @param message
	 *            the error message to use
	 * @return a new empty {@link SettableOnce}
	 * @throws NullPointerException
	 *             if message is null
	 */
	public static <T> FinalizableSettableOnce<T> withMessage(String message) {
		return new FinalizableSettableOnce<T>(message);
	}

	private boolean mayBeSet = true;

	private FinalizableSettableOnce(String message) {
		super(message);
	}

	/**
	 * @param input
	 *            the value to set
	 * 
	 * @throws IllegalStateException
	 *             if the value has already been set
	 * @throws NullPointerException
	 *             if input is null
	 */
	@Override
	public void set(T input) {
		checkState(mayBeSet, getMessage());

		super.set(input);
	}

	/**
	 * @param input
	 *            the value to set if this instance has not already been set
	 * 
	 * @throws NullPointerException
	 *             if value has not been set and input is null
	 */
	@Override
	public void setIfAbsent(T input) {
		if (mayBeSet) {
			super.setIfAbsent(input);
		}
	}

	/**
	 * Marks this instance as no longer being setting.
	 */
	public void makeImmutable() {
		mayBeSet = false;
		setMessage(getMessage().replace(" cannot be set more than once", " has been finalized"));
	}
}
