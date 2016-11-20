package org.libex.test.exampleinput.expected;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public interface WithExpectedValues<T> {

    T getExpectedValues();
}
