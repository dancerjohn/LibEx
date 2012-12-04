package org.libex.test.mockito.answer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.SimpleBarrier;
import org.mockito.invocation.InvocationOnMock;

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
		return new BarrierredAnswer<T>();
	}

	private final SimpleBarrier barrier = new SimpleBarrier();

	private BarrierredAnswer() {
		super();
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
	@Nullable
	protected T getResult(InvocationOnMock invocation) throws Throwable {
		barrier.await();
		return super.getResult(invocation);
	}

}
