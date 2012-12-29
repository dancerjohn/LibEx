package org.libex.concurrent.cancelling;

import static com.google.common.base.Preconditions.*;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.libex.concurrent.TimeSpan;

import com.google.common.util.concurrent.ListenableFuture;

@NotThreadSafe
@ParametersAreNonnullByDefault
public class FixedTimeoutCancelingListeningExecutorService extends CancelingListeningExecutorService {

	private final TimeSpan timeout;

	FixedTimeoutCancelingListeningExecutorService(ExecutorService delegate,
			ScheduledExecutorService scheduledService, TimeSpan timeout) {
		super(delegate, scheduledService);

		this.timeout = checkNotNull(timeout);
	}

	FixedTimeoutCancelingListeningExecutorService(ExecutorService delegate,
			Timer timer, TimeSpan timeout) {
		super(delegate, timer);

		this.timeout = checkNotNull(timeout);
	}

	@Override
	public <T> ListenableFuture<T> submit(Callable<T> task) {
		return super.submit(task, timeout);
	}

	public <T> ListenableFuture<T> submit(Callable<T> task,
			CancelingExecutorObserver<T> observer) {
		return super.submit(task, timeout, observer);
	}

	@Override
	public ListenableFuture<?> submit(Runnable task) {
		return super.submit(task, timeout);
	}

	public <T> ListenableFuture<T> submit(Runnable task,
			CancelingExecutorObserver<T> observer) {
		return super.submit(task, timeout, observer);
	}

	@Override
	public <T> ListenableFuture<T> submit(Runnable task, T result) {
		return super.submit(task, timeout, result);
	}

	public <T> ListenableFuture<T> submit(Runnable task, T result,
			CancelingExecutorObserver<T> observer) {
		return super.submit(task, timeout, result, observer);
	}
}
