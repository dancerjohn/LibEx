package org.libex.test.mockito.answer;

import static com.google.common.base.Preconditions.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class ThrowingAnswer<T> implements Answer<T> {

	public static <T> ThrowingAnswer<T> wrap(Throwable result) {
		return new ThrowingAnswer<T>(result);
	}

	private final Throwable result;

	private ThrowingAnswer(Throwable result) {
		checkNotNull(result);
		this.result = result;
	}

	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		throw result;
	}
}
