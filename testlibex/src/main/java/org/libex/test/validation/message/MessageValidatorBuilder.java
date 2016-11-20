package org.libex.test.validation.message;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Matcher;
import org.joda.time.Duration;
import org.libex.primitives.tiny.integer.NaturalInteger;

/**
 * Builds a MessageValidator, specifying all validation
 * 
 * @param <KeyType>
 *            type of the key for messages
 * @param <MessageType>
 *            type of the messages
 * 
 *            Consider the following:
 *
 *            <pre>
 * {@code
 *     DefaultMessageValidatorBuilder&lt;TrafficthiefTopic, Message&gt; builder = DefaultMessageValidatorBuilder
 *             .create();
 *     builder
 * 
 *             // All Enriched
 *             .forKeysIn(TrafficthiefTopic.ENRICHED,
 *                     TrafficthiefTopic.ENRICHED_NETDEF,
 *                     TrafficthiefTopic.ENRICHED_OBFUSCATED,
 *                     TrafficthiefTopic.ENRICHED_RESTRICTED)
 *             .validateEachMessage(containsHeader(IslandTransportHeader.TIPPING_SOURCE))
 *             .validateEachMessage(
 *                     // does not contain TOS header
 *                     hasHeaderMatching(
 *                     Matchers.not(IsMapContaining.&lt;String&gt; hasKey(
 *                             TrafficthiefHeader.TARGETING_ORACLE_MARKER.getKey()))))
 * 
 *             // TOS
 *             .forKey(TrafficthiefTopic.TOS)
 *             .validateEachMessage(
 *                     hasHeaderMatching(
 *                     Matchers.hasEntry(
 *                             TrafficthiefHeader.TARGETING_ORACLE_MARKER.getKey(),
 *                             (Object) PublishRouteBuilder.TARGETING_ORACLE_HEADER_VALUE)))
 * 
 *             // ALL
 *             .forExpectedKeys()
 *             .validateEachMessage(containsHeader(IslandTransportHeader.EWM_ORIG_DEST))
 *             .validateEachMessage(containsHeader(TrafficthiefHeader.LOCAL_HOST_NAME))
 * 
 *             // The Rest
 *             .forUnexpectedKeysIn(TrafficthiefTopic.TrafficthiefTopicRetriever.KNOWN_TOPICS)
 *             .expectMessageCount(NaturalInteger.of(0))
 * 
 *             // Wait
 *             .setMaxWaitTime(Duration.standardSeconds(10))
 *             .setPostDelayWaitTime(Duration.standardSeconds(3));
 * }
 * </pre>
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface MessageValidatorBuilder<KeyType, MessageType> {

    // ////////////////////////////////////////////////////
    // For Key methods

    /**
     * Specifies the key for which the following validation applied (until another key
     * is specified).
     * 
     * @param key
     *            the key against which to match
     * 
     * @return this instance
     * 
     * @throws IllegalArgumentException
     *             if key is null
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forKey(
            final KeyType key);

    /**
     * Specifies the key(s) for which the following validation applied (until another key
     * is specified).
     * 
     * @param keys
     *            the key(s) against which to match
     * 
     * @return this instance
     * 
     * @throws IllegalArgumentException
     *             if keys is null or empty
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forKeysIn(
            @SuppressWarnings("unchecked") final KeyType... keys);

    /**
     * Specifies the key(s) for which the following validation applied (until another key
     * is specified).
     * 
     * @param keys
     *            the key(s) against which to match
     * 
     * @return this instance
     * 
     * @throws IllegalArgumentException
     *             if keys is null or empty
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forKeysIn(
            final Iterable<KeyType> keys);

    /**
     * Specifies that the following validation applies to all received keys.
     * 
     * @return this instance
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forAllKeys();

    /**
     * Deprecated because cannot find a good use instead of {@link #forKeysIn(Iterable)}
     * 
     * Specifies that the following validation applies to all keys that have been marked as expected.
     * 
     * @return this instance
     * 
     * @see #markAsExpected()
     * @see #expectMessageCount(NaturalInteger)
     * @see #waitForAndExpectMessageCount(NaturalInteger)
     */
    @Nonnull
    @Deprecated
    MessageValidatorBuilder<KeyType, MessageType> forExpectedKeys();

    /**
     * Specifies that the following validation applies to any key that is received and has not been marked as expected.
     * 
     * @return this instance
     * 
     * @see #markAsExpected()
     * @see #expectMessageCount(NaturalInteger)
     * @see #waitForAndExpectMessageCount(NaturalInteger)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeys();

    /**
     * Specifies that the following validation applies to any key in {@code keys} and has not been marked as expected
     * elsewhere.
     * 
     * @param keys
     *            the key(s) against which to match
     * 
     * @return this instance
     * @throws IllegalArgumentException
     *             if keys is null or empty
     * 
     * @see #markAsExpected()
     * @see #expectMessageCount(NaturalInteger)
     * @see #waitForAndExpectMessageCount(NaturalInteger)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeysIn(
            @SuppressWarnings("unchecked") final KeyType... keys);

    /**
     * Specifies that the following validation applies to any key in {@code keys} and has not been marked as expected
     * elsewhere.
     * 
     * @param keys
     *            the key(s) against which to match
     * 
     * @return this instance
     * @throws IllegalArgumentException
     *             if keys is null or empty
     * 
     * @see #markAsExpected()
     * @see #expectMessageCount(NaturalInteger)
     * @see #waitForAndExpectMessageCount(NaturalInteger)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> forUnexpectedKeysIn(
            final Iterable<KeyType> keys);

    // ////////////////////////////////////////////////////
    // expect methods

    /**
     * Marks the configured key(s) as expected.
     * 
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if forKey or forKeysIn has not been called or is not
     *             the most resent "for" call
     * 
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> markAsExpected();

    /**
     * For the configured key(s), creates an expectation that exactly {@code count} messages will be received. If
     * {@code count} greater than 0, marks the key(s) as expected.
     * 
     * @param count
     *            the expected count
     * @return this instance
     * 
     * @see #markAsExpected()
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> expectMessageCount(
            final NaturalInteger count);

    // ////////////////////////////////////////////////////
    // delay methods

    /**
     * For the configured key(s), causes the system to wait for the provided message count to be received.
     * This will only take effect if {@link #setMaxWaitTime(Duration)} has been called.
     * 
     * @param count
     *            the number of messages for which to wait
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if forKey, forKeysIn or forExpectedKeys has not been called or is not
     *             the most resent "for" call
     * 
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     * @see #forExpectedKeys()
     * @see #setMaxWaitTime(Duration)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> waitForMessageCount(
            final NaturalInteger count);

    /**
     * For the configured key(s), causes the system to wait for the message count to match the provided {@link Matcher}.
     * This will only take effect if {@link #setMaxWaitTime(Duration)} has been called.
     * 
     * @param matcher
     *            matcher to used against the received message count
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if forKey, forKeysIn or forExpectedKeys has not been called or is not
     *             the most resent "for" call
     * 
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     * @see #forExpectedKeys()
     * @see #setMaxWaitTime(Duration)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> waitForMessageCount(
            final Matcher<Integer> matcher);

    /**
     * For the configured key(s), causes the system to wait for the message count to match the provided
     * {@link SingleMessageMatcher}.
     * This will only take effect if {@link #setMaxWaitTime(Duration)} has been called.
     * 
     * @param matcher
     *            matcher to used against the received message count
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if forKey, forKeysIn or forExpectedKeys has not been called or is not
     *             the most resent "for" call
     * 
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     * @see #forExpectedKeys()
     * @see #setMaxWaitTime(Duration)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> waitForMessages(
            final SingleMessageMatcher<? super List<? super MessageType>, ?> matcher);

    /**
     * @param count
     *            the number of messages for which to wait
     * @return this instance
     * 
     * @throws IllegalStateException
     *             if forKey, forKeysIn or forExpectedKeys has not been called or is not
     *             the most resent "for" call
     * 
     * @see #waitForMessageCount(NaturalInteger)
     * @see #expectMessageCount(NaturalInteger)
     * 
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> waitForAndExpectMessageCount(
            final NaturalInteger count);

    /**
     * Sets the maximum amount to time to wait for the expected messages to be received.
     * 
     * @param maxWait
     *            maximum time to wait
     * @return this instance
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> setMaxWaitTime(
            @Nullable final Duration maxWait);

    /**
     * Sets the amount of time to wait after all expected messages have been received.
     * 
     * @param postWaitDelay
     *            time to wait
     * @return this instance
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> setPostDelayWaitTime(
            @Nullable final Duration postWaitDelay);

    /**
     * @return a Validator that will wait for the expected messages to be received
     */
    @Nonnull
    MessageValidator<KeyType, MessageType> buildDelay();

    // ////////////////////////////////////////////////////
    // validate methods

    /**
     * For the configured key(s), creates an uses the provided {@link Matcher} to validate the
     * received message count. NOTE: this method does NOT mark the key as expected.
     * 
     * @param matcher
     *            the matcher to use against the receive message count
     * @return this instance
     * 
     * @see #forKey(Object)
     * @see #forKeysIn(Iterable)
     * @see #forKeysIn(Object...)
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> validateMessageCount(
            final Matcher<Integer> matcher);

    /**
     * For the configured key(s), creates a validation where all messages received on each configured key
     * is passed to the matcher.
     * 
     * @param matcher
     *            matcher to use against all of the messages received on each key
     * @return this instance
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> validateAllMessages(
            final IterableMessageMatcher<? super MessageType, ?> matcher);

    /**
     * For the configured key(s), creates a validation where each message received on each configured key
     * is passed to the matcher.
     * 
     * @param matcher
     *            matcher to use against each of the messages received on each key
     * @return this instance
     */
    @Nonnull
    MessageValidatorBuilder<KeyType, MessageType> validateEachMessage(
            final SingleMessageMatcher<? super MessageType, ?> matcher);

    // ////////////////////////////////////////////////////
    // build methods

    /**
     * Builds the validator.
     * 
     * @return the configured validator
     */
    @Nonnull
    MessageValidator<KeyType, MessageType> build();
}
