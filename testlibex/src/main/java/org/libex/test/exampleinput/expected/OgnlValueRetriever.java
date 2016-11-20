package org.libex.test.exampleinput.expected;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import ognl.Ognl;
import ognl.OgnlException;

import org.libex.test.exampleinput.expected.DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@ThreadSafe
public class OgnlValueRetriever
        implements Function<Object, Object> {

    public static Function<ExpectedValueConverterInput<?>, Object> composeWithGetCurrentValue(
            final String expression)
    {
        Function<Object, Object> getter = new OgnlValueRetriever(expression);
        Function<ExpectedValueConverterInput<?>, Object> result =
                Functions.compose(getter,
                        DefaultModifiableExampleInputWithExpectedValues.toCurrentEntryValue(Object.class));
        return result;
    }

    public static Function<ExpectedValueConverterInput<?>, Object> composeWithGetExpectedValue(
            final String expression)
    {
        Function<Object, Object> getter = new OgnlValueRetriever(expression);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Function<ExpectedValueConverterInput<?>, Object> result =
                (Function) Functions.compose(getter,
                        DefaultModifiableExampleInputWithExpectedValues.toExpectedValue());
        return result;
    }

    private final Object expression;

    public OgnlValueRetriever(
            final String expression) {
        try {
            this.expression = Ognl.parseExpression(expression);
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Object apply(
            final Object input)
    {
        try {
            return Ognl.getValue(expression, input);
        } catch (OgnlException e) {
            throw Throwables.propagate(e);
        }
    }

}
