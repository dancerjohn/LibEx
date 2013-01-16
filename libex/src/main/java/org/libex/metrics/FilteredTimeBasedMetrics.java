package org.libex.metrics;

import static com.google.common.base.Preconditions.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;

@ThreadSafe
@ParametersAreNonnullByDefault
public class FilteredTimeBasedMetrics extends ForwardingTimeBasedMetrics {

	private final Predicate<Long> filter;

	public FilteredTimeBasedMetrics(TimeBasedMetrics delegate, Predicate<Long> filter) {
		super(checkNotNull(delegate));

		this.filter = checkNotNull(filter);
	}

	@Override
	public long addValue(long value) {
		if (filter.apply(value)) {
			return super.addValue(value);
		} else {
			return super.getEventCount();
		}
	}

	@Override
	public MetricsSnapshot addValueAndGetSnapshot(long value) {
		if (filter.apply(value)) {
			return super.addValueAndGetSnapshot(value);
		} else {
			return super.getSnapshot();
		}
	}
}
