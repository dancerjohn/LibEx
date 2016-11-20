package org.libex.test.mockito.answer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Optional;

/**
 * Allows for wrapping an answer while adding behavior.
 * 
 * @param <T>
 *            type to be returned by the Answer
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public abstract class WrappedAnswer<T> implements Answer<T> {

	@Nullable
	private final Answer<T> delegate;

	protected WrappedAnswer(@Nullable final Answer<T> delegate) {
		super();
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock
	 * )
	 */
	@Override
	public T answer(final InvocationOnMock invocation) throws Throwable {
		preProcess(invocation);

		Optional<T> result = Optional.absent();
		Optional<Throwable> thrown = Optional.absent();
		if (delegate != null) {
			try {
				T value = delegate.answer(invocation);
				result = Optional.fromNullable(value);
			} catch (Throwable t) {
				thrown = Optional.of(t);
			}
		}
		return postProcess(invocation, result, thrown);
	}

	    /**
     * Can be overridden to add behavior.
     * 
     * @param invocation
     *            value passed to {@code answer}
     * @throws Throwable
     *             if a Throwable is thrown
     */
	protected void preProcess(final InvocationOnMock invocation) throws Throwable {
		// NO OP, can be overridden
	}

	    /**
     * Can be overridden to add behavior.
     * 
     * NOTE: any overriding classes should call {@code super.postProcess}
     * 
     * @param invocation
     *            value passed to {@code answer}
     * @param result
     *            value returned from {@code delegate} if any
     * @param thrown
     *            exception thrown from {@code delegate} if any
     * @return the value that should be returned from {@link #answer(InvocationOnMock)}
     * @throws Throwable
     *             if a Throwable is thrown
     */
	protected T postProcess(final InvocationOnMock invocation, final Optional<T> result, final Optional<? extends Throwable> thrown) throws Throwable {
		if (thrown.isPresent()) {
			throw thrown.get();
		} else {
			return result.orNull();
		}
	}
}
