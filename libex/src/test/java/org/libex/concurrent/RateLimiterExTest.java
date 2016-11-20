package org.libex.concurrent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.libex.test.TestBaseLocal;

public class RateLimiterExTest extends TestBaseLocal {

    RateLimiterEx rateLimiter;

    @Before
    public void setup()
    {
        rateLimiter = RateLimiterEx.builder()
                .maxAllowedInTimePeriod(2)
                .timePeriod(Duration.standardSeconds(3))
                .build();
    }

    @Test
    public void testTryAcquire()
    {
        assertThat(rateLimiter.tryAcquire(), is(true));
    }

    @Test
    public void testTryAcquire_thirdTime() throws InterruptedException
    {
        assertThat(rateLimiter.tryAcquire(), is(true));
        Thread.sleep(2000);
        assertThat(rateLimiter.tryAcquire(), is(true));
        assertThat(rateLimiter.tryAcquire(), is(false));

        Thread.sleep(1100);
        assertThat(rateLimiter.tryAcquire(), is(true));
        assertThat(rateLimiter.tryAcquire(), is(false));

        Thread.sleep(2000);
        assertThat(rateLimiter.tryAcquire(), is(true));
    }
}
