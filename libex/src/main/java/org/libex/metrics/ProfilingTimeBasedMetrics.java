package org.libex.metrics;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.profile.Profiling;
import org.libex.concurrent.profile.Profiling.ProfileResult;

@ThreadSafe
@ParametersAreNonnullByDefault
public class ProfilingTimeBasedMetrics extends ForwardingTimeBasedMetrics implements Profiling.Callback {

	private final TimeUnit timeUnit;

	public ProfilingTimeBasedMetrics(TimeBasedMetrics delegate, TimeUnit timeUnit) {
		super(checkNotNull(delegate));

		this.timeUnit = checkNotNull(timeUnit);
	}

	@Override
	public void processProfileEvent(ProfileResult result) {
		processProfileEventAndGetEventCount(result);
	}

	public long processProfileEventAndGetEventCount(ProfileResult result) {
		return this.addValue(result.getTimeSpan().getDurationIn(timeUnit));
	}
}
