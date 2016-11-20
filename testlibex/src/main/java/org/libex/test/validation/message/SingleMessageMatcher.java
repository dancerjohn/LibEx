package org.libex.test.validation.message;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Matcher;

import com.google.common.base.Predicate;

/**
 * Message Matcher against a single message
 * 
 * @param <InputType>
 *            type provided by the capturer
 * @param <MatchedType>
 *            type to be matched
 * 
 * @see BaseMessageMatcher
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface SingleMessageMatcher<InputType, MatchedType>
        extends BaseMessageMatcher<InputType, MatchedType> {

    /**
     * @return a Predicate that determines if the provided input message should be validated
     */
    Predicate<? super InputType> getPredicate();

    /**
     * @return the Matcher to use against each message
     */
    Matcher<? super MatchedType> getMatcher();
}
