package org.libex.test.jms.validation.matcher;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Exchange;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.libex.camel.IsExchange;
import org.libex.camel.IsMessage;
import org.libex.test.jms.validation.capture.JmsExchangeCapturer;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class JmsMessageBodyMatchers {

    private JmsMessageBodyMatchers() {
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allReceivedBodiesMatch(
            final Matcher<String> matcher)
    {
        Matcher<Exchange> exchangeMatcher = IsExchange.withInMessage(
                IsMessage.withBody(matcher, String.class));
        return JmsMessageMatchers.allMessagesReceivedMatch(exchangeMatcher);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesBodiesOnMatch(
            final T destination,
            final Matcher<String> matcher)
    {
        Matcher<Exchange> exchangeMatcher = IsExchange.withInMessage(
                IsMessage.withBody(matcher, String.class));
        return JmsMessageMatchers.allMessagesReceivedOnMatch(destination, exchangeMatcher);
    }
}
