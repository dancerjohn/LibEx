package org.libex.test.jms.validation.matcher;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.joda.time.Duration;
import org.libex.test.jms.validation.capture.JmsExchangeCapturer;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@NotThreadSafe
@Slf4j
public final class CoreJmsExchangeCapturerMatchers {

    public static <D, T> TypeSafeMatcher<JmsExchangeCapturer<D>> matches(
            final Function<JmsExchangeCapturer<D>, T> function,
            final Matcher<? super T> matcher)
    {
        return matches(function, matcher, null);
    }

    public static <D, T> TypeSafeMatcher<JmsExchangeCapturer<D>> matches(
            final Function<JmsExchangeCapturer<D>, ? extends T> function,
            final Matcher<? super T> matcher,
            @Nullable final Duration timeLimit)
    {
        checkNotNull(matcher);
        checkNotNull(function);

        return new TypeSafeMatcher<JmsExchangeCapturer<D>>() {

            @Override
            public void describeTo(
                    final Description description)
            {
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(
                    final JmsExchangeCapturer<D> item)
            {
                if (timeLimit == null) {
                    return matchesNoWait(item);
                } else {
                    return matchesWithWait(item);
                }
            }

            private boolean matchesNoWait(
                    final JmsExchangeCapturer<D> item)
            {
                T output = function.apply(item);
                boolean result = matcher.matches(output);
                return result;
            }

            private boolean matchesWithWait(
                    final JmsExchangeCapturer<D> item)
            {
                Stopwatch stopwatch = Stopwatch.createStarted();
                boolean result = matchesNoWait(item);

                boolean logged = false;
                while (!result &&
                        stopwatch.elapsed(TimeUnit.MILLISECONDS) < timeLimit.getMillis()) {
                    if (!logged) {
                        logged = true;
                        log.info("\n--- Waiting for messages sent to hub");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw Throwables.propagate(e);
                    }

                    result = matchesNoWait(item);
                }
                return result;
            }
        };
    }

    private CoreJmsExchangeCapturerMatchers() {
    }
}
