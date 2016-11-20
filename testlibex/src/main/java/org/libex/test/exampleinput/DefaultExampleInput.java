package org.libex.test.exampleinput;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.io.InputStreamsEx;
import org.libex.io.ResourcesEx;

import com.google.common.base.Function;
import com.google.common.base.Functions;

@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
public class DefaultExampleInput<InputType> implements ExampleInput {

    public static final Function<String, String> classPathResourceToFileContents =
            Functions.compose(InputStreamsEx.asString(),
                    Functions.compose(ResourcesEx.toInputStream(), ResourcesEx.toClasspathResource()));

    private final InputType input;
    private final Function<InputType, String> toString;

    /**
     * @param input
     *            input to toString function
     * @param toString
     *            function to use to convert input string to input
     * 
     * @see InputTypeFunctions
     */
    public DefaultExampleInput(
            final InputType input,
            final Function<InputType, String> toString) {
        super();
        this.input = checkNotNull(input);
        this.toString = checkNotNull(toString);
    }

    @Override
    public String getInput()
    {
        return toString.apply(input);
    }

}
