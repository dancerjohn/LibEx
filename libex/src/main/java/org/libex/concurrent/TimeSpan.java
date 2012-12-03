package org.libex.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Objects;
import com.google.common.primitives.Longs;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
public final class TimeSpan implements Comparable<TimeSpan> {
	private final long duration;
	private final TimeUnit timeUnit;

	public TimeSpan(long duration, TimeUnit timeUnit) {
		checkNotNull(timeUnit, "timeUnit may not be null");
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	public long getDuration() {
		return duration;
	}

	public long getDurationIn(TimeUnit timeUnit) {
		return timeUnit.convert(duration, this.timeUnit);
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public int compareTo(TimeSpan other) {
		return Longs.compare(duration, other.getDurationIn(timeUnit));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getDurationIn(TimeUnit.NANOSECONDS));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TimeSpan other = (TimeSpan) obj;
		return duration == other.getDurationIn(timeUnit);
	}

	@Override
	public String toString() {
		return TimeUnitsEx.toString(duration, timeUnit);
	}
}
