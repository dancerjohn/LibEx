package org.libex.test.validation.message;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Matcher;

/**
 * Message Matcher for a List of elements
 * 
 * @see BaseMessageMatcher
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface IterableMessageMatcher<InputType, MatchedType>
        extends BaseMessageMatcher<InputType, MatchedType> {

    /**
     * @return the Matcher
     */
    @Nonnull
    Matcher<? super List<MatchedType>> getMatcher();
}
