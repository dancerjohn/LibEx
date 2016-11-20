package org.libex.test.exampleinput.expected;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.exampleinput.ExampleInput;

@ParametersAreNonnullByDefault
@ThreadSafe
public interface ExampleInputWithExpectedValues<T>
        extends ExampleInput, WithExpectedValues<T> {

    ExampleInput getExampleInput();
}
