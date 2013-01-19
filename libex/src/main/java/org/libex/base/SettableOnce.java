package org.libex.base;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

/**
 * A wrapper for a field that should only be set once. After the wrapped value
 * has been set once to a non-null value attempting to set it again will throw
 * an IllegalStateException. For a thread-safe implementation see
 * {@link org.libex.concurrent.ConcurrentSettableOnce}
 * 
 * @author John Butler
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
public class SettableOnce<T> implements Supplier<T> {

	/**
	 * @return a new empty {@link SettableOnce}
	 */
	public static <T> SettableOnce<T> empty() {
		return new SettableOnce<T>("Field cannot be set more than once");
	}

	/**
	 * @param name
	 *            the name of the field to be included in error messages
	 * @return a new empty {@link SettableOnce}
	 * @throws NullPointerException
	 *             if name is null
	 */
	public static <T> SettableOnce<T> withName(String name) {
		checkNotNull(name);
		return new SettableOnce<T>("Field " + name + " cannot be set more than once");
	}

	/**
	 * @param message
	 *            the error message to use
	 * @return a new empty {@link SettableOnce}
	 * @throws NullPointerException
	 *             if message is null
	 */
	public static <T> SettableOnce<T> withMessage(String message) {
		return new SettableOnce<T>(message);
	}

	private Optional<T> value = Optional.absent();
	private String message;

	protected SettableOnce(String message) {
		this.message = checkNotNull(message);
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
	public void set(T input) {
		checkState(!value.isPresent(), message);

		value = Optional.of(input);
	}

	/**
	 * @param input
	 *            the value to set if this instance has not already been set
	 * 
	 * @throws NullPointerException
	 *             if value has not been set and input is null
	 */
	public void setIfAbsent(T input) {
		if (!value.isPresent()) {
			value = Optional.of(input);
		}
	}

	/**
	 * @return an Optional containing the value if present
	 */
	@Nonnull
	public Optional<T> getOptional() {
		return value;
	}

	/**
	 * @return the wrapped value if set, otherwise null
	 */
	@Override
	@Nullable
	public T get() {
		return value.orNull();
	}

	protected String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = checkNotNull(message);
	}

}
