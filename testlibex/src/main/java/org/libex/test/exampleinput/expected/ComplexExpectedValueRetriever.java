package org.libex.test.exampleinput.expected;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Data;
import lombok.experimental.Accessors;

import org.libex.test.exampleinput.expected.DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
@Data
public class ComplexExpectedValueRetriever<T, NewValueType>
        implements Function<ExpectedValueConverterInput<? extends T>, Object> {
    
    @Data
    @Accessors(chain = true)
    public static class NewValueExpectedValueConverterInput<T, NewValueType>{
        private final ExpectedValueConverterInput<? extends T> input;
        private NewValueType newValue; 
    }
    
    private final Function<ExpectedValueConverterInput<? extends T>, NewValueType> toNewValue;
    private final Function<NewValueExpectedValueConverterInput<?, NewValueType>, ?> toFinalValue;

    @Override
    public Object apply(
            final ExpectedValueConverterInput<? extends T> input)
    {
        NewValueType newValue = toNewValue.apply(input);
        NewValueExpectedValueConverterInput<T, NewValueType> newInput = 
                new NewValueExpectedValueConverterInput<T, NewValueType>(input)
                        .setNewValue(newValue);
        Object finalValue = toFinalValue.apply(newInput);
        return finalValue;
    }

}
