package org.libex.camel;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Message;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class IsMessage<BodyType> extends TypeSafeMatcher<Message> {

    public static IsMessage<Object> withBody(
            final Matcher<Object> matcher)
    {
        return new IsMessage<>(Object.class, matcher);
    }

    public static <T> IsMessage<T> withBody(
            final Matcher<? super T> matcher,
            final Class<T> type)
    {
        return new IsMessage<>(type, matcher);
    }

    public static <T> IsMessage<T> withHeaders(
            final Matcher<? super Map<String, Object>> headerMatcher)
    {
        return new IsMessage<T>(headerMatcher);
    }

    @Nullable
    private final Class<BodyType> bodyType;

    @Nullable
    private final Matcher<? super BodyType> bodyMatcher;

    @Nullable
    private final Matcher<? super Map<String, Object>> headerMatcher;

    private IsMessage(
            final Class<BodyType> bodyType,
            final Matcher<? super BodyType> bodyMatcher) {
        super();
        this.bodyType = checkNotNull(bodyType);
        this.bodyMatcher = checkNotNull(bodyMatcher);
        this.headerMatcher = null;
    }

    private IsMessage(
            final Matcher<? super Map<String, Object>> headerMatcher) {
        super();
        this.bodyType = null;
        this.bodyMatcher = null;
        this.headerMatcher = checkNotNull(headerMatcher);
    }

    @Override
    public void describeTo(
            final Description description)
    {
        if (bodyMatcher != null) {
            description.appendText(String.format("a Message whose body is of type %s and matches",
                    bodyType.getSimpleName()));
            bodyMatcher.describeTo(description);
        } else {
            description.appendText("a Message whose headers match ");
            headerMatcher.describeTo(description);
        }
    }

    @Override
    protected boolean matchesSafely(
            final Message message)
    {
        if (bodyMatcher != null) {
            return bodyMatches(message);
        } else {
            return headerMatcher.matches(message.getHeaders());
        }
    }

    private boolean bodyMatches(
            final Message message)
    {
        Object body = message.getBody();
        if (body != null
                && !(bodyType.isInstance(body))) {
            return false;
        }

        return bodyMatcher.matches(body);
    }
}
