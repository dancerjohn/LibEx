package org.libex.concurrent;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.math.RunningAverage;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

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
	private final Multiset<Long> countGroups;
	private final Object lock = new Object();

	private long eventsRecorded = 0L;
	private long totalOfValuesInQueue = 0L;
	private long numberToSkip;
	private long groupingRange = 0L;

	/**
	 * @param numberValuesToMaintain
	 *            the number of the most recent values to include in the running
	 *            average
	 */
	public ConcurrentRunningAverage(int numberValuesToMaintain) {
		checkArgument(numberValuesToMaintain > 0);

		this.queue = new ArrayBlockingQueue<Long>(numberValuesToMaintain);
		this.countGroups = HashMultiset.create(numberValuesToMaintain);
	}

	public void setNumberToSkip(long numberToSkip) {
		checkArgument(numberToSkip > 0L, "numberToSkip must be > 0");
		checkState(eventsRecorded == 0, "cannot set numberToSkip after metrics collection has begun");

		this.numberToSkip = numberToSkip;
	}

	public void setGroupingRange(long groupingRange) {
		checkArgument(groupingRange > 0L, "groupingRange must be > 0");
		checkState(eventsRecorded == 0, "cannot set groupingRange after metrics collection has begun");

		this.groupingRange = groupingRange;
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
				removeOneElement();
			}

			eventsRecorded++;
			if (eventsRecorded > numberToSkip) {
				addElement(value);
			}

			return eventsRecorded;
		}
	}

	private void removeOneElement() {
		Long removedValue = queue.poll();
		countGroups.remove(convertValueToGroup(removedValue));
		totalOfValuesInQueue -= removedValue;
	}

	private void addElement(long value) {
		totalOfValuesInQueue += value;
		queue.add(value);
		countGroups.add(convertValueToGroup(value));
	}

	private long convertValueToGroup(long value) {
		if (groupingRange > 0L) {
			return (value / groupingRange) * groupingRange;
		} else {
			return value;
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

	@Override
	public ImmutableSet<Entry<Long>> getEventCountSet() {
		synchronized (lock) {
			return ImmutableSet.copyOf(this.countGroups.entrySet());
		}
	}
}
