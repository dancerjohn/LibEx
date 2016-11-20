package org.libex.test.exampleinput;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Use for getting example input that can be customized. This usually uses a content engine
 * like Velocity to update the input.
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface ModifiableExampleInput extends ExampleInput {

    // Builder

    public static interface Builder {
        ModifiableExampleInput build();

        /**
         * Overrides a single variable value.
         * 
         * @param key
         *            key to override
         * @param value
         *            new value
         * @return this builder
         */
        Builder override(
                final String key,
                final Object value);

        /**
         * Overrides a set of variable values
         * 
         * @param overrides
         *            map of keys/values to override
         * @return this builder
         */
        Builder overrideAll(
                final Map<String, ?> overrides);
    }

    /**
     * Copy constructor
     * 
     * @return new Builder
     */
    Builder copy();

    ModifiableExampleInput replace(
            final String key,
            @Nullable final Object value);

    // Core

    /**
     * Gets example input but using variable override value
     * 
     * @param key
     *            key to override
     * @param value
     *            new value
     * @return updated example input
     */
    String getInputUsing(
            final String key,
            @Nullable final Object value);

    /**
     * Gets example input but using variable override values
     * 
     * @param overrides
     *            map of keys to override with associated values
     * @return updated example input
     */
    String getInputUsing(
            @Nullable final Map<String, ?> overrides);

    /**
     * @return Gets a copy of the variables used in with the template
     */
    Map<String, Object> getVariables();

    /**
     * Gets example input but using variable override value
     * 
     * @param key
     *            key to update
     * @param value
     *            new value
     * @return the updated variable map
     */
    Map<String, Object> getVariablesUsing(
            final String key,
            @Nullable final Object value);

    /**
     * Gets example input but using variable override values
     * 
     * @param overrides
     *            map of keys to override with associated values
     * @return the updated variable map
     */
    Map<String, Object> getVariablesUsing(
            @Nullable final Map<String, ?> overrides);
}
