package org.libex.concurrent;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Builder;

import org.joda.time.Duration;

import com.google.common.base.Predicate;

/**
 * Allows for limiting the rate of events over a period of time. For example, it might limit the number
 * of events to 2 per 10 minutes.
 * 
 * In general, Guava's RateLimiter should probably be used in preference to this class. However, it appears
 * that Guava's RateLimiter does not allow for rates that are slower than 1 per second which was why this
 * class was created.
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Builder
public class RateLimiterEx {

    public static final <T> Predicate<T> createRateLimitingPredicate(
            final RateLimiterEx rateLimiter)
    {
        return new Predicate<T>() {

            @Override
            public boolean apply(
                    final T input)
            {
                return rateLimiter.tryAcquire();
            }
        };
    }

    private final DelayQueue<Delayed> delayQueue = new DelayQueue<>();
    private final Object lock = new Object();

    private final Duration timePeriod;
    private final int maxAllowedInTimePeriod;

    public boolean tryAcquire()
    {
        synchronized (lock) {
            clearExpired();
            if (spaceAvailableInQueue()) {
                delayQueue.add(Delayeds.createDelayUsingDateSupplier(timePeriod));
                return true;
            } else {
                return false;
            }
        }
    }

    private void clearExpired()
    {
        while (delayQueue.poll() != null) {
            ;
        }
    }

    private boolean spaceAvailableInQueue()
    {
        return delayQueue.size() < maxAllowedInTimePeriod;
    }
}
