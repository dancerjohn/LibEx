package org.libex.concurrent;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Maintains the count of event during the most recent time period.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class TimedCounter {

	private final TimeSpan timeSpan;
	private final DelayQueue<Delayed> delayQueue = new DelayQueue<Delayed>();
	private final Object lock = new Object();

	/**
	 * @param timeSpan
	 *            the amount of time over which to count
	 */
	public TimedCounter(TimeSpan timeSpan) {
		this.timeSpan = timeSpan;
	}

	/**
	 * Adds an event to the count
	 */
	public void addEvent() {
		synchronized (lock) {
			clearExpired();
			delayQueue.add(Delayeds.createDelayUsingDateSupplier(timeSpan));
		}
	}

	/**
	 * @return the number of event that have occurred in the most recent time
	 *         span
	 */
	public int getNumberEventsInTimeSpan() {
		synchronized (lock) {
			clearExpired();
			return delayQueue.size();
		}
	}

	private void clearExpired() {
		while (delayQueue.poll() != null) {
			; // NO OP
		}
	}

	/**
	 * @return a snapshot of the count at the current moment
	 */
	public Snapshot getSnapshot() {
		synchronized (lock) {
			return new Snapshot(timeSpan, getNumberEventsInTimeSpan());
		}
	}

	/**
	 * Adds an event and gets the number of event that have occurred in the most
	 * recent time span
	 * 
	 * @return the number of event that have occurred in the most recent time
	 *         span
	 */
	public int addEventAndGetNumberEventsInTimeSpan() {
		synchronized (lock) {
			addEvent();
			return getNumberEventsInTimeSpan();
		}
	}

	/**
	 * Adds an event and gets a snapshot of the count at the current moment
	 * 
	 * @return a snapshot of the count at the current moment
	 */
	public Snapshot addEventAndGetSnapshot() {
		synchronized (lock) {
			addEvent();
			return getSnapshot();
		}
	}

	/**
	 * Sets the count to 0.
	 */
	public void reset() {
		synchronized (lock) {
			delayQueue.clear();
		}
	}

	public static class Snapshot {
		private final TimeSpan timeSpan;
		private final Date recordingTime;
		private final int numberOfEventsInTimeSpan;

		private Snapshot(TimeSpan timeSpan, int numberOfEventsInTimeSpan) {
			this.recordingTime = new Date();
			this.timeSpan = timeSpan;
			this.numberOfEventsInTimeSpan = numberOfEventsInTimeSpan;
		}

		/**
		 * @return the amount of time over which the events were collected
		 */
		public TimeSpan getTimeSpan() {
			return timeSpan;
		}

		/**
		 * @return time at which the snapshot was taken
		 */
		public Date getRecordingTime() {
			return recordingTime;
		}

		/**
		 * @return the number of event that have occurred in the time span
		 */
		public int getNumberOfEventsInTimeSpan() {
			return numberOfEventsInTimeSpan;
		}

		@Override
		public String toString() {
			return String.format("%d events in the last %s",
					numberOfEventsInTimeSpan, timeSpan);
		}
	}
}
