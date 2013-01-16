package org.libex.metrics;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpanCounter;
import org.libex.concurrent.TimeSpanCounter.TimeSpanCount;
import org.libex.math.RunningAverage;
import org.libex.math.RunningAverage.RunningAverageSnapshot;

import com.google.common.base.Optional;

@ThreadSafe
@ParametersAreNonnullByDefault
public interface TimeBasedMetrics {

	@Immutable
	@ThreadSafe
	public static class MetricsSnapshot {
		private final long eventCount;
		private final Optional<RunningAverage.RunningAverageSnapshot> runningAverageSnapshot;
		private final Optional<TimeSpanCount> timeBasedCount;

		public MetricsSnapshot(long eventCount,
				@Nullable RunningAverageSnapshot runningAverageSnapshot,
				@Nullable TimeSpanCount timeBasedCount) {
			super();
			this.eventCount = eventCount;
			this.runningAverageSnapshot = Optional.fromNullable(runningAverageSnapshot);
			this.timeBasedCount = Optional.fromNullable(timeBasedCount);
		}

		public long getEventCount() {
			return eventCount;
		}

		public Optional<RunningAverage.RunningAverageSnapshot> getRunningAverageSnapshot() {
			return runningAverageSnapshot;
		}

		public Optional<TimeSpanCount> getTimeBasedCount() {
			return timeBasedCount;
		}
	}

	Optional<RunningAverage> getOptionalRunningAverage();

	Optional<TimeSpanCounter> getOptionalTimeSpanCounter();

	long getEventCount();

	long addValue(long value);

	MetricsSnapshot getSnapshot();

	MetricsSnapshot addValueAndGetSnapshot(long value);
}
