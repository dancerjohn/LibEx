package org.libex.test.exampleinput;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * @param <InputType>
 *            the template specification
 * 
 * @see VelocityEngineConversionFunction
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
public class DefaultModifiableExampleInput<InputType>
        implements ModifiableExampleInput {

    @lombok.Builder
    @Getter
    public static class ConverterInput<InputType> {
        private final InputType inputPath;
        private final Map<String, ?> variables;
    }

    @Getter
    @Setter
    @Accessors(chain = true, fluent = true)
    public static class ModifiableExampleInputBuilder<InputType>
            implements ModifiableExampleInput.Builder {

        private InputType input;
        private Map<String, Object> variables = newHashMap();
        private Function<ConverterInput<InputType>, String> converter;

        /**
         * Replaces all existing variables with the provided map. To override as subset
         * use {@link ModifiableExampleInputBuilder#override(String, Object)} or
         * {@link ModifiableExampleInputBuilder#overrideAll(Map)}
         * 
         * @param overrides
         *            map of keys to override with associated values
         * @return this builder
         */
        public ModifiableExampleInputBuilder<InputType> variables(
                final Map<String, ?> overrides)
        {
            this.variables = Maps.newHashMap(overrides);
            return this;
        }

        @Override
        public ModifiableExampleInputBuilder<InputType> override(
                final String key,
                @Nullable final Object value)
        {
            variables.put(key, value);
            return this;
        }

        @Override
        public Builder overrideAll(
                final Map<String, ?> overrides)
        {
            variables.putAll(overrides);
            return this;
        }

        @Override
        public DefaultModifiableExampleInput<InputType> build()
        {
            return new DefaultModifiableExampleInput<>(input, variables, converter);
        }
    }

    public static <InputType> ModifiableExampleInputBuilder<InputType> builder()
    {
        return new ModifiableExampleInputBuilder<InputType>();
    }

    private final InputType input;
    private final Map<String, Object> variables;
    private final Function<ConverterInput<InputType>, String> converter;

    /**
     * @param input
     *            input document
     * @param converter
     *            conversion engine to use.
     * 
     * @see VelocityEngineConversionFunction
     */
    public DefaultModifiableExampleInput(
            final InputType input,
            final Function<ConverterInput<InputType>, String> converter) {
        this(input, ImmutableMap.<String, String> of(), converter);
    }

    /**
     * @param converter
     *            conversion engine to use.
     * 
     * @see VelocityEngineConversionFunction
     * 
     * @param input
     *            input document
     * @param variables
     *            variables to use to modify input
     * @param converter
     *            function to convert input to String
     */
    public DefaultModifiableExampleInput(
            final InputType input,
            final Map<String, ?> variables,
            final Function<ConverterInput<InputType>, String> converter) {
        this.input = checkNotNull(input);
        this.converter = checkNotNull(converter);
        this.variables = Maps.newHashMap(variables);
    }

    /**
     * Creates an example based on an existing version with variable overrides
     * 
     * @param base
     *            the base example
     * @param overrides
     *            the variable overrides
     * 
     * @see #copy()
     */
    public DefaultModifiableExampleInput(
            final DefaultModifiableExampleInput<InputType> base,
            final Map<String, ?> overrides) {
        this.input = checkNotNull(base.input);
        this.converter = checkNotNull(base.converter);

        Map<String, Object> overriddenVariables = Maps.newHashMap(base.variables);
        overriddenVariables.putAll(overrides);
        this.variables = overriddenVariables;
    }

    @Override
    public ModifiableExampleInputBuilder<InputType> copy()
    {
        return DefaultModifiableExampleInput.<InputType> builder()
                .input(input)
                .variables(variables)
                .converter(converter);
    }

    @Override
    public ModifiableExampleInput replace(
            final String key,
            final Object value)
    {
        return copy()
                .override(key, value)
                .build();
    }

    @Override
    public String getInput()
    {
        return executeConversionUsing(variables);
    }

    @Override
    public String getInputUsing(
            final String key,
            @Nullable final Object value)
    {
        return getInputUsing(getVariablesUsing(key, value));
    }

    @Override
    public String getInputUsing(
            @Nullable final Map<String, ?> overrides)
    {
        return executeConversionUsing(getVariablesUsing(overrides));
    }

    private String executeConversionUsing(
            final Map<String, ?> inputVariables)
    {
        return converter.apply(
                ConverterInput.<InputType> builder()
                        .inputPath(input)
                        .variables(inputVariables)
                        .build());
    }

    @Override
    public Map<String, Object> getVariables()
    {
        return Maps.newHashMap(variables);
    }

    @Override
    public Map<String, Object> getVariablesUsing(
            final String key,
            @Nullable final Object value)
    {
        Map<String, Object> map = Maps.newHashMap();
        map.put(key, value);
        return getVariablesUsing(map);
    }

    @Override
    public Map<String, Object> getVariablesUsing(
            @Nullable final Map<String, ?> overrides)
    {
        Map<String, Object> overriddenVariables = Maps.newHashMap(variables);

        if (overrides != null) {
            overriddenVariables.putAll(overrides);
        }
        return overriddenVariables;
    }
}
