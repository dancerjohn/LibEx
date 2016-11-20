package org.libex.test.validation.message;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

/**
 * Base interface for Message Matchers
 * 
 * @param <InputType>
 *            type provided by the capturer
 * @param <TransformedType>
 *            type to be matched
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface BaseMessageMatcher<InputType, TransformedType> {

    /**
     * @return the message in case of a failure
     */
    @CheckForNull
    String getMessage();

    /**
     * @return the {@link Function} to use to convert the captured type to the matched type
     */
    @Nonnull
    Function<? super InputType, ? extends TransformedType> getFunction();

    /**
     * @return if the ErrorCollector (if provided) should be skipped to cause the test
     *         to fail fast in case of a test failure
     */
    boolean failFast();
}
