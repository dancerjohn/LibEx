package org.libex.concurrent;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.atomic.AtomicBoolean;

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
public class ConcurrentFinalizableSettableOnce<T> extends ConcurrentSettableOnce<T> {

	private AtomicBoolean mayBeSet = new AtomicBoolean(true);

	public ConcurrentFinalizableSettableOnce() {
		super();
	}

	/**
	 * @param name
	 *            the name of the field to be included in error messages
	 * @throws NullPointerException
	 *             if name is null
	 */
	public ConcurrentFinalizableSettableOnce(String name) {
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
		synchronized (mayBeSet) {
			checkState(mayBeSet.get(), getMessage());

			super.set(input);
		}
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
		synchronized (mayBeSet) {
			if (mayBeSet.get()) {
				super.setIfAbsent(input);
			}
		}
	}

	@Override
	public void finalize() {
		synchronized (mayBeSet) {
			mayBeSet.set(false);
			setMessage(getMessage().replace(" cannot be set more than once", " has been finalized"));
		}
	}
}
