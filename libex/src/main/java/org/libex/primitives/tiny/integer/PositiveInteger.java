package org.libex.primitives.tiny.integer;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Wrapper for positive (greater than 0) integer values
 */
@Immutable
public class PositiveInteger extends IntegerTinyType<PositiveInteger> {

    /**
     * @param value
     *            the value
     * @return the PositiveInteger
     */
    @Nonnull
    public static PositiveInteger of(final int value)
    {
        return new PositiveInteger(value);
    }

    public static PositiveInteger parsePositiveInt(
            final String input)
    {
        return of(Integer.parseUnsignedInt(input));
    }

    private PositiveInteger(final Integer value) {
        super(value);

        checkArgument(value > 0, "A positive number must be > 0. Passed " + value);
    }
}
