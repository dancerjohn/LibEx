package org.libex.test.validation.message.impl;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.Duration;
import org.libex.test.rules.NullableWrappingErrorCollector;
import org.libex.test.validation.message.MessageCapturer;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

@ParametersAreNonnullByDefault
@ThreadSafe
@Slf4j
@Getter
abstract class AbstractMessageValidatorRunner<KeyType, MessageType, MatcherType> {

    private final Duration maxWaitTime;
    private final ImmutableMultimap<KeyType, MatcherType> waitMatcherMap;

    @Nullable
    private final Duration waitAfterMessagesReceived;

    private final MessageCapturer<KeyType, MessageType> capturer;

    @Nullable
    private final NullableWrappingErrorCollector errorCollector;

    AbstractMessageValidatorRunner(
            final Duration maxWaitTime,
            final ImmutableMultimap<KeyType, MatcherType> waitMatcherMap,
            final Duration waitAfterMessagesReceived,
            final MessageCapturer<KeyType, MessageType> capturer,
            final NullableWrappingErrorCollector errorCollector) {
        super();
        this.maxWaitTime = maxWaitTime;
        this.waitMatcherMap = waitMatcherMap;
        this.waitAfterMessagesReceived = waitAfterMessagesReceived;
        this.capturer = capturer;
        this.errorCollector = errorCollector;
    }

    void validate() throws InterruptedException
    {
        boolean allMessagesReceived = waitForAndGetIfAllMessageReceived();
        if (!allMessagesReceived) {
            validateAllMatchers();
        }

        if (waitAfterMessagesReceived != null) {
            log.info("Doing post-delay wait of {}", waitAfterMessagesReceived);
            Thread.sleep(waitAfterMessagesReceived.getMillis());
        }
    }

    private boolean waitForAndGetIfAllMessageReceived() throws InterruptedException
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        boolean allMessagesReceived = false;
        do {
            allMessagesReceived = areAllMessagesReceived();
            waitIfNeeded(allMessagesReceived, stopwatch);
        } while (shouldWaitLonger(allMessagesReceived, stopwatch));

        return allMessagesReceived;
    }

    private boolean areAllMessagesReceived()
    {
        boolean allMessagesReceived = true;
        keyLoop: for (Entry<KeyType, Collection<MatcherType>> entry : waitMatcherMap
                .asMap().entrySet()) {
            ImmutableList<MessageType> messages = capturer.getMessages(entry.getKey());
            for (MatcherType matcher : entry.getValue()) {
                if (!matches(messages, matcher)) {
                    allMessagesReceived = false;
                    break keyLoop;
                }
            }
        }
        return allMessagesReceived;
    }

    abstract <T> boolean matches(
            final ImmutableList<MessageType> messages,
            final MatcherType matcher);

    private boolean waitLogged = false;

    private void waitIfNeeded(
            final boolean allMessagesReceived,
            final Stopwatch stopwatch) throws InterruptedException
    {
        if (shouldWaitLonger(allMessagesReceived, stopwatch)) {
            if (!waitLogged) {
                waitLogged = true;
                log.info("Waiting for expected messages to be received");
            }
            Thread.sleep(1000L);
        }
    }

    private boolean shouldWaitLonger(
            final boolean allMessagesReceived,
            final Stopwatch stopwatch)
    {
        return !allMessagesReceived
                && stopwatch.elapsed(TimeUnit.MILLISECONDS) <= maxWaitTime.getMillis();
    }

    private void validateAllMatchers()
    {
        for (Entry<KeyType, Collection<MatcherType>> entry : waitMatcherMap
                .asMap().entrySet()) {
            KeyType key = entry.getKey();
            ImmutableList<MessageType> messages = capturer.getMessages(key);
            for (MatcherType matcher : entry.getValue()) {
                assertThat(key, messages, matcher);
            }
        }
    }

    abstract <T> void assertThat(
            final KeyType key,
            final ImmutableList<MessageType> messages,
            final MatcherType matcher);
}
