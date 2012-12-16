package org.libex.concurrent.profile;

import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.Caller;
import org.libex.concurrent.profile.Profiler.TimedResult;

@ThreadSafe
@ParametersAreNonnullByDefault
public interface Profiling extends Caller {

	public static interface Callback {
		void processProfileEvent(TimedResult result);
	}

	@Override
	<T> T call(Callable<T> callable) throws Exception;

	void setObserver(Callback callback);
}
