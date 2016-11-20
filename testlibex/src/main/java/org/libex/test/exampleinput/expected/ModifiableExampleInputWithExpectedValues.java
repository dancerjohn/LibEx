package org.libex.test.exampleinput.expected;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.libex.test.exampleinput.ExampleInput;
import org.libex.test.exampleinput.ModifiableExampleInput;

/**
 * 
 * @param <T>
 *            the expected values type
 */
@ParametersAreNonnullByDefault
@NotThreadSafe
public interface ModifiableExampleInputWithExpectedValues<T>
        extends ExampleInput, WithExpectedValues<T> {

    ModifiableExampleInput getExampleInput();

    ModifiableExampleInputWithExpectedValues<T> copyUsing(
            final ModifiableExampleInput input);

    ModifiableExampleInputWithExpectedValues<T> copyOverriding(
            final Map<String, ?> overrides);

    /**
     * Gets expected values but using variable override value
     * 
     * @param key
     *            the key to override
     * @param value
     *            the new value
     * @return the updated expected values
     */
    T getExpectedValuesUsing(
            final String key,
            @Nullable final Object value);

    /**
     * Gets expected values but using variable override values
     * 
     * @param overrides
     *            map of keys/values to override
     * @return the updated expected values
     */
    T getExpectedValuesUsing(
            @Nullable final Map<String, ?> overrides);

}
