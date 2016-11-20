package org.libex.hamcrest;

import static org.hamcrest.core.IsEqual.*;

import java.util.Map;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class IsMapWithSize extends FeatureMatcher<Map<?, ?>, Integer> {
    public IsMapWithSize(final Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "a map with size", "map size");
    }

    @Override
    protected Integer featureValueOf(final Map<?, ?> actual) {
        return actual.size();
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the <code>size()</code> method returns
     * a value that satisfies the specified matcher.
     * 
     * For example:
     * 
     * <pre>
     * assertThat(myMap, hasSize(equalTo(2)))
     * </pre>
     * 
     * @param sizeMatcher
     *            a matcher for the size of an examined {@link java.util.Map}
     * @return a matcher
     */
    @Factory
    public static Matcher<Map<?, ?>> hasSize(
            final Matcher<? super Integer> sizeMatcher)
    {
        return new IsMapWithSize(sizeMatcher);
    }

    /**
     * Creates a matcher for {@link java.util.Map}s that matches when the <code>size()</code> method returns
     * a value equal to the specified <code>size</code>.
     * 
     * For example:
     * 
     * <pre>
     * assertThat(myMap, hasSize(2))
     * </pre>
     * 
     * @param size
     *            the expected size of an examined {@link java.util.Map}
     * @return a matcher
     */
    @Factory
    public static Matcher<Map<?, ?>> hasSize(
            final int size)
    {
        Matcher<? super Integer> matcher = equalTo(size);
        return IsMapWithSize.hasSize(matcher);
    }

}

