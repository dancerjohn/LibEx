package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.validation.message.MessageCapturer;

import com.google.common.base.Function;

/**
 * A {@link MessageCapturer} that is capable of transforming a single input into the key and message.
 * 
 * @param <InputType>
 *            the type that is received and transformed to produce the key and message
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class TransformingMessageCapturer<InputType, KeyType, MessageType>
        extends DefaultMessageCapturer<KeyType, MessageType> {

    private final Function<? super InputType, ? extends KeyType> keyFunction;
    private final Function<? super InputType, ? extends MessageType> messageFunction;

    public TransformingMessageCapturer(
            final Function<? super InputType, ? extends KeyType> keyFunction,
            final Function<? super InputType, ? extends MessageType> messageFunction) {
        super();
        this.keyFunction = checkNotNull(keyFunction);
        this.messageFunction = checkNotNull(messageFunction);
    }

    public void receive(
            final InputType input)
    {
        receive(keyFunction.apply(input), messageFunction.apply(input));
    }
}
