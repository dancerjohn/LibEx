package org.libex.test.jms.validation.capture;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@Slf4j
public class JmsCaptureRouteBuilder extends RouteBuilder {

    public static final String INPUT_TOPIC_HEADER = "receivedFromTopic";

    private final JmsExchangeCapturer<?> capturer;

    public JmsCaptureRouteBuilder(
            final JmsExchangeCapturer<?> capturer) {
        super();
        this.capturer = capturer;
    }

    @Override
    public void configure() throws Exception
    {
        for (String topic : capturer.getDestinations()) {
            log.debug("Reading JMS message from {}", topic);
            from(topic)
                    .log(LoggingLevel.TRACE,
                            "org.libex.test.jms.validation.capture.JmsCaptureRouteBuilder",
                            "\n<---- Received message from " + topic)
                    .setHeader(INPUT_TOPIC_HEADER).constant(topic)
                    .bean(capturer);
        }
    }
}
