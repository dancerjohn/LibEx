package org.libex.test.exampleinput;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Used for retrieving example inputs.
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface ExampleInput {

    /**
     * @return gets the default input
     */
    String getInput();
}
