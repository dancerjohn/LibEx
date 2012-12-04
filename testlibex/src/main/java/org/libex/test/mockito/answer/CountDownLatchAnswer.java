package org.libex.test.mockito.answer;

import java.util.concurrent.CountDownLatch;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;
import org.mockito.invocation.InvocationOnMock;

/**
 * A {@link WrappedAnswer} that allows a unit test to wait until the mock has
 * been invoked a specified number of times.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class CountDownLatchAnswer<T> extends WrappedAnswer<T> {

	/**
	 * @return a new {@link CountDownLatchAnswer} that will return from
	 *         {@link #await()} after 1 invocation of the mock.
	 */
	public static <T> CountDownLatchAnswer<T> create() {
		return new CountDownLatchAnswer<T>(1);
	}

	/**
	 * @param numberOfInvocations
	 *            the number of invocation for which to block {@link #await()}
	 * @return a new {@link CountDownLatchAnswer} that will return from
	 *         {@link #await()} after {@code numberOfInvocations} invocation of
	 *         the mock.
	 */
	public static <T> CountDownLatchAnswer<T> create(int numberOfInvocations) {
		return new CountDownLatchAnswer<T>(numberOfInvocations);
	}

	private final Object lock = new Object();
	private volatile int numberOfInvocationsToExpect;
	private volatile CountDownLatch countDownLatch;

	private CountDownLatchAnswer(int numberOfInvocationsToExpect) {
		super();
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
	public CountDownLatchAnswer<T> reset(int numberOfInvocations) {
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
	 */
	public boolean await(TimeSpan timeSpan) throws InterruptedException {
		return countDownLatch.await(timeSpan.getDuration(),
				timeSpan.getTimeUnit());
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
	@Nullable
	protected T getResult(InvocationOnMock invocation) throws Throwable {
		synchronized (lock) {
			countDownLatch.countDown();
		}
		return super.getResult(invocation);
	}

}
