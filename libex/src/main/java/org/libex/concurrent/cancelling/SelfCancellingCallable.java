package org.libex.concurrent.cancelling;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class SelfCancellingCallable<T> implements Callable<T> {

	private final Callable<T> wrappedCallable;
	private final TimeSpan timeout;
	@Nullable
	private final ListeningScheduledExecutorService executorService;
	private final Object lock = new Object();

	@Nullable
	private ScheduledFuture<?> cancellingfuture = null;
	@Nullable
	private Thread callableThread = null;
	@Nullable
	private Timer timer = null;

	public SelfCancellingCallable(Callable<T> wrappedCallable, TimeSpan timeout) {
		this(wrappedCallable, timeout, null);
	}

	public SelfCancellingCallable(Callable<T> wrappedCallable, TimeSpan timeout, @Nullable ListeningScheduledExecutorService executorService) {
		super();
		this.wrappedCallable = wrappedCallable;
		this.timeout = timeout;
		this.executorService = executorService;
	}

	@Override
	public T call() throws Exception {
		callableThread = Thread.currentThread();
		cancellingfuture = executorService.schedule(new Canceller(), timeout.getDuration(), timeout.getTimeUnit());

		try {
			return wrappedCallable.call();
		} finally {
			try {
				synchronized (lock) {
					cancellingfuture.cancel(true);
				}
			} catch (Exception e) {
				// NO OP
			}
		}
	}

	private class Canceller implements Runnable {

		@Override
		public void run() {
			synchronized (lock) {
				callableThread.interrupt();
			}
		}
	}
}
