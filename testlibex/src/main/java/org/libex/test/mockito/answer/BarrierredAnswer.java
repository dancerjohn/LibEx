package org.libex.test.mockito.answer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.SimpleBarrier;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A {@link WrappedAnswer} that if closed will block the mock invocation until
 * opened.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class BarrierredAnswer<T> extends WrappedAnswer<T> {

	/**
	 * @return a new open {@link BarrierredAnswer}
	 */
	public static <T> BarrierredAnswer<T> create() {
		return new BarrierredAnswer<T>(null);
	}

	/**
	 * @param delegate
	 *            the Answer to wrap
	 * @return a new open {@link BarrierredAnswer} wrapping the passed delegate
	 */
	public static <T> BarrierredAnswer<T> create(@Nullable Answer<T> delegate) {
		return new BarrierredAnswer<T>(delegate);
	}

	private final SimpleBarrier barrier = new SimpleBarrier();

	private BarrierredAnswer(@Nullable Answer<T> delegate) {
		super(delegate);
		barrier.open();
	}

	/**
	 * Opens the barrier allowing mock invocations to proceed. Any invocations
	 * after this call will not be blocked and any currently blocks invocations
	 * will be released.
	 */
	public void open() {
		barrier.open();
	}

	/**
	 * Closes the barrier blocking any future mock invocations until the barrier
	 * is opened.
	 */
	public void close() {
		barrier.close();
	}

	@Override
	protected void preProcess(InvocationOnMock invocation) throws Throwable {
		barrier.await();
		super.preProcess(invocation);
	}
}
