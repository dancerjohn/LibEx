package org.libex.concurrent.cancelling;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
	private final Object lock = new Object();
	@Nullable
	private final ListeningScheduledExecutorService executorService;
	@Nullable
	private final Timer timer;

	@Nullable
	private ScheduledFuture<?> cancellingfuture = null;
	@Nullable
	private Thread callableThread = null;
	@Nullable
	private TimerTask cancellingTask;

	public SelfCancellingCallable(Callable<T> wrappedCallable, TimeSpan timeout) {
		this(wrappedCallable, timeout, null);
	}

	public SelfCancellingCallable(Callable<T> wrappedCallable, TimeSpan timeout, @Nullable ListeningScheduledExecutorService executorService) {
		super();
		this.wrappedCallable = wrappedCallable;
		this.timeout = timeout;
		this.executorService = executorService;
		this.timer = (executorService == null) ? new Timer() : null;
	}

	@Override
	public T call() throws Exception {
		scheduleCancellation();

		try {
			return wrappedCallable.call();
		} finally {
			markComplete();
		}
	}

	private void scheduleCancellation() {
		callableThread = Thread.currentThread();
		Canceller canceller = new Canceller();

		if (executorService != null) {
			cancellingfuture = executorService.schedule(canceller, timeout.getDuration(), timeout.getTimeUnit());
		} else
		{
			timer.schedule(canceller, timeout.getDurationIn(TimeUnit.MILLISECONDS));
			cancellingTask = canceller;
		}
	}

	private void markComplete() {
		try {
			synchronized (lock) {
				if (cancellingfuture != null) {
					cancellingfuture.cancel(true);
				}

				if (cancellingTask != null) {
					cancellingTask.cancel();
				}
			}
		} catch (Exception e) {
			// NO OP
		}
	}

	private class Canceller extends TimerTask implements Runnable {

		@Override
		public void run() {
			synchronized (lock) {
				callableThread.interrupt();
			}
		}
	}
}
