package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang.Validate;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.number.OrderingComparison;
import org.joda.time.Duration;
import org.libex.collect.CollectionsEx;
import org.libex.primitives.tiny.integer.NaturalInteger;
import org.libex.test.validation.message.BaseMessageMatcher;
import org.libex.test.validation.message.IterableMessageMatcher;
import org.libex.test.validation.message.MessageValidator;
import org.libex.test.validation.message.MessageValidatorBuilder;
import org.libex.test.validation.message.SingleMessageMatcher;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class DefaultMessageValidatorBuilder<KeyType, MessageType> implements
        MessageValidatorBuilder<KeyType, MessageType> {

    public static <KeyType, MessageType> DefaultMessageValidatorBuilder<KeyType, MessageType> create()
    {
        return new DefaultMessageValidatorBuilder<>();
    }

    private static enum KeyState {
        SPECIFIED_KEYS,
        ALL_KEYS,
        EXPECTED_KEYS,
        ALL_UNEXPECTED_KEYS,
        UNEXPECTED_KEYS_IN
    }

    private class UnexpectedIn {
        private final ImmutableList<KeyType> keys;
        private final List<BaseMessageMatcher<? super MessageType, ?>> matcher = newArrayList();

        private UnexpectedIn(
                final ImmutableList<KeyType> keys) {
            super();
            this.keys = keys;
        }
    }

    // ///////////////////////////
    // State Fields
    @CheckForNull
    private KeyState keyState = null;

    @CheckForNull
    private ImmutableList<KeyType> currentKeys = null;

    private UnexpectedIn currentUnexpectedIn = null;

    // ///////////////////////////
    // Matchers

    private final Set<KeyType> expectedKeys = newLinkedHashSet();

    private final Multimap<KeyType, Matcher<Integer>> expectedCountMatcherMap = LinkedListMultimap
            .create();
    private final Multimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> matcherMap = LinkedListMultimap
            .create();
    private List<BaseMessageMatcher<? super MessageType, ?>> forExpectedKeysMatchers = newArrayList();

    private List<BaseMessageMatcher<? super MessageType, ?>> forAllKeysMatchers = newArrayList();
    private List<BaseMessageMatcher<? super MessageType, ?>> forUnexpectedKeysMatchers = newArrayList();

    private List<UnexpectedIn> unexpectedIn = newArrayList();

    // ///////////////////////////
    // Wait fields
    private final Multimap<KeyType, SingleMessageMatcher<? super List<? super MessageType>, ?>> waitMatcherMap = LinkedListMultimap
            .create();
    private final List<SingleMessageMatcher<? super List<? super MessageType>, ?>> forExpectedKeysDelayers = newArrayList();

    private Duration maxWait = null;
    private Duration postDelayWait = null;

    // Done
    // ///////////////////////////

    public DefaultMessageValidatorBuilder() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forKey(
            final KeyType key)
    {
        return forKeysIn(newArrayList(key));
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forKeysIn(
            @SuppressWarnings("unchecked") final KeyType... keys)
    {
        return forKeysIn(newArrayList(keys));
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forKeysIn(
            final Iterable<KeyType> keys)
    {
        Validate.notEmpty(newArrayList(keys));

        keyState = KeyState.SPECIFIED_KEYS;
        currentKeys = ImmutableList.copyOf(keys);
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forAllKeys()
    {
        keyState = KeyState.ALL_KEYS;
        currentKeys = null;
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forExpectedKeys()
    {
        keyState = KeyState.EXPECTED_KEYS;
        currentKeys = null;
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeys()
    {
        keyState = KeyState.ALL_UNEXPECTED_KEYS;
        currentKeys = null;
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeysIn(
            @SuppressWarnings("unchecked") final KeyType... keys)
    {
        return forUnexpectedKeysIn(newArrayList(keys));
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeysIn(
            final Iterable<KeyType> keys)
    {
        Validate.notEmpty(newArrayList(keys));

        keyState = KeyState.UNEXPECTED_KEYS_IN;
        currentKeys = ImmutableList.copyOf(keys);
        currentUnexpectedIn = null;
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> markAsExpected()
    {
        checkState(keyState == KeyState.SPECIFIED_KEYS,
                "As least one key must have been set before marking as expected");
        for (KeyType key : currentKeys) {
            expectedKeys.add(key);
        }
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> waitForMessageCount(
            final NaturalInteger count)
    {
        return waitForMessageCount(OrderingComparison.greaterThanOrEqualTo(count.get()));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public MessageValidatorBuilder<KeyType, MessageType> waitForMessageCount(
            final Matcher<Integer> matcher)
    {
        DefaultMessageMatcher.DefaultMessageMatcherBuilder<? super List<? super MessageType>, Integer> builder = DefaultMessageMatcher
                .builder();
        SingleMessageMatcher<? super List<? super MessageType>, ?> messageMatcher = builder
                .function((Function) CollectionsEx.toSize())
                .matcher(matcher)
                .build();
        return waitForMessages(messageMatcher);
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> waitForMessages(
            final SingleMessageMatcher<? super List<? super MessageType>, ?> matcher)
    {
        checkState(newArrayList(KeyState.SPECIFIED_KEYS, KeyState.EXPECTED_KEYS).contains(keyState),
                "Key state must be either specified via forKey(s) or forExpectedKeys()");

        if (keyState == KeyState.SPECIFIED_KEYS) {
            for (KeyType key : currentKeys) {
                waitMatcherMap.put(key, matcher);
            }
        } else {
            forExpectedKeysDelayers.add(matcher);
        }
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> expectMessageCount(
            final NaturalInteger count)
    {
        if (keyState == KeyState.SPECIFIED_KEYS &&
                count.get() > 0) {
            markAsExpected();
        }

        return validateMessageCount(is(count.get()));
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> validateMessageCount(
            final Matcher<Integer> matcher)
    {
        switch (keyState) {
        case SPECIFIED_KEYS:
            for (KeyType key : currentKeys) {
                expectedCountMatcherMap.put(key, matcher);
            }
            break;
        default:
            @SuppressWarnings({ "unchecked", "rawtypes" })
            DefaultIterableMessageMatcher<MessageType, ?> iterableMatcher = DefaultIterableMessageMatcher
                    .is((Matcher) IsCollectionWithSize.hasSize(matcher));

            addBaseMessageMatcher(iterableMatcher);
        }
        return this;
    }

    private void addBaseMessageMatcher(
            final BaseMessageMatcher<? super MessageType, ?> matcher)
    {
        switch (keyState) {
        case SPECIFIED_KEYS:
            for (KeyType key : currentKeys) {
                matcherMap.put(key, matcher);
            }
            break;
        case ALL_KEYS:
            forAllKeysMatchers.add(matcher);
            break;
        case EXPECTED_KEYS:
            forExpectedKeysMatchers.add(matcher);
            break;
        case ALL_UNEXPECTED_KEYS:
            forUnexpectedKeysMatchers.add(matcher);
            break;
        case UNEXPECTED_KEYS_IN:
            addUnexpectedKeyInMatcher(matcher);
            break;
        default:
            throw new IllegalStateException("Unsupported keyState: " + keyState);
        }
    }

    private void addUnexpectedKeyInMatcher(
            final BaseMessageMatcher<? super MessageType, ?> matcher)
    {
        if (currentUnexpectedIn == null) {
            currentUnexpectedIn = new UnexpectedIn(currentKeys);
            unexpectedIn.add(currentUnexpectedIn);
        }

        currentUnexpectedIn.matcher.add(matcher);
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> waitForAndExpectMessageCount(
            final NaturalInteger count)
    {
        waitForMessageCount(count);
        return expectMessageCount(count);
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> setMaxWaitTime(
            @Nullable final Duration maxWait)
    {
        this.maxWait = maxWait;
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> setPostDelayWaitTime(
            @Nullable final Duration postWaitDelay)
    {
        this.postDelayWait = postWaitDelay;
        return this;
    }

    @Override
    public MessageValidator<KeyType, MessageType> buildDelay()
    {
        checkState(maxWait != null, "maxWait must be specified");

        Multimap<KeyType, SingleMessageMatcher<? super List<? super MessageType>, ?>> waitMapCopy = LinkedListMultimap
                .create(waitMatcherMap);

        if (!forExpectedKeysDelayers.isEmpty()) {
            checkState(!expectedKeys.isEmpty(), "forExpectedKeys was used but no keys are marked as expected");

            for (KeyType expectedKey : expectedKeys) {
                for (SingleMessageMatcher<? super List<? super MessageType>, ?> matcher : forExpectedKeysDelayers) {
                    waitMapCopy.put(expectedKey, matcher);
                }
            }
        }

        MessageMatcherMessageReceiptDelay.MessageMatcherMessageReceiptDelayBuilder<KeyType, MessageType> builder =
                MessageMatcherMessageReceiptDelay.builder();
        builder.maxWaitTime(maxWait)
                .waitAfterMessagesReceived(postDelayWait)
                .waitMatcherMap(waitMapCopy);

        return builder.build();
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> validateAllMessages(
            final IterableMessageMatcher<? super MessageType, ?> matcher)
    {
        addBaseMessageMatcher(matcher);
        return this;
    }

    @Override
    public MessageValidatorBuilder<KeyType, MessageType> validateEachMessage(
            final SingleMessageMatcher<? super MessageType, ?> matcher)
    {
        addBaseMessageMatcher(matcher);
        return this;
    }

    @Override
    public MessageValidator<KeyType, MessageType> build()
    {
        DefaultMessageValidator.DefaultMessageValidatorBuilder<KeyType, MessageType> builder = DefaultMessageValidator
                .builder();

        if (!isEmpty(waitMatcherMap.values())
                || !isEmpty(forExpectedKeysDelayers)) {
            builder.delay(buildDelay());
        }

        builder
                .expectedKeys(expectedKeys)
                .expectedCountMatcherMap(expectedCountMatcherMap)
                .matcherMap(buildMatcherMap())
                .forAllKeysMatchers(forAllKeysMatchers)
                .forUnexpectedKeysMatchers(forUnexpectedKeysMatchers);

        return builder.build();
    }

    private Multimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> buildMatcherMap()
    {
        Multimap<KeyType, BaseMessageMatcher<? super MessageType, ?>> matcherMapCopy = LinkedListMultimap
                .create(matcherMap);

        if (!forExpectedKeysMatchers.isEmpty()) {
            checkState(!expectedKeys.isEmpty(), "forExpectedKeys was used but no keys are marked as expected");

            for (KeyType expectedKey : expectedKeys) {
                for (BaseMessageMatcher<? super MessageType, ?> matcher : forExpectedKeysMatchers) {
                    matcherMapCopy.put(expectedKey, matcher);
                }
            }
        }

        if (!unexpectedIn.isEmpty()) {
            for (UnexpectedIn unexpected : unexpectedIn) {
                for (KeyType unexpectedKey : unexpected.keys) {
                    if (!expectedKeys.contains(unexpectedKey)) {
                        for (BaseMessageMatcher<? super MessageType, ?> matcher : unexpected.matcher) {
                            matcherMapCopy.put(unexpectedKey, matcher);
                        }
                    }
                }
            }
        }
        return matcherMapCopy;
    }
}
