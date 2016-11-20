package org.libex.primitives;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class IntsEx {

    public static Function<Integer, Integer> modulus(final int mod) {
        checkArgument(mod != 0, "mod may not be 0");

        return new Function<Integer, Integer>() {

            @Override
            public Integer apply(final Integer input) {
                if (input == null) {
                    return 0;
                } else {
                    return input % mod;
                }
            }
        };
    }

    private static Function<String, Integer> PARSE = new Function<String, Integer>() {

        @Override
        public Integer apply(
                final String input)
        {
            return Integer.parseInt(input);
        }
    };

    /**
     * @return A Function that uses {@link Integer#parseInt} to convert the passed {@link String} to an {@link Integer}
     */
    public static Function<String, Integer> parse()
    {
        return PARSE;
    }

    private IntsEx() {
    }

}
