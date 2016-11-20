package org.libex.primitives.tiny.integer;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Wrapper for natural (greater than or equal to 0) integer values
 */
@Immutable
public class NaturalInteger extends IntegerTinyType<NaturalInteger> {

    @Nonnull
    public static NaturalInteger of(final int value)
    {
        return new NaturalInteger(value);
    }

    public static NaturalInteger parseNaturalInt(
            final String input)
    {
        return of(Integer.parseUnsignedInt(input));
    }

    private NaturalInteger(final Integer value) {
        super(value);

        checkArgument(value >= 0, "A natural number must be >= 0. Passed " + value);
    }
}
