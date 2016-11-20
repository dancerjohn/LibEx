package org.libex.test.validation.message.camel;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.libex.test.validation.message.impl.TransformingMessageCapturer;

import com.google.common.base.Function;
import com.google.common.base.Functions;

/**
 * {@link TransformingMessageCapturer} that handles Camel {@link Message} instances.
 * 
 * @param <KeyType>
 *            the type of the key to be captured
 * @param <MessageType>
 *            the type of the messages to be captured
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public class CamelTransformingMessageCapturer<KeyType, MessageType>
        extends TransformingMessageCapturer<Message, KeyType, MessageType> {

    /**
     * @return a capturer that uses the input_topic_header as the key and the {@link Message} as the captured message
     */
    public static CamelTransformingMessageCapturer<String, Message> createDefault()
    {
        return new CamelTransformingMessageCapturer<>(
                CamelMessageCaptureRouteBuilder.TO_INPUT_TOPIC_HEADER,
                Functions.<Message> identity());
    }

    /**
     * Creates a Capturer that transforms the received {@link Message} to get the key but uses the {@link Message} as
     * the message
     * 
     * @param keyFunction
     *            {@link Function} to use to transform the {@link Message} to the key
     * @return key transforming capturer
     * @param <KeyType>
     *            the type of the key to be captured
     */
    public static <KeyType> CamelTransformingMessageCapturer<KeyType, Message> transformerKey(
            final Function<? super Message, ? extends KeyType> keyFunction)
    {
        return new CamelTransformingMessageCapturer<>(
                keyFunction,
                Functions.<Message> identity());
    }

    /**
     * Creates a Capturer that transforms the received {@link Message} to get the key and message
     * 
     * @param keyFunction
     *            {@link Function} to use to transform the {@link Message} to the key
     * @param messageFunction
     *            {@link Function} to use to transform the {@link Message} to the message
     */
    public CamelTransformingMessageCapturer(
            final Function<? super Message, ? extends KeyType> keyFunction,
            final Function<? super Message, ? extends MessageType> messageFunction) {
        super(keyFunction, messageFunction);
    }

    /**
     * Receives the message from Camel
     * 
     * @param body
     *            message body
     * @param headers
     *            message headers
     */
    public void receiveMessage(
            @Body final Object body,
            @Headers final Map<String, Object> headers)
    {
        DefaultMessage message = new DefaultMessage();
        message.setBody(body);
        message.setHeaders(headers);

        receive(message);
    }
}
