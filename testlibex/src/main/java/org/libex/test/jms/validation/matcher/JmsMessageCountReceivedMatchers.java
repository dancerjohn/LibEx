package org.libex.test.jms.validation.matcher;

import static org.hamcrest.CoreMatchers.allOf;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Exchange;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsMapContaining;
import org.joda.time.Duration;
import org.libex.test.jms.validation.capture.JmsExchangeCapturer;
import org.libex.test.jms.validation.capture.JmsExchangeCapturers;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class JmsMessageCountReceivedMatchers {

    private JmsMessageCountReceivedMatchers() {
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> messageCountReceivedOn(
            final T destination,
            final int expectedCount)
    {
        return messageCountReceivedOn(destination, expectedCount, null);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> messageCountReceivedOn(
            final T destination,
            final int expectedCount,
            @Nullable final Duration timeLimit)
    {
        Function<JmsExchangeCapturer<T>, ImmutableList<Exchange>> toTopicMessages =
                JmsExchangeCapturers.toDestination(destination);

        Matcher<Collection<? extends Exchange>> hasSize =
                IsCollectionWithSize.<Exchange> hasSize(expectedCount);

        return CoreJmsExchangeCapturerMatchers.matches(
                toTopicMessages,
                hasSize,
                timeLimit);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> messageCountReceived(
            final Map<T, Integer> countMap)
    {
        return messageCountReceived(countMap, null);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> messageCountReceived(
            final Map<T, Integer> countMap,
            @Nullable final Duration timeLimit)
    {
        
        Function<JmsExchangeCapturer<T>, Map<T, Integer>> toCount = JmsExchangeCapturers.toCountMap();

        @SuppressWarnings("unchecked")
        Matcher<Map<? extends T, ? extends Integer>>[] matchers = new Matcher[countMap.size()];
        int i = 0;
        for (Entry<T, Integer> count : countMap.entrySet()) {
            matchers[i++] = IsMapContaining.hasEntry(count.getKey(), count.getValue());
        }

        Matcher<Map<T, Integer>> matcher = allOf(matchers);

        return CoreJmsExchangeCapturerMatchers.matches(
                toCount,
                matcher,
                timeLimit);
    }
}
