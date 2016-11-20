package org.libex.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.Duration;

import com.google.common.base.Objects;
import com.google.common.primitives.Longs;

/**
 * Specifies a period of time. The idea for this class was taken from the Guava
 * Use {@link org.joda.time.Duration}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
@Deprecated
public final class TimeSpan implements Comparable<TimeSpan> {

	public static TimeSpan oneNanosecond() {
		return new TimeSpan(TimeUnit.NANOSECONDS);
	}

	public static TimeSpan nanosecods(final long duration) {
		return new TimeSpan(duration, TimeUnit.NANOSECONDS);
	}

	public static TimeSpan oneMicrosecond() {
		return new TimeSpan(TimeUnit.MICROSECONDS);
	}

	public static TimeSpan microseconds(final long duration) {
		return new TimeSpan(duration, TimeUnit.MICROSECONDS);
	}

	public static TimeSpan oneMillisecond() {
		return new TimeSpan(TimeUnit.MILLISECONDS);
	}

	public static TimeSpan milliseconds(final long duration) {
		return new TimeSpan(duration, TimeUnit.MILLISECONDS);
	}

	public static TimeSpan oneSecond() {
		return new TimeSpan(TimeUnit.SECONDS);
	}

	public static TimeSpan seconds(final long duration) {
		return new TimeSpan(duration, TimeUnit.SECONDS);
	}

	public static TimeSpan oneMinute() {
		return new TimeSpan(TimeUnit.MINUTES);
	}

	public static TimeSpan minutes(final long duration) {
		return new TimeSpan(duration, TimeUnit.MINUTES);
	}

	public static TimeSpan oneDay() {
		return new TimeSpan(TimeUnit.DAYS);
	}

	public static TimeSpan days(final long duration) {
		return new TimeSpan(duration, TimeUnit.DAYS);
	}

	private final long duration;
	private final TimeUnit timeUnit;

	public TimeSpan(final TimeUnit timeUnit) {
		checkNotNull(timeUnit, "timeUnit may not be null");
		this.duration = 1;
		this.timeUnit = timeUnit;
	}

	public TimeSpan(final long duration, final TimeUnit timeUnit) {
		checkNotNull(timeUnit, "timeUnit may not be null");
		this.duration = duration;
		this.timeUnit = timeUnit;
	}
	public long getDuration() {
		return duration;
	}

    public Duration toDuration()
    {
        return new Duration(getDurationIn(TimeUnit.MILLISECONDS));
    }

	public long getDurationIn(final TimeUnit timeUnit) {
		return timeUnit.convert(duration, this.timeUnit);
	}

	public TimeSpan convertTo(final TimeUnit timeUnit) {
		return new TimeSpan(getDurationIn(timeUnit), timeUnit);
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public int compareTo(final TimeSpan other) {
		return Longs.compare(duration, other.getDurationIn(timeUnit));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getDurationIn(TimeUnit.NANOSECONDS));
	}

	@Override
	public boolean equals(final Object obj) {
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

	public String toString(final TimeUnit outputTimeUnit) {
		return TimeUnitsEx.toString(duration, outputTimeUnit);
	}
}
