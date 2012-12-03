package org.libex.test.mockito.answer;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.libex.concurrent.TimeSpan;
import org.mockito.invocation.InvocationOnMock;

/**
 * @author John Butler
 * 
 */
public class DelayedAnswer<T> extends WrappedAnswer<T> {

	public static <T> DelayedAnswer<T> create(TimeSpan timeSpan) {
		return new DelayedAnswer<T>(timeSpan);
	}

	private final TimeSpan timeSpan;

	private DelayedAnswer(TimeSpan timeSpan) {
		super();
		this.timeSpan = timeSpan;
	}

	@Override
	@Nullable
	protected T getResult(InvocationOnMock invocation) throws Throwable {
		Thread.sleep(timeSpan.getDurationIn(TimeUnit.MILLISECONDS));
		return super.getResult(invocation);
	}

}
