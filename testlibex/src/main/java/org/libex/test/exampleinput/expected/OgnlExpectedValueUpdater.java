package org.libex.test.exampleinput.expected;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import lombok.Data;
import lombok.experimental.Accessors;
import ognl.Ognl;
import ognl.OgnlException;

import org.libex.test.exampleinput.expected.DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput;
import org.libex.test.exampleinput.expected.DefaultModifiableExampleInputWithExpectedValues.ExpectedValueUpdater;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class OgnlExpectedValueUpdater
        implements ExpectedValueUpdater<Object> {

    @Data
    @Accessors(chain = true)
    public static class Builder {
        private final Object expression;
        @Nullable
        private final String variableName;
        private Function<ExpectedValueConverterInput<?>, ?> valueRetriever =
                DefaultModifiableExampleInputWithExpectedValues.toCurrentEntryValue(Object.class);
        private Function<?, ?> valueConverter = Functions.identity();

        private Builder(
                final Object expression,
                final String variableName) {
            super();
            this.expression = expression;
            this.variableName = variableName;
        }

        public OgnlExpectedValueUpdater build()
        {
            return new OgnlExpectedValueUpdater(this);
        }
    }

    public static Builder createSetValueOgnlWithExpression(
            final String expression)
    {
        try {
            return new Builder(Ognl.parseExpression(expression), null);
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }

    public static final String DEFAULT_VARIABLE_NAME = "setterVariable";

    public static final String format(
            final String template)
    {
        return String.format(template, "#" + DEFAULT_VARIABLE_NAME);
    }

    public static Builder formatSetUsingGetValueOgnlExpression(
            final String expression)
    {
        try {
            return new Builder(Ognl.parseExpression(
                    format(expression)), DEFAULT_VARIABLE_NAME);
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }

    public static Builder createGetValueOgnlWithExpression(
            final String expression,
            final String variableName)
    {
        try {
            return new Builder(Ognl.parseExpression(expression), variableName);
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }

    private final Object expression;
    @Nullable
    private final String variableName;
    private final Function<ExpectedValueConverterInput<?>, ?> valueRetriever;
    @SuppressWarnings("rawtypes")
    private final Function valueConverter;

    private OgnlExpectedValueUpdater(
            final Builder builder) {
        this.expression = builder.expression;
        this.variableName = builder.variableName;

        this.valueRetriever = builder.valueRetriever;
        this.valueConverter = builder.valueConverter;
    }

    @Override
    public void update(
            final ExpectedValueConverterInput<?> input)
    {
        try {
            Object currentEntryValue = valueRetriever.apply(input);

            @SuppressWarnings("unchecked")
            Object newValue = valueConverter.apply(currentEntryValue);

            if (variableName == null) {
                Ognl.setValue(expression, input.getExpectedValue(), newValue);
            } else {
                Map<String, Object> map = newHashMap();
                map.put(variableName, newValue);
                Ognl.getValue(expression, map, input.getExpectedValue());
            }
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }
}
