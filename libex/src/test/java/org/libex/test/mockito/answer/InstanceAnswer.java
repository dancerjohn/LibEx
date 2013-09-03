package org.libex.test.mockito.answer;

import javax.annotation.Nullable;
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
public class InstanceAnswer<T> implements Answer<T> {

	public static <T> InstanceAnswer<T> wrap(@Nullable T result) {
		return new InstanceAnswer<T>(result);
	}

	private final T result;

	private InstanceAnswer(@Nullable T result) {
		super();
		this.result = result;
	}

	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		return result;
	}

}
