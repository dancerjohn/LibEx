package org.libex.concurrent;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.math.BasicRunningAverage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset.Entry;

/**
 * Maintains a running average in a thread-safe manner.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class ConcurrentRunningAverage extends BasicRunningAverage {

	private final Object lock = new Object();

	/**
	 * @param numberValuesToMaintain
	 *            the number of the most recent values to include in the running
	 *            average
	 */
	public ConcurrentRunningAverage(int numberValuesToMaintain) {
		super(numberValuesToMaintain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#addValue(long)
	 */
	@Override
	public long addValue(long value) {
		synchronized (lock) {
			return super.addValue(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#getNumberEventsRecorded()
	 */
	@Override
	public long getNumberEventsRecorded() {
		synchronized (lock) {
			return super.getNumberEventsRecorded();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#getNumberEventsInAverage()
	 */
	@Override
	public long getNumberEventsInAverage() {
		synchronized (lock) {
			return super.getNumberEventsInAverage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#getRunningAverage()
	 */
	@Override
	public long getRunningAverage() {
		synchronized (lock) {
			return super.getRunningAverage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#getSnapshot()
	 */
	@Override
	public RunningAverageSnapshot getSnapshot() {
		synchronized (lock) {
			return super.getSnapshot();
		}
	}

	/**
	 * Adds the passed value to the average and returns a snapshot of the
	 * current average including the new value.
	 * 
	 * @param value
	 *            the value to add
	 * @return a snapshot of the average including the new value
	 */
	public RunningAverageSnapshot addValueAndGetSnapshot(long value) {
		synchronized (lock) {
			super.addValue(value);
			return super.getSnapshot();
		}
	}

	@Override
	public ImmutableSet<Entry<Long>> getEventCountSet() {
		synchronized (lock) {
			return super.getEventCountSet();
		}
	}
}
