package org.libex.concurrent.cancelling;

import static com.google.common.base.Preconditions.*;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;
import org.libex.concurrent.cancelling.SelfCancelingCallable.SelfCancelingCallableBuilder;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ForwardingListeningExecutorService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class CancelingListeningExecutorService extends ForwardingListeningExecutorService {

	private final ListeningExecutorService delegate;

	private final Optional<ListeningScheduledExecutorService> scheduledService;
	private final Optional<Timer> timer;

	CancelingListeningExecutorService(ExecutorService delegate,
			ScheduledExecutorService scheduledService) {
		this(delegate,
				Optional.of(MoreExecutors.listeningDecorator(scheduledService)),
				Optional.<Timer> absent());
	}

	CancelingListeningExecutorService(ExecutorService delegate,
			Timer timer) {
		this(delegate,
				Optional.<ListeningScheduledExecutorService> absent(),
				Optional.of(timer));
	}

	private CancelingListeningExecutorService(ExecutorService delegate,
			Optional<ListeningScheduledExecutorService> scheduledService, Optional<Timer> timer) {
		super();
		this.delegate = MoreExecutors.listeningDecorator(checkNotNull(delegate));
		this.scheduledService = scheduledService;
		this.timer = timer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.common.util.concurrent.ForwardingListeningExecutorService#
	 * delegate()
	 */
	@Override
	protected ListeningExecutorService delegate() {
		return delegate;
	}

	public <T> ListenableFuture<T> submit(Callable<T> task, TimeSpan timeout) {
		return submitWithTimeout(task, timeout, null);
	}

	public <T> ListenableFuture<T> submit(Callable<T> task, TimeSpan timeout,
			CancelingExecutorObserver<T> observer) {
		return submitWithTimeout(task, timeout, observer);
	}

	public ListenableFuture<?> submit(Runnable task, TimeSpan timeout) {
		return submitWithTimeout(createCallable(task, null), timeout, null);
	}

	public <T> ListenableFuture<T> submit(Runnable task, TimeSpan timeout,
			CancelingExecutorObserver<T> observer) {
		return submitWithTimeout(createCallable(task, (T) null), timeout, observer);
	}

	public <T> ListenableFuture<T> submit(Runnable task, TimeSpan timeout, T result) {
		return submitWithTimeout(createCallable(task, result), timeout, null);
	}

	public <T> ListenableFuture<T> submit(Runnable task, TimeSpan timeout, T result,
			CancelingExecutorObserver<T> observer) {
		return submitWithTimeout(createCallable(task, result), timeout, observer);
	}

	private <T> Callable<T> createCallable(final Runnable task, final T result) {
		return new Callable<T>() {

			@Override
			public T call() throws Exception {
				task.run();
				return result;
			}
		};
	}

	private <T> ListenableFuture<T> submitWithTimeout(Callable<T> task, TimeSpan timeout,
			CancelingExecutorObserver<T> observer) {
		SelfCancelingCallableBuilder<T> builder = SelfCancelingCallable.<T> newBuilder()
				.setCallable(task)
				.setTimeout(timeout)
				.setObserver(observer);

		if (scheduledService.isPresent()) {
			builder.setExecutorService(scheduledService.get());
		} else {
			builder.setTimer(timer.get());
		}

		SelfCancelingCallable<T> callable = builder.build();
		return delegate().submit(callable);
	}
}
