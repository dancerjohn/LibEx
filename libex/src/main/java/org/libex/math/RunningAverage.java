package org.libex.math;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author John Butler
 * 
 * @see org.libex.concurrent.ConcurrentRunningAverage
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public interface RunningAverage {

	long addValue(long value);

	long getNumberEventsRecorded();

	long getNumberEventsInAverage();

	long getRunningAverage();

	RunningAverageSnapshot getSnapshot();

	public static class RunningAverageSnapshot {
		private final long numberEventsInAverage;
		private final long numberEventsRecorded;
		private final long runningAverage;

		public RunningAverageSnapshot(long numberEventsInAverage, long numberEventsRecorded, long runningAverage) {
			super();
			this.numberEventsInAverage = numberEventsInAverage;
			this.numberEventsRecorded = numberEventsRecorded;
			this.runningAverage = runningAverage;
		}

		public long getNumberEventsInAverage() {
			return numberEventsInAverage;
		}

		public long getNumberEventsRecorded() {
			return numberEventsRecorded;
		}

		public long getRunningAverage() {
			return runningAverage;
		}
	}
}
