package org.libex.concurrent.cancelling;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;

@ThreadSafe
@ParametersAreNonnullByDefault
public final class CancelingExecutors {

	private static final Timer timer = new Timer("Default Canceling Executor Timer", true);

	public static CancelingListeningExecutorService cancelingExecutor(ExecutorService delegate) {
		return new CancelingListeningExecutorService(delegate, timer);
	}

	public static CancelingListeningExecutorService cancelingExecutor(ExecutorService delegate,
			ScheduledExecutorService scheduledService) {
		return new CancelingListeningExecutorService(delegate, scheduledService);
	}

	public static CancelingListeningExecutorService cancelingExecutor(ExecutorService delegate,
			Timer timer) {
		return new CancelingListeningExecutorService(delegate, timer);
	}

	public static FixedTimeoutCancelingListeningExecutorService cancelingExecutor(ExecutorService delegate,
			TimeSpan timeout) {
		return new FixedTimeoutCancelingListeningExecutorService(delegate, timer, timeout);
	}

	public static FixedTimeoutCancelingListeningExecutorService cancelingExecutor(ExecutorService delegate,
			TimeSpan timeout,
			ScheduledExecutorService scheduledService) {
		return new FixedTimeoutCancelingListeningExecutorService(delegate, scheduledService, timeout);
	}

	public static FixedTimeoutCancelingListeningExecutorService cancelingExecutor(ExecutorService delegate,
			TimeSpan timeout,
			Timer timer) {
		return new FixedTimeoutCancelingListeningExecutorService(delegate, timer, timeout);
	}

	private CancelingExecutors() {
	}
}
