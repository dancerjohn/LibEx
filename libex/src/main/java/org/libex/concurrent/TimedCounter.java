package org.libex.concurrent;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;


/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class TimedCounter {

	private final TimeSpan timeSpan;
	private final DelayQueue<Delayed> delayQueue = new DelayQueue<Delayed>();
	private final Object lock = new Object();

	public TimedCounter(TimeSpan timeSpan) {
		this.timeSpan = timeSpan;
	}

	public void addEvent() {
		synchronized (lock) {
			clearExpired();
			delayQueue.add(Delayeds.createDelay(timeSpan));
		}
	}

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

	public Snapshot getSnapshot() {
		synchronized (lock) {
			return new Snapshot(timeSpan, getNumberEventsInTimeSpan());
		}
	}

	public int addEventAndGetNumberEventsInTimeSpan() {
		synchronized (lock) {
			addEvent();
			return getNumberEventsInTimeSpan();
		}
	}

	public Snapshot addEventAndGetSnapshot() {
		synchronized (lock) {
			addEvent();
			return getSnapshot();
		}
	}

	public void reset() {
		synchronized (lock) {
			delayQueue.clear();
		}
	}

	public static class Snapshot {
		private final TimeSpan timeSpan;
		private final Date recordingTime;
		private final int numberOfEventsInTimeSpan;

		public Snapshot(TimeSpan timeSpan, int numberOfEventsInTimeSpan) {
			this.recordingTime = new Date();
			this.timeSpan = timeSpan;
			this.numberOfEventsInTimeSpan = numberOfEventsInTimeSpan;
		}

		public TimeSpan getTimeSpan() {
			return timeSpan;
		}

		public Date getRecordingTime() {
			return recordingTime;
		}

		public int getNumberOfEventsInTimeSpan() {
			return numberOfEventsInTimeSpan;
		}

		@Override
		public String toString() {
			return String.format("%d events in the last %s", numberOfEventsInTimeSpan, timeSpan);
		}
	}
}
