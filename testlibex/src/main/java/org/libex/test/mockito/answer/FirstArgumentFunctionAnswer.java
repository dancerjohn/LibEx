package org.libex.test.mockito.answer;

import static com.google.common.base.Preconditions.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Function;

/**
 * Allows for uning a {@link Function} to determine the answer to a mocked method
 * that has a single argument. May also be used where there are no arguments if the {@link Function} can accept null;
 *
 * @param <I>
 *            expected type of the first (and optional) argument to the mocked method. Input type of the Function.
 * @param <T>
 *            expected return type of the mocked method. Return type of the Function
 */
public class FirstArgumentFunctionAnswer<I, T> implements Answer<T> {

    public static <I, T> FirstArgumentFunctionAnswer<I, T> create(Function<? super I, ? extends T> function) {
        return new FirstArgumentFunctionAnswer<I, T>(function);
    }

    private final Function<? super I, ? extends T> function;

    public FirstArgumentFunctionAnswer(Function<? super I, ? extends T> function) {
        this.function = checkNotNull(function);
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        @SuppressWarnings("unchecked")
        I firstArgument = (arguments.length > 0) ? (I) arguments[0] : null;
        return function.apply(firstArgument);
    }

}
