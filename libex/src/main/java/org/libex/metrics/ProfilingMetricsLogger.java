package org.libex.metrics;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.libex.concurrent.profile.Profiling;
import org.libex.concurrent.profile.Profiling.ProfileResult;
import org.libex.math.RunningAverage;
import org.libex.metrics.TimeBasedMetrics.MetricsSnapshot;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Longs;

@ThreadSafe
@ParametersAreNonnullByDefault
public class ProfilingMetricsLogger implements Profiling.Callback {

	private static final Logger LOG = Logger.getLogger(ProfilingMetricsLogger.class);

	private final ProfilingTimeBasedMetrics metrics;
	private final Optional<RunningAverage> average;
	private final long logMetricsInterval;
	private long logRunningAverageCountsInterval = 0L;
	private Level loggingLevel = Level.DEBUG;

	public ProfilingMetricsLogger(ProfilingTimeBasedMetrics metrics, long logMetricsInterval) {
		checkArgument(logMetricsInterval > 0, "logMetricsInterval must be > 0");

		this.metrics = checkNotNull(metrics);
		this.average = metrics.getOptionalRunningAverage();
		this.logMetricsInterval = logMetricsInterval;
	}

	public void setLogRunningAverageCountsInterval(long logRunningAverageCountsInterval) {
		checkArgument(logRunningAverageCountsInterval > 0, "logRunningAverageCountsInterval must be > 0");

		this.logRunningAverageCountsInterval = logRunningAverageCountsInterval;
	}

	public void setLoggingLevel(Level loggingLevel) {
		this.loggingLevel = checkNotNull(loggingLevel);
	}

	@Override
	public void processProfileEvent(ProfileResult result) {
		long count = metrics.processProfileEventAndGetEventCount(result);

		if (count > 0 && LOG.isEnabledFor(loggingLevel)) {
			logMetrics(count);
			logMetricsCounts(count);
		}
	}

	private void logMetrics(long count) {
		if (count % logMetricsInterval == 0) {
			MetricsSnapshot snapshot = metrics.getSnapshot();
			if (snapshot.getRunningAverageSnapshot().isPresent()) {
				LOG.log(loggingLevel, "Running Average = " + snapshot.getRunningAverageSnapshot().get());
			}

			if (snapshot.getTimeBasedCount().isPresent()) {
				LOG.log(loggingLevel, snapshot.getTimeBasedCount().get());
			}
		}
	}

	private void logMetricsCounts(long count) {
		if (shoudLogRunningAverageCounts(count)) {
			List<Entry<Long>> entryList = getSortedEntryCounts();

			LOG.log(loggingLevel, "Running Average Counts: ");
			for (int i = 0, n = entryList.size(); i < n; i++) {
				Entry<Long> current = entryList.get(i);
				if (i + 1 < n) {
					Entry<Long> next = entryList.get(i + 1);
					LOG.log(loggingLevel,
							String.format("%d events between %d and %d", current.getCount(), current.getElement(),
									next.getElement()));
				} else {
					LOG.log(loggingLevel, String.format("%d greater than %d", current.getCount(), current.getElement()));
				}
			}
		}
	}

	private boolean shoudLogRunningAverageCounts(long count) {
		return average.isPresent() &&
				logRunningAverageCountsInterval > 0 && count % logRunningAverageCountsInterval == 0;
	}

	private List<Entry<Long>> getSortedEntryCounts() {
		ImmutableSet<Entry<Long>> entrySet = average.get().getEventCountSet();
		List<Entry<Long>> entryList = newArrayList(entrySet);
		Collections.sort(entryList, entryComparator);
		return entryList;
	}

	private static final Comparator<Entry<Long>> entryComparator = new Comparator<Entry<Long>>() {

		@Override
		public int compare(Entry<Long> arg0, Entry<Long> arg1) {
			return Longs.compare(arg0.getElement(), arg1.getElement());
		}
	};
}
