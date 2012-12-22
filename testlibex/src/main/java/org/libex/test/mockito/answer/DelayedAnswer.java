package org.libex.test.mockito.answer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.libex.concurrent.TimeSpan;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A {@link WrappedAnswer} that delays the response from the mock invocation by
 * a specified amount of time.
 * 
 * @author John Butler
 * 
 */
public class DelayedAnswer<T> extends WrappedAnswer<T> {

	/**
	 * Creates a {@link DelayedAnswer} that delays the specified time span
	 * 
	 * @param timeSpan
	 *            the amount of time the answer should delay before returning
	 *            from the invocation
	 * @return the new answer
	 */
	public static <T> DelayedAnswer<T> create(TimeSpan timeSpan) {
		return new DelayedAnswer<T>(null, timeSpan);
	}

	/**
	 * Creates a {@link DelayedAnswer} that delays the specified time span
	 * 
	 * @param delegate
	 *            the Answer to wrap
	 * @param timeSpan
	 *            the amount of time the answer should delay before returning
	 *            from the invocation
	 * @return the new answer
	 */
	public static <T> DelayedAnswer<T> create(@Nullable Answer<T> delegate, TimeSpan timeSpan) {
		return new DelayedAnswer<T>(delegate, timeSpan);
	}

	private final TimeSpan timeSpan;

	private DelayedAnswer(@Nullable Answer<T> delegate, TimeSpan timeSpan) {
		super(delegate);
		this.timeSpan = timeSpan;
	}

	@Override
	protected void preProcess(InvocationOnMock invocation) throws Throwable {
		Thread.sleep(timeSpan.getDurationIn(TimeUnit.MILLISECONDS));
		super.preProcess(invocation);
	}
}
