package org.libex.concurrent;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.math.RunningAverage;

/**
 * Maintains a running average in a thread-safe manner.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class ConcurrentRunningAverage implements RunningAverage {

	private final ArrayBlockingQueue<Long> queue;
	private final long numberToSkip;
	private final Object lock = new Object();

	private long eventsRecorded = 0L;
	private long totalOfValuesInQueue = 0L;

	/**
	 * @param numberValuesToMaintain
	 *            the number of the most recent values to include in the running
	 *            average
	 */
	public ConcurrentRunningAverage(int numberValuesToMaintain) {
		this(numberValuesToMaintain, 0L);
	}

	/**
	 * @param numberValuesToMaintain
	 *            the number of the most recent values to include in the running
	 *            average
	 * @param numberToSkip
	 *            the number of initial values to ignore, use this if initial
	 *            values include setup and therefore skew the average
	 */
	public ConcurrentRunningAverage(int numberValuesToMaintain,
			long numberToSkip) {
		checkArgument(numberValuesToMaintain > 0);
		checkArgument(numberToSkip >= 0);

		this.queue = new ArrayBlockingQueue<Long>(numberValuesToMaintain);
		this.numberToSkip = 0L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#addValue(long)
	 */
	@Override
	public long addValue(long value) {
		synchronized (lock) {
			if (queue.remainingCapacity() == 0) {
				totalOfValuesInQueue -= queue.poll();
			}

			eventsRecorded++;
			if (eventsRecorded > numberToSkip) {
				totalOfValuesInQueue = +value;
				queue.add(value);
			}

			return eventsRecorded;
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
			return eventsRecorded;
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
			return queue.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.libex.math.RunningAverage#getRunningAverage()
	 */
	@Override
	public long getRunningAverage() {
		long numberInQueue;
		long totalOfValues;

		synchronized (lock) {
			numberInQueue = queue.size();
			totalOfValues = totalOfValuesInQueue;
		}

		if (numberInQueue == 0L) {
			return 0L;
		} else {
			return totalOfValues / numberInQueue;
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
			return new RunningAverageSnapshot(getNumberEventsInAverage(),
					getNumberEventsRecorded(), getRunningAverage());
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
			addValue(value);
			return getSnapshot();
		}
	}
}
