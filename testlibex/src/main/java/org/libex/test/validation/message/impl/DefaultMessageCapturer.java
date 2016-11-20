package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

import org.libex.test.validation.message.MessageCapturer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * The Default {@link MessageCapturer}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Slf4j
public class DefaultMessageCapturer<KeyType, MessageType>
        implements MessageCapturer<KeyType, MessageType> {

    private final Multimap<KeyType, MessageType> receivedMessages = ArrayListMultimap.create();

    public DefaultMessageCapturer() {
    }

    @Override
    public void receive(
            final KeyType key,
            final MessageType message)
    {
        checkNotNull(key);
        checkNotNull(message);

        log.debug("Captured message sent to {}", key);
        receivedMessages.put(key, message);
    }

    @Override
    public Iterable<KeyType> getKeys()
    {
        return receivedMessages.keys();
    }

    @Override
    public ImmutableList<MessageType> getAllMessages()
    {
        return ImmutableList.copyOf(receivedMessages.values());
    }

    @Override
    public ImmutableList<MessageType> getMessages(
            final KeyType key)
    {
        return ImmutableList.copyOf(receivedMessages.get(key));
    }

    @Override
    public ImmutableMultimap<KeyType, MessageType> getMessageMap()
    {
        return ImmutableMultimap.copyOf(receivedMessages);
    }

    @Override
    public void reset()
    {
        receivedMessages.clear();
    }

}
