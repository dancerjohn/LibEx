package org.libex.primitives.tiny.longs;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Wrapper for positive (greater than 0) integer values
 */
@Immutable
public class PositiveLong extends LongTinyType<PositiveLong> {

    /**
     * @param value
     *            the value
     * @return the PositiveLong
     */
    @Nonnull
    public static PositiveLong of(final long value)
    {
        return new PositiveLong(value);
    }

    private PositiveLong(final Long value)
    {
        super(value);

        checkArgument(value > 0L, "A positive number must be > 0. Passed " + value);
    }
}
