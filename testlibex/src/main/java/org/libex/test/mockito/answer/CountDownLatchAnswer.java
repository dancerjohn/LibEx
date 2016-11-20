package org.libex.test.mockito.answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.Duration;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A {@link WrappedAnswer} that allows a unit test to wait until the mock has
 * been invoked a specified number of times.
 * 
 * @param <T>
 *            type to be returned by the Answer
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class CountDownLatchAnswer<T> extends WrappedAnswer<T> {

	    /**
     * @return a new {@link CountDownLatchAnswer} that will return from {@link #await()} after 1 invocation of the mock.
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
	public static <T> CountDownLatchAnswer<T> create() {
		return new CountDownLatchAnswer<T>(null, 1);
	}

	    /**
     * @param delegate
     *            the Answer to wrap
     * @return a new {@link CountDownLatchAnswer} that will return from {@link #await()} after 1 invocation of the mock.
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
	public static <T> CountDownLatchAnswer<T> create(@Nullable final Answer<T> delegate) {
		return new CountDownLatchAnswer<T>(delegate, 1);
	}

	    /**
     * @param numberOfInvocations
     *            the number of invocation for which to block {@link #await()}
     * @return a new {@link CountDownLatchAnswer} that will return from {@link #await()} after
     *         {@code numberOfInvocations} invocation of
     *         the mock.
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
	public static <T> CountDownLatchAnswer<T> create(final int numberOfInvocations) {
		return new CountDownLatchAnswer<T>(null, numberOfInvocations);
	}

	    /**
     * @param delegate
     *            the Answer to wrap
     * @param numberOfInvocations
     *            the number of invocation for which to block {@link #await()}
     * @return a new {@link CountDownLatchAnswer} that will return from {@link #await()} after
     *         {@code numberOfInvocations} invocation of
     *         the mock.
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
	public static <T> CountDownLatchAnswer<T> create(@Nullable final Answer<T> delegate, final int numberOfInvocations) {
		return new CountDownLatchAnswer<T>(delegate, numberOfInvocations);
	}

	private final Object lock = new Object();
	private volatile int numberOfInvocationsToExpect;
	private volatile CountDownLatch countDownLatch;

	private CountDownLatchAnswer(@Nullable final Answer<T> delegate, final int numberOfInvocationsToExpect) {
		super(delegate);
		this.numberOfInvocationsToExpect = numberOfInvocationsToExpect;
		this.countDownLatch = new CountDownLatch(numberOfInvocationsToExpect);
	}

	/**
	 * Causes the answer to wait again for the configured number of invocations.
	 * 
	 * @return this instance
	 */
	public CountDownLatchAnswer<T> reset() {
		reset(this.numberOfInvocationsToExpect);
		return this;
	}

	/**
	 * Causes the answer to wait again for the specified number of invocations.
	 * 
	 * @param numberOfInvocations
	 *            the number of invocations to wait
	 * @return this instance
	 */
	public CountDownLatchAnswer<T> reset(final int numberOfInvocations) {
		synchronized (lock) {
			while (countDownLatch.getCount() > 0) {
				countDownLatch.countDown();
			}

			this.numberOfInvocationsToExpect = numberOfInvocations;
			this.countDownLatch = new CountDownLatch(
					numberOfInvocationsToExpect);

		}
		return this;
	}

	    /**
     * Waits for the mock to be invoked the configured number of times
     * 
     * @throws InterruptedException
     *             if call is interrupted while waiting on latch
     */
	public void await() throws InterruptedException {
		countDownLatch.await();
	}

	    /**
     * Waits for the mock to be invoked the configured number of times
     * 
     * @param timeSpan
     *            the maximum amount of time wait
     * @return true if the mock was invoked the specified number of time, false
     *         if the timeout was reached
     * @throws InterruptedException
     *             if call is interrupted while waiting on latch
     */
    public boolean await(
            final Duration timeSpan) throws InterruptedException
    {
        return countDownLatch.await(timeSpan.getMillis(),
                TimeUnit.MILLISECONDS);
    }

	/**
	 * @return the number of expected invocations remaining
	 */
	public long getLatchCount() {
		synchronized (lock) {
			return countDownLatch.getCount();
		}
	}

	@Override
	protected void preProcess(final InvocationOnMock invocation) throws Throwable {
		synchronized (lock) {
			countDownLatch.countDown();
		}
		super.preProcess(invocation);
	}
}
