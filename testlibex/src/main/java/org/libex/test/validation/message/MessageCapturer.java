package org.libex.test.validation.message;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

/**
 * Receives and holds message for validation
 * 
 * @param <KeyType>
 *            the key by which messages are grouped
 * @param <MessageType>
 *            the message type
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface MessageCapturer<KeyType, MessageType> {

    /**
     * Stores the passed message
     * 
     * @param key
     *            key under which to store the message
     * @param message
     *            the message to store
     */
    void receive(
            final KeyType key,
            final MessageType message);

    /**
     * @return the list of received keys
     */
    @Nonnull
    Iterable<KeyType> getKeys();

    /**
     * @return the list of received messages
     */
    @Nonnull
    ImmutableList<MessageType> getAllMessages();

    /**
     * Gets the list of messages that were received with the passed key
     * 
     * @param key
     *            the key for which to retrieve the list of retrieve messages
     * @return the list of messages received with the passed key
     */
    @Nonnull
    ImmutableList<MessageType> getMessages(
            final KeyType key);

    /**
     * Gets all received messages
     * 
     * @return the full map of recevied messages
     */
    @Nonnull
    ImmutableMultimap<KeyType, MessageType> getMessageMap();

    /**
     * Clears the list of received messages and keys
     */
    void reset();
}
