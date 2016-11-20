package org.libex.primitives.tiny.longs;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Wrapper for natural (greater than or equal to 0) integer values
 */
@Immutable
public class NaturalLong extends LongTinyType<NaturalLong> {

    @Nonnull
    public static NaturalLong of(final long value)
    {
        return new NaturalLong(value);
    }

    protected NaturalLong(final Long value)
    {
        super(value);

        checkArgument(value >= 0L, "A natural number must be >= 0. Passed " + value);
    }
}
