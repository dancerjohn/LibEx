package org.libex.concurrent.profile;

import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.Caller;
import org.libex.concurrent.TimeSpan;

/**
 * API for profiling a {@link Callable}
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public interface Profiling extends Caller {

	public static class ProfileResult {
		private final TimeSpan timeSpan;
		private final Callable<?> callable;

		public ProfileResult(TimeSpan timeSpan, Callable<?> callable) {
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

	public static interface Callback {
		void processProfileEvent(ProfileResult result);
	}

	@Override
	<T> T call(Callable<T> callable) throws Exception;

	/**
	 * Sets an observer of the profiling result
	 * 
	 * @param callback
	 *            the observer
	 */
	void setObserver(Callback callback);
}
