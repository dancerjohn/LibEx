package org.libex.hamcrest;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class MatchersEx {

    /**
     * Applies the passed Function on the input and provides the Function's result
     * to the passed Matcher for verification.
     * 
     * @param <T>
     *            pre-transformed type
     * @param <V>
     *            matched type
     * @param transformer
     *            the pre-match transformer
     * @param matcher
     *            the matcher to use on the transformed object
     * @return A Matcher that first transforms the input before verifying it.
     */
    public static <T, V> Matcher<T> compose(final Function<T, ? extends V> transformer, final Matcher<V> matcher) {
        checkNotNull(transformer, "transformer");
        checkNotNull(matcher, "matcher");

        return new TypeSafeMatcher<T>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("a value that when transformed by " + transformer + " is ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final T item) {
                V transformed = transformer.apply(item);
                return matcher.matches(transformed);
            }
        };
    }

    private MatchersEx() {
    }
}
