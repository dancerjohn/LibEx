package org.libex.test.jms.validation.matcher;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Exchange;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.Every;
import org.libex.collect.IterablesEx;
import org.libex.test.jms.validation.capture.JmsExchangeCapturer;
import org.libex.test.jms.validation.capture.JmsExchangeCapturers;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class JmsMessageMatchers {

    private JmsMessageMatchers() {
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesReceivedMatch(
            final Matcher<Exchange> matcher)
    {
        Function<JmsExchangeCapturer<T>, Collection<Exchange>> toMessages =
                JmsExchangeCapturers.toExchanges();
        return every(toMessages, matcher);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesReceivedOnMatch(
            final T destination,
            final Matcher<Exchange> matcher)
    {
        Function<JmsExchangeCapturer<T>, ImmutableList<Exchange>> toTopicMessages =
                JmsExchangeCapturers.toDestination(destination);
        return every(toTopicMessages, matcher);
    }

    public static <T, U> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesReceivedMatch(
            final Function<Exchange, U> toMatchedType,
            final Matcher<U> matcher)
    {
        Function<Iterable<Exchange>, Iterable<U>> collectionFunction = IterablesEx
                .asCollectionFunction(toMatchedType);
        Function<JmsExchangeCapturer<T>, Iterable<U>> toConvertedMessages =
                Functions.compose(collectionFunction, JmsExchangeCapturers.<T> toExchanges());
        return every(toConvertedMessages, matcher);
    }

    public static <T, U> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesReceivedOnMatch(
            final T destination,
            final Function<Exchange, U> toMatchedType,
            final Matcher<U> matcher)
    {
        Function<Iterable<Exchange>, Iterable<U>> collectionFunction = IterablesEx
                .asCollectionFunction(toMatchedType);
        Function<JmsExchangeCapturer<T>, ImmutableList<Exchange>> toTopicMessages =
                JmsExchangeCapturers.toDestination(destination);
        Function<JmsExchangeCapturer<T>, Iterable<U>> toConvertedMessages =
                Functions.compose(collectionFunction, toTopicMessages);
        return every(toConvertedMessages, matcher);
    }
    
    private static <T, U> TypeSafeMatcher<JmsExchangeCapturer<T>> every(
            final Function<JmsExchangeCapturer<T>, ? extends Iterable<U>> toExchanges,
            final Matcher<U> matcher)
    {
        Matcher<Iterable<U>> everyExchangeMatches = Every.everyItem(matcher);

        return CoreJmsExchangeCapturerMatchers.matches(
                toExchanges,
                everyExchangeMatches,
                null);
    }
}
