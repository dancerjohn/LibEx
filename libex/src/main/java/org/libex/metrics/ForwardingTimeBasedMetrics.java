package org.libex.metrics;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpanCounter;
import org.libex.math.RunningAverage;

import com.google.common.base.Optional;

@ThreadSafe
@ParametersAreNonnullByDefault
public class ForwardingTimeBasedMetrics implements TimeBasedMetrics {

	@Nullable
	private final TimeBasedMetrics delegate;

	protected ForwardingTimeBasedMetrics() {
		this(null);
	}

	protected ForwardingTimeBasedMetrics(@Nullable TimeBasedMetrics delegate) {
		super();
		this.delegate = delegate;
	}

	protected TimeBasedMetrics delegate() {
		checkState(delegate != null);

		return delegate;
	}

	@Override
	public Optional<RunningAverage> getOptionalRunningAverage() {
		return delegate().getOptionalRunningAverage();
	}

	@Override
	public Optional<TimeSpanCounter> getOptionalTimeSpanCounter() {
		return delegate().getOptionalTimeSpanCounter();
	}

	@Override
	public long addValue(long value) {
		return delegate().addValue(value);
	}

	@Override
	public MetricsSnapshot getSnapshot() {
		return delegate().getSnapshot();
	}

	@Override
	public MetricsSnapshot addValueAndGetSnapshot(long value) {
		return delegate().addValueAndGetSnapshot(value);
	}

	@Override
	public long getEventCount() {
		return delegate().getEventCount();
	}

}
