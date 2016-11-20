package org.libex.test.mockito.answer;

import javax.annotation.Nullable;

import org.joda.time.Duration;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * A {@link WrappedAnswer} that delays the response from the mock invocation by
 * a specified amount of time.
 * 
 * @param <T>
 *            type to be returned by the Answer
 */
public class DelayedAnswer<T> extends WrappedAnswer<T> {

    /**
     * Creates a {@link DelayedAnswer} that delays the specified time span
     * 
     * @param timeSpan
     *            the amount of time the answer should delay before returning
     *            from the invocation
     * @return the new answer
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
    public static <T> DelayedAnswer<T> create(
            final Duration timeSpan)
    {
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
     * 
     * @param <T>
     *            type to be returned by the Answer
     */
    public static <T> DelayedAnswer<T> create(
            @Nullable final Answer<T> delegate,
            final Duration timeSpan)
    {
        return new DelayedAnswer<T>(delegate, timeSpan);
    }

    private final Duration timeSpan;

    private DelayedAnswer(
            @Nullable final Answer<T> delegate,
            final Duration timeSpan) {
        super(delegate);
        this.timeSpan = timeSpan;
    }

    @Override
    protected void preProcess(
            final InvocationOnMock invocation) throws Throwable
    {
        Thread.sleep(timeSpan.getMillis());
        super.preProcess(invocation);
    }
}
