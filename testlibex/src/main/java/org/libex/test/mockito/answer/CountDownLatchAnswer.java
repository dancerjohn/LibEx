package org.libex.test.mockito.answer;

import java.util.concurrent.CountDownLatch;

import javax.annotation.Nullable;

import org.libex.concurrent.TimeSpan;
import org.mockito.invocation.InvocationOnMock;

/**
 * @author John Butler
 * 
 */
public class CountDownLatchAnswer<T> extends WrappedAnswer<T> {

	public static <T> CountDownLatchAnswer<T> create() {
		return new CountDownLatchAnswer<T>(1);
	}

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

	public CountDownLatchAnswer<T> reset() {
		reset(this.numberOfInvocationsToExpect);
		return this;
	}

	public CountDownLatchAnswer<T> reset(int numberOfInvocations) {
		synchronized (lock) {
			while (countDownLatch.getCount() > 0) {
				countDownLatch.countDown();
			}

			this.numberOfInvocationsToExpect = numberOfInvocations;
			this.countDownLatch = new CountDownLatch(numberOfInvocationsToExpect);

		}
		return this;
	}

	public void await() throws InterruptedException {
		countDownLatch.await();
	}

	public boolean await(TimeSpan timeSpan) throws InterruptedException {
		return countDownLatch.await(timeSpan.getDuration(), timeSpan.getTimeUnit());
	}

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
