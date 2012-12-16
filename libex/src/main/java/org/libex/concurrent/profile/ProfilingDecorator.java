package org.libex.concurrent.profile;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.profile.Profiler.TimedResult;
import org.libex.concurrent.profile.Profiling.Callback;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
abstract class ProfilingDecorator implements Callback, Profiling {

	protected static class Config {
		private Profiling delegate;

		public void setDelegate(Profiling delegate) {
			this.delegate = delegate;
		}
	}

	private final Profiling delegate;
	private final AtomicReference<Callback> callback = new AtomicReference<Profiler.Callback>();

	ProfilingDecorator(Config config) {
		this.delegate = checkNotNull(config.delegate, "delegate");
		this.delegate.setObserver(this);
	}

	@Override
	public void processProfileEvent(TimedResult result) {
		Callback callback = this.callback.get();
		if (callback != null) {
			callback.processProfileEvent(result);
		}
	}

	@Override
	public <T> T call(Callable<T> callable) throws Exception {
		return delegate.call(callable);
	}

	@Override
	public void setObserver(Callback callback) {
		checkNotNull(callback);
		checkState(this.callback.get() == null, "Callback already set");

		this.callback.set(callback);
	}

	protected Profiling getDelegate() {
		return delegate;
	}

	protected AtomicReference<Callback> getCallback() {
		return callback;
	}
}
