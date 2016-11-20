package org.libex.test.validation.message.camel;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

/**
 * Route builder that can be use to capture messages (for example from JMS).
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
public class CamelMessageCaptureRouteBuilder extends RouteBuilder {

    public static final String INPUT_TOPIC_HEADER = "receivedFromTopic";
    public static final Function<Message, String> TO_INPUT_TOPIC_HEADER = new Function<Message, String>() {

        @Override
        public String apply(
                final Message input)
        {
            return (String) input.getHeader(INPUT_TOPIC_HEADER);
        }
    };

    private final String capturer;
    private final List<String> inputs;

    /**
     * @param capturer
     *            endpoint to invoke to pass received message
     * @param inputs
     *            list of enpoints from which to listen for messages
     */
    public CamelMessageCaptureRouteBuilder(
            final String capturer,
            final List<String> inputs) {

        this.capturer = capturer;
        this.inputs = ImmutableList.copyOf(inputs);
    }

    @Override
    public void configure() throws Exception
    {
        for (String input : inputs) {
            log.debug("Reading messages from {}", input);
            from(input)
                    .log(LoggingLevel.TRACE,
                            CamelMessageCaptureRouteBuilder.class.getName(),
                            "\n<---- Received message from " + input)
                    .setHeader(INPUT_TOPIC_HEADER).constant(input)
                    .to(capturer);
        }
    }
}
