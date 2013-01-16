package org.libex.metrics;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpanCounter;
import org.libex.math.RunningAverage;

import com.google.common.base.Optional;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class SimpleTimeBasedMetrics implements TimeBasedMetrics {

	private long count = 0L;
	@Nullable
	private RunningAverage runningAverage;
	@Nullable
	private TimeSpanCounter counter;
	private final Object lock = new Object();

	public void setRunningAverage(RunningAverage runningAverage) {
		checkNotNull(runningAverage);
		checkState(count == 0, "Cannot add average after collection has begun");

		this.runningAverage = runningAverage;
	}

	@Override
	public Optional<RunningAverage> getOptionalRunningAverage() {
		return Optional.fromNullable(runningAverage);
	}

	public void setCounter(TimeSpanCounter counter) {
		checkNotNull(counter);
		checkState(count == 0, "Cannot add average after collection has begun");

		this.counter = counter;
	}

	@Override
	public Optional<TimeSpanCounter> getOptionalTimeSpanCounter() {
		return Optional.fromNullable(counter);
	}

	@Override
	public long addValue(long value) {
		synchronized (lock) {
			count++;

			if (runningAverage != null) {
				runningAverage.addValue(value);
			}

			if (counter != null) {
				counter.addEvent();
			}

			return count;
		}
	}

	@Override
	public MetricsSnapshot getSnapshot() {
		synchronized (lock) {
			return new MetricsSnapshot(count,
					runningAverage == null ? null : runningAverage.getSnapshot(),
					counter == null ? null : counter.getSnapshot());
		}
	}

	@Override
	public MetricsSnapshot addValueAndGetSnapshot(long value) {
		synchronized (lock) {
			addValue(value);
			return getSnapshot();
		}
	}

	@Override
	public long getEventCount() {
		return count;
	}
}
