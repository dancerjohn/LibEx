package org.libex.math;

import static com.google.common.base.Preconditions.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;

@ThreadSafe
@ParametersAreNonnullByDefault
public class FilteredRunningAverage extends ForwardingRunningAverage {

	private final Predicate<Long> filter;

	public FilteredRunningAverage(RunningAverage delegate, Predicate<Long> filter) {
		super(checkNotNull(delegate));

		this.filter = checkNotNull(filter);
	}

	@Override
	public long addValue(long value) {
		if (filter.apply(value)) {
			return super.addValue(value);
		} else {
			return super.getNumberEventsRecorded();
		}
	}
}
