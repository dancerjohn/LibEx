package org.libex.camel;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class IsExchange extends TypeSafeMatcher<Exchange> {

    public static IsExchange withInMessage(
            final Matcher<Message> matcher)
    {
        return new IsExchange(MessageLocation.IN, matcher);
    }

    public static IsExchange withOutMessage(
            final Matcher<Message> matcher)
    {
        return new IsExchange(MessageLocation.OUT, matcher);
    }

    private final MessageLocation location;
    private final Matcher<Message> matcher;

    public IsExchange(
            final MessageLocation location,
            final Matcher<Message> matcher) {
        this.location = checkNotNull(location);
        this.matcher = checkNotNull(matcher);
    }

    @Override
    public void describeTo(
            final Description description)
    {
        description.appendText(String.format("an Exchange whose %s message matches ", location.name().toLowerCase()));
        matcher.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(
            final Exchange item)
    {
        if (item == null) {
            return false;
        }

        Message message = location.getMessage(item);
        return matcher.matches(message);
    }

}
