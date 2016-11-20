package org.libex.concurrent;

import static com.google.common.base.Preconditions.*;
import static org.libex.concurrent.Delayeds.*;

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
public class TimeSpanCounter {

	private final TimeSpan timeSpan;
	private final DelayQueue<Delayed> delayQueue = new DelayQueue<Delayed>();
	private final Object lock = new Object();

	public TimeSpanCounter(TimeSpan timeSpan) {
		checkNotNull(timeSpan);

		this.timeSpan = timeSpan;
	}

	public void addEvent() {
		synchronized (lock) {
			removeExpiredEvents();

			delayQueue.add(createDelayUsingDateSupplier(timeSpan));
		}
	}

	public int getNumberOfEventsInWindow() {
		synchronized (lock) {
			removeExpiredEvents();

			return delayQueue.size();
		}
	}

	private void removeExpiredEvents() {
		while (delayQueue.poll() != null) {
			; // NO OP, just draining queue of all expired elements
		}
	}

	public TimeSpanCount getSnapshot() {
		return new TimeSpanCount(timeSpan, getNumberOfEventsInWindow());
	}

	public TimeSpanCount addEventAndGetSnapshot() {
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

	public static class TimeSpanCount {
		private final TimeSpan timeSpan;
		private final int numberOfEventsInWindow;

		private TimeSpanCount(TimeSpan timeSpan, int numberOfEventsInWindow) {
			super();
			this.timeSpan = timeSpan;
			this.numberOfEventsInWindow = numberOfEventsInWindow;
		}

		public TimeSpan getTimeSpan() {
			return timeSpan;
		}

		public int getNumberOfEventsInWindow() {
			return numberOfEventsInWindow;
		}

		@Override
		public String toString() {
			return String.format("%d events observed in the last %s", numberOfEventsInWindow, timeSpan);
		}
	}

}
