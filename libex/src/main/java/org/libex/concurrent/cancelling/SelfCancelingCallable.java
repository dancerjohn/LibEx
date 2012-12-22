package org.libex.concurrent.cancelling;

import static com.google.common.base.Preconditions.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * A Callable that will cancel itself after it has been running for a specified
 * time span.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class SelfCancelingCallable<T> implements Callable<T> {

	private final Callable<T> wrappedCallable;
	private final TimeSpan timeout;
	@Nullable
	private final ListeningScheduledExecutorService executorService;
	@Nullable
	private final Timer timer;

	public SelfCancelingCallable(Callable<T> wrappedCallable, TimeSpan timeout, Timer timer) {
		this(wrappedCallable, timeout, checkNotNull(timer, "timer"), null);
	}

	public SelfCancelingCallable(Callable<T> wrappedCallable, TimeSpan timeout, ScheduledExecutorService executorService) {
		this(wrappedCallable, timeout, null, checkNotNull(executorService, "executorService"));
	}

	private SelfCancelingCallable(Callable<T> wrappedCallable, TimeSpan timeout, @Nullable Timer timer, @Nullable ScheduledExecutorService executorService) {
		checkNotNull(wrappedCallable, "callable");
		checkNotNull(timeout, "timeout");

		this.wrappedCallable = wrappedCallable;
		this.timeout = timeout;
		this.executorService = (executorService == null) ? null : MoreExecutors.listeningDecorator(executorService);
		this.timer = timer;
	}

	@Override
	public T call() throws Exception {
		Canceler canceler = new Canceler();
		canceler.scheduleCancellation();

		try {
			return wrappedCallable.call();
		} finally {
			canceler.markComplete();
		}
	}

	/**
	 * Use of an inner class allows the SelfCancelingCallable to be used
	 * multiple times in a thread-safe manner.
	 */
	private class Canceler {
		private final Object lock = new Object();
		@Nullable
		private ScheduledFuture<?> cancellingfuture = null;
		@Nullable
		private TimerTask cancellingTask = null;
		@Nullable
		private Thread callableThread = null;

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

		private class Canceller extends TimerTask {

			@Override
			public void run() {
				synchronized (lock) {
					callableThread.interrupt();
				}
			}
		}
	}
}
