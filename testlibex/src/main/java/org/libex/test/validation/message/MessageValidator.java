package org.libex.test.validation.message;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.rules.ErrorCollector;
import org.libex.test.rules.NullableWrappingErrorCollector;

/**
 * Validates received messages
 * 
 * @param <KeyType>
 *            type of the key for messages
 * @param <MessageType>
 *            type of the messages
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface MessageValidator<KeyType, MessageType> {

    /**
     * Executes the configured validation against the captured messages
     * 
     * @param capturer
     *            message capturer
     * @throws InterruptedException
     *             if wait for messages was interrupted
     */
    void validate(
            final MessageCapturer<KeyType, MessageType> capturer) throws InterruptedException;

    /**
     * Executes the configured validation against the captured messages
     * 
     * @param capturer
     *            message capturer
     * @param errorCollector
     *            the error collector to use if any
     * @throws InterruptedException
     *             if wait for messages was interrupted
     */
    void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            @Nullable final ErrorCollector errorCollector) throws InterruptedException;

    /**
     * Executes the configured validation against the captured messages
     * 
     * @param capturer
     *            message capturer
     * @param errorCollector
     *            the error collector to use
     * @throws InterruptedException
     *             if wait for messages was interrupted
     */
    void validate(
            final MessageCapturer<KeyType, MessageType> capturer,
            final NullableWrappingErrorCollector errorCollector) throws InterruptedException;
}
