package org.libex.test.jms.validation.matcher;

import java.util.Map;

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
public final class JmsMessageHeadersMatchers {

    private JmsMessageHeadersMatchers() {
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allReceivedHeadersMatch(
            final Matcher<? super Map<String, Object>> headerMatcher)
    {
        Matcher<Exchange> exchangeMatcher = IsExchange.withInMessage(
                IsMessage.withHeaders(headerMatcher));
        return JmsMessageMatchers.allMessagesReceivedMatch(exchangeMatcher);
    }

    public static <T> TypeSafeMatcher<JmsExchangeCapturer<T>> allMessagesHeadersOnMatch(
            final T destination,
            final Matcher<? super Map<String, Object>> headerMatcher)
    {
        Matcher<Exchange> exchangeMatcher = IsExchange.withInMessage(
                IsMessage.withHeaders(headerMatcher));
        return JmsMessageMatchers.allMessagesReceivedOnMatch(destination, exchangeMatcher);
    }
}
