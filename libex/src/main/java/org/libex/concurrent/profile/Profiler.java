package org.libex.concurrent.profile;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.log4j.Logger;
import org.libex.concurrent.TimeSpan;

import com.google.common.base.Stopwatch;

/**
 * Profiles {@link Callable} instances, determining the length of time taken for
 * the {@link Callable} to complete. This class invokes the callback (if any
 * registered) for each passed {@link Callable}.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class Profiler implements Profiling {

	private static final Logger logger = Logger.getLogger(Profiler.class);

	private final AtomicReference<Callback> callback = new AtomicReference<Profiler.Callback>();

	public Profiler() {
	}

	@Override
	public <T> T call(Callable<T> callable) throws Exception {
		checkNotNull(callable);

		Callback callback = this.callback.get();
		if (callback != null) {
			return profile(callable, callback);
		} else {
			return callable.call();
		}
	}

	private <T> T profile(Callable<T> callable, Callback callback) throws Exception {
		Stopwatch stopWatch = Stopwatch.createStarted();
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
			TimeSpan timeSpan = new TimeSpan(stopWatch.elapsed(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS);
			ProfileResult timedResult = new ProfileResult(timeSpan, callable);

			if (logger.isTraceEnabled()) {
				logger.trace(timedResult);
			}

			issueCallback(timedResult, callback, caughtException);
		}
	}

	private void issueCallback(ProfileResult timedResult, Callback callback, @Nullable Exception caughtException) {
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
