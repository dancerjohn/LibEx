package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matcher;
import org.junit.rules.ErrorCollector;
import org.libex.test.rules.NullableWrappingErrorCollector;
import org.libex.test.validation.message.BaseMessageMatcher;
import org.libex.test.validation.message.IterableMessageMatcher;
import org.libex.test.validation.message.MessageCapturer;
import org.libex.test.validation.message.MessageValidator;
import org.libex.test.validation.message.SingleMessageMatcher;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

@ParametersAreNonnullByDefault
@ThreadSafe
@Slf4j
class DefaultMessageValidator<KeyType, MessageType>
        implements MessageValidator<KeyType, MessageType> {

    @Nullable
    private final MessageValidator<KeyType, MessageType> delay;

    private final ImmutableMultimap<KeyType, Matcher<Integer>> expectedCountMatcherMap;
    private final ImmutableMultimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> matcherMap;
    private final ImmutableList<BaseMessageMatcher<? super MessageType, ?>> forAllKeysMatchers;

    private final ImmutableList<KeyType> expectedKeys;
    private final ImmutableList<BaseMessageMatcher<? super MessageType, ?>> forUnexpectedKeysMatchers;

    @Builder
    private DefaultMessageValidator(
            @Nullable final MessageValidator<KeyType, MessageType> delay,
            final Iterable<KeyType> expectedKeys,
            final Multimap<KeyType, Matcher<Integer>> expectedCountMatcherMap,
            final Multimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> matcherMap,
            final Iterable<BaseMessageMatcher<? super MessageType, ?>> forAllKeysMatchers,
            final Iterable<BaseMessageMatcher<? super MessageType, ?>> forUnexpectedKeysMatchers) {
        super();

        this.delay = delay;

        this.expectedKeys = ImmutableList.copyOf(expectedKeys);
        this.expectedCountMatcherMap = ImmutableMultimap.copyOf(expectedCountMatcherMap);
        this.matcherMap = ImmutableMultimap.copyOf(matcherMap);
        this.forAllKeysMatchers = ImmutableList.copyOf(forAllKeysMatchers);

        this.forUnexpectedKeysMatchers = ImmutableList.copyOf(forUnexpectedKeysMatchers);
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer)
    {
        new MessageValidatorRunner(capturer, (ErrorCollector) null)
                .validate();
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            @Nullable final ErrorCollector errorCollector)
    {
        new MessageValidatorRunner(capturer, errorCollector)
                .validate();
    }

    @Override
    public void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            final NullableWrappingErrorCollector errorCollector)
    {
        new MessageValidatorRunner(capturer, errorCollector)
                .validate();
    }

    private class MessageValidatorRunner {
        private final MessageCapturer<KeyType, MessageType> capturer;

        private final NullableWrappingErrorCollector errorCollector;
        private final ImmutableSet<KeyType> keysWithMatchers;
        private final List<KeyType> validatedKeys = newArrayList();

        private MessageValidatorRunner(
                final MessageCapturer<KeyType, MessageType> capturer,
                @Nullable final ErrorCollector errorCollector) {
            this(capturer, new NullableWrappingErrorCollector(errorCollector));
        }

        private MessageValidatorRunner(
                final MessageCapturer<KeyType, MessageType> capturer,
                final NullableWrappingErrorCollector errorCollector) {
            this.capturer = checkNotNull(capturer);
            this.errorCollector = checkNotNull(errorCollector);

            keysWithMatchers = ImmutableSet.<KeyType> builder()
                    .addAll(expectedCountMatcherMap.keys())
                    .addAll(matcherMap.keys())
                    .build();
        }

        private void validate()
        {
            doDelay();
            validateKeysWithMatchers();
            validateRemainingReceivedKeys();
        }

        private void doDelay()
        {
            if (delay != null) {
                try {
                    delay.validate(capturer, errorCollector);
                } catch (InterruptedException e) {
                    Throwables.propagate(e);
                }
            }
        }

        private void validateKeysWithMatchers()
        {
            for (KeyType key : keysWithMatchers) {
                log.info("Checking key {}", key);

                doValidationFor(key);
            }
        }

        private void validateExpectedCount(
                final KeyType key,
                @Nullable final ImmutableList<MessageType> messages)
        {
            String message = String.format("Expected count for key = %s", key);
            int count = (messages == null) ? 0 : messages.size();
            for (Matcher<Integer> matcher : expectedCountMatcherMap.get(key)) {
                errorCollector.checkThat(message, count, matcher, false);
            }
        }

        private void validateRemainingReceivedKeys()
        {
            for (KeyType key : capturer.getKeys()) {
                if (!validatedKeys.contains(key)) {
                    log.info("Checking received key {}", key);

                    doValidationFor(key);
                }
            }
        }

        private void doValidationFor(
                final KeyType key)
        {
            @Nullable
            ImmutableList<MessageType> messages = capturer.getMessages(key);

            // in the case of a remaining recieved key, the below two will not have any matchers
            validateExpectedCount(key, messages);
            validateUsingEachOf(key, messages, matcherMap);

            validateUsingEachOf(key, messages, forAllKeysMatchers);

            if (!expectedKeys.contains(key)) {
                validateUsingEachOf(key, messages, forUnexpectedKeysMatchers);
            }

            validatedKeys.add(key);
        }

        private void validateUsingEachOf(
                final KeyType key,
                @Nullable final ImmutableList<MessageType> messages,
                final Multimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> matchers)
        {
            for (BaseMessageMatcher<? super MessageType, ?> matcher : matchers.get(key)) {
                validateUsingMatcher(key, messages, matcher);
            }
        }

        private void validateUsingEachOf(
                final KeyType key,
                @Nullable final ImmutableList<MessageType> messages,
                final List<BaseMessageMatcher<? super MessageType, ?>> matchers)
        {

            for (BaseMessageMatcher<? super MessageType, ?> matcher : matchers) {
                validateUsingMatcher(key, messages, matcher);
            }
        }

        private void validateUsingMatcher(
                final KeyType key,
                @Nullable final ImmutableList<MessageType> messages,
                final BaseMessageMatcher<? super MessageType, ?> matcher)
        {
            String message = String.format("For key = %s: %s", key, matcher.getMessage());
            if (matcher instanceof IterableMessageMatcher) {
                validateUsingIterableMessageMatcher(message, messages,
                        (IterableMessageMatcher<? super MessageType, ?>) matcher);
            } else {
                validateUsingSingleMessageMatcher(message, messages,
                        (SingleMessageMatcher<? super MessageType, ?>) matcher);
            }
        }

        private <T> void validateUsingSingleMessageMatcher(
                final String message,
                @Nullable final ImmutableList<MessageType> messages,
                final SingleMessageMatcher<? super MessageType, T> matcher)
        {
            for (MessageType preTransformed : messages) {
                if (matcher.getPredicate().apply(preTransformed)) {
                    T transformed = matcher.getFunction().apply(preTransformed);
                    errorCollector.checkThat(message, transformed, matcher.getMatcher(), matcher.failFast());
                }
            }
        }

        private <T> void validateUsingIterableMessageMatcher(
                final String message,
                @Nullable final ImmutableList<MessageType> messages,
                final IterableMessageMatcher<? super MessageType, T> matcher)
        {
            List<T> transformed = transform(messages, matcher.getFunction());
            errorCollector.checkThat(message, transformed, matcher.getMatcher(), matcher.failFast());
        }
    }
}
