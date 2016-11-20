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

	private boolean mayBeSet = true;

	public FinalizableSettableOnce() {
		super();
	}

	/**
	 * @param name
	 *            the name of the field to be included in error messages
	 * @throws NullPointerException
	 *             if name is null
	 */
	public FinalizableSettableOnce(String name) {
		super(name);
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

	@Override
	public void finalize() {
		mayBeSet = false;
		setMessage(getMessage().replace(" cannot be set more than once", " has been finalized"));
	}
}
