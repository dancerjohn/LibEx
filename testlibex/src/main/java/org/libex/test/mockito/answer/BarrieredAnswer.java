package org.libex.test.mockito.answer;

import javax.annotation.Nullable;

import org.libex.concurrent.SimpleBarrier;
import org.mockito.invocation.InvocationOnMock;

/**
 * @author John Butler
 * 
 */
public class BarrieredAnswer<T> extends WrappedAnswer<T> {

	public static <T> BarrieredAnswer<T> create() {
		return new BarrieredAnswer<T>();
	}

	private final SimpleBarrier barrier = new SimpleBarrier();

	private BarrieredAnswer() {
		super();
	}

	public void open() {
		barrier.open();
	}

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
