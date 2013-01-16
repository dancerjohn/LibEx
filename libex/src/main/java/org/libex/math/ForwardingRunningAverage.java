package org.libex.math;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset.Entry;

@NotThreadSafe
@ParametersAreNonnullByDefault
public class ForwardingRunningAverage implements RunningAverage {

	@Nullable
	private final RunningAverage delegate;

	protected ForwardingRunningAverage() {
		this(null);
	}

	protected ForwardingRunningAverage(@Nullable RunningAverage delegate) {
		super();
		this.delegate = delegate;
	}

	protected RunningAverage delegate() {
		checkState(delegate != null);

		return delegate;
	}

	@Override
	public long addValue(long value) {
		return delegate().addValue(value);
	}

	@Override
	public long getNumberEventsRecorded() {
		return delegate().getNumberEventsRecorded();
	}

	@Override
	public long getNumberEventsInAverage() {
		return delegate().getNumberEventsInAverage();
	}

	@Override
	public long getRunningAverage() {
		return delegate().getRunningAverage();
	}

	@Override
	public RunningAverageSnapshot getSnapshot() {
		return delegate().getSnapshot();
	}

	@Override
	public ImmutableSet<Entry<Long>> getEventCountSet() {
		return delegate().getEventCountSet();
	}

}
