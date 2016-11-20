package org.libex.primitives.tiny.longs;

import javax.annotation.concurrent.Immutable;

import org.libex.tiny.ComparableTinyType;

/**
 * Wrapper for Integer TinyTypes
 *
 * @param <SELF_CLASS>
 *            this type
 */
@Immutable
public abstract class LongTinyType<SELF_CLASS extends LongTinyType<? extends Object>>
        extends ComparableTinyType<Long, SELF_CLASS> {

    protected LongTinyType(final Long value)
    {
        super(value);
    }

    /**
     * @return int value
     */
    public long getLong()
    {
        return this.get();
    }
}
