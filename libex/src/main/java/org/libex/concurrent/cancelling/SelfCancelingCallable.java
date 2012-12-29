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
class SelfCancelingCallable<T> implements Callable<T> {

	static class SelfCancelingCallableBuilder<T> {

		private Callable<T> wrappedCallable;
		private TimeSpan timeout;
		private ListeningScheduledExecutorService executorService;
		private Timer timer;
		private CancelingExecutorObserver<T> observer;

		public SelfCancelingCallableBuilder<T> setCallable(Callable<T> wrappedCallable) {
			this.wrappedCallable = wrappedCallable;
			return this;
		}

		public SelfCancelingCallableBuilder<T> setTimeout(TimeSpan timeout) {
			this.timeout = timeout;
			return this;
		}

		public SelfCancelingCallableBuilder<T> setExecutorService(ListeningScheduledExecutorService executorService) {
			this.executorService = executorService;
			return this;
		}

		public SelfCancelingCallableBuilder<T> setTimer(Timer timer) {
			this.timer = timer;
			return this;
		}

		public SelfCancelingCallableBuilder<T> setObserver(CancelingExecutorObserver<T> observer) {
			this.observer = observer;
			return this;
		}

		SelfCancelingCallable<T> build() {
			checkState(wrappedCallable != null);
			checkState(timeout != null);
			checkState(executorService != null || timer != null);

			return new SelfCancelingCallable<T>(wrappedCallable,
					timeout, timer, executorService, observer);
		}
	}

	public static <T> SelfCancelingCallableBuilder<T> newBuilder() {
		return new SelfCancelingCallableBuilder<T>();
	}

	private final Callable<T> wrappedCallable;
	private final TimeSpan timeout;
	@Nullable
	private final ListeningScheduledExecutorService executorService;
	@Nullable
	private final Timer timer;
	@Nullable
	private final CancelingExecutorObserver<T> observer;

	private SelfCancelingCallable(Callable<T> wrappedCallable,
			TimeSpan timeout,
			@Nullable Timer timer,
			@Nullable ScheduledExecutorService executorService,
			@Nullable CancelingExecutorObserver<T> observer) {

		this.wrappedCallable = wrappedCallable;
		this.timeout = timeout;
		this.executorService = (executorService == null) ? null : MoreExecutors.listeningDecorator(executorService);
		this.timer = timer;
		this.observer = observer;
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
				try {
					synchronized (lock) {
						callableThread.interrupt();
					}
				} catch (Exception e) {
					// NO OP
				} finally {
					if (observer != null) {
						try {
							observer.onTaskCanceled(wrappedCallable);
						} catch (Exception e) {
							// NO OP
						}
					}
				}
			}
		}
	}
}
