package org.libex.test.validation.message.impl;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Builder;

import org.hamcrest.Matcher;
import org.joda.time.Duration;
import org.junit.rules.ErrorCollector;
import org.libex.test.rules.NullableWrappingErrorCollector;
import org.libex.test.validation.message.MessageCapturer;
import org.libex.test.validation.message.MessageValidator;
import org.libex.test.validation.message.SingleMessageMatcher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

@ParametersAreNonnullByDefault
@ThreadSafe
class MessageMatcherMessageReceiptDelay<KeyType, MessageType>
        implements MessageValidator<KeyType, MessageType> {

    private final Duration maxWaitTime;
    private final ImmutableMultimap<KeyType, SingleMessageMatcher<? super List<? super MessageType>, ?>> waitMatcherMap;

    @Nullable
    private final Duration waitAfterMessagesReceived;

    @Builder
    private MessageMatcherMessageReceiptDelay(
            final Duration maxWaitTime,
            final Multimap<KeyType, SingleMessageMatcher<? super List<? super MessageType>, ?>> waitMatcherMap,
            @Nullable final Duration waitAfterMessagesReceived) {
        super();
        this.maxWaitTime = maxWaitTime;
        this.waitMatcherMap = ImmutableMultimap.copyOf(waitMatcherMap);
        this.waitAfterMessagesReceived = waitAfterMessagesReceived;
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer) throws InterruptedException
    {
        new MessageValidatorRunner(capturer, (ErrorCollector) null)
                .validate();
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            @Nullable final ErrorCollector errorCollector) throws InterruptedException
    {
        new MessageValidatorRunner(capturer, errorCollector)
                .validate();
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            final NullableWrappingErrorCollector errorCollector)
            throws InterruptedException
    {
        new MessageValidatorRunner(capturer, errorCollector)
                .validate();
    }

    private class MessageValidatorRunner
            extends
            AbstractMessageValidatorRunner<KeyType, MessageType, SingleMessageMatcher<? super List<? super MessageType>, ?>> {

        private MessageValidatorRunner(
                final MessageCapturer<KeyType, MessageType> capturer,
                @Nullable final ErrorCollector errorCollector) {
            this(capturer, new NullableWrappingErrorCollector(errorCollector));
        }

        private MessageValidatorRunner(
                final MessageCapturer<KeyType, MessageType> capturer,
                final NullableWrappingErrorCollector errorCollector) {
            super(maxWaitTime, waitMatcherMap, waitAfterMessagesReceived, capturer, errorCollector);
        }

        @Override
        <T> boolean matches(
                final ImmutableList<MessageType> messages,
                final SingleMessageMatcher<? super List<? super MessageType>, ?> matcher)
        {
            @SuppressWarnings("unchecked")
            T transformed = (T) matcher.getFunction().apply(messages);
            return matcher.getMatcher().matches(transformed);
        }

        @SuppressWarnings("unchecked")
        @Override
        <T> void assertThat(
                final KeyType key,
                final ImmutableList<MessageType> messages,
                final SingleMessageMatcher<? super List<? super MessageType>, ?> matcher)
        {
            String message = String.format("For key = %s: %s", key, matcher.getMessage());
            T transformed = (T) matcher.getFunction().apply(messages);
            getErrorCollector().checkThat(message, transformed, (Matcher<T>) matcher.getMatcher());
        }
    }
}
