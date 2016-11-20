package org.libex.concurrent;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.Duration;
import org.libex.primitives.DatesEx;

import com.google.common.base.Supplier;
import com.google.common.primitives.Longs;

/**
 * Utility methods for {@link Delayed} instances.
 *
 * @author John Butler
 *
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class Delayeds {

	    /**
     * Creates a {@link Delayed} that will delay for the passed {@link TimeSpan}. This {@link Delayed} will use the
     * {@link DateSupplier} to get the current date. This
     * allows for control of time during testing.
     *
     * @param timeSpan
     *            the amount of time to delay
     * @return a new {@link Delayed} that will delay for the passed {@link TimeSpan}
     */
	public static Delayed createDelayUsingDateSupplier(final TimeSpan timeSpan) {
        return new DefaultDelay(Duration.millis(timeSpan.getDurationIn(TimeUnit.MILLISECONDS)),
                DateSupplier.getInstance());
	}

    /**
     * Creates a {@link Delayed} that will delay for the passed {@link Duration} This {@link Delayed} will use the
     * {@link DateSupplier} to get the current date. This
     * allows for control of time during testing.
     *
     * @param duration
     *            the amount of time to delay
     * @return a new {@link Delayed} that will delay for the passed {@link Duration}
     */
    public static Delayed createDelayUsingDateSupplier(final Duration duration) {
        return new DefaultDelay(duration, DateSupplier.getInstance());
    }

    /**
     * Creates a {@link Delayed} that will delay for the passed {@link TimeSpan}. This {@link Delayed} will use the
     * System to get the current date.
     *
     * @param timeSpan
     *            the amount of time to delay
     * @return a new {@link Delayed} that will delay for the passed {@link TimeSpan}
     */
    public static Delayed createDelayUsingSystemTime(
            final TimeSpan timeSpan)
    {
        return new DefaultDelay(Duration.millis(timeSpan.getDurationIn(TimeUnit.MILLISECONDS)),
                DatesEx.getSystemDateSupplier());
    }

    /**
     * Creates a {@link Delayed} that will delay for the passed {@link Duration} This {@link Delayed} will use the
     * System to get the current date.
     *
     * @param duration
     *            the amount of time to delay
     * @return a new {@link Delayed} that will delay for the passed {@link Duration}
     */
    public static Delayed createDelayUsingSystemTime(
            final Duration duration)
    {
        return new DefaultDelay(duration, DatesEx.getSystemDateSupplier());
    }

	private static class DefaultDelay implements Delayed {
		private final long endTime;
        private final Supplier<Date> currentDateSupplier;

        private DefaultDelay(
                final Duration duration,
                final Supplier<Date> currentDateSupplier) {
            this.currentDateSupplier = currentDateSupplier;
            this.endTime = currentDateSupplier.get().getTime() + duration.getMillis();
		}

		@Override
		public int compareTo(final Delayed o) {
			return Longs.compare(this.getDelay(TimeUnit.MILLISECONDS),
					o.getDelay(TimeUnit.MILLISECONDS));
		}

		@Override
		public long getDelay(final TimeUnit timeUnit) {
            long currentTime = currentDateSupplier.get().getTime();
			long remainder = endTime - currentTime;
			if (remainder <= 0) {
				return 0;
			} else {
				long result = timeUnit.convert(remainder, TimeUnit.MILLISECONDS);
                return result;
			}
		}

	}

	private Delayeds() {
	}
}
