package org.libex.concurrent.profile;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;

import com.google.common.base.Stopwatch;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class Profiler implements Profiling {

	public static class TimedResult {
		private final TimeSpan timeSpan;
		private final Callable<?> callable;

		public TimedResult(TimeSpan timeSpan, Callable<?> callable) {
			super();
			this.timeSpan = timeSpan;
			this.callable = callable;
		}

		public TimeSpan getTimeSpan() {
			return timeSpan;
		}

		public Callable<?> getCallable() {
			return callable;
		}
	}

	private final AtomicReference<Callback> callback = new AtomicReference<Profiler.Callback>();

	public Profiler() {
	}

	@Override
	public <T> T call(Callable<T> callable) throws Exception {
		Callback callback = this.callback.get();
		if (callback != null) {
			return profile(callable, callback);
		} else {
			return callable.call();
		}
	}

	private <T> T profile(Callable<T> callable, Callback callback) throws Exception {
		Stopwatch stopWatch = new Stopwatch();
		stopWatch.start();

		@Nullable
		Exception caughtException = null;
		try {
			T result = callable.call();
			return result;
		} catch (Exception e) {
			caughtException = e;
			throw caughtException;
		} finally {
			TimedResult timedResult = new TimedResult(new TimeSpan(stopWatch.elapsedMillis(), TimeUnit.MILLISECONDS), callable);
			issueCallback(timedResult, callback, caughtException);
		}
	}

	private void issueCallback(TimedResult timedResult, Callback callback, @Nullable Exception caughtException) {
		try {
			callback.processProfileEvent(timedResult);
		} catch (RuntimeException e) {
			if (caughtException == null) {
				throw e;
			} else {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setObserver(Callback callback) {
		checkNotNull(callback);
		checkState(this.callback.get() == null, "Callback already set");

		this.callback.set(callback);
	}
}
