package org.libex.primitives.tiny.integer;

import javax.annotation.concurrent.Immutable;

import org.libex.tiny.ComparableTinyType;

/**
 * Wrapper for Integer TinyTypes
 *
 * @param <SELF_CLASS>
 *            this type
 */
@Immutable
public abstract class IntegerTinyType<SELF_CLASS extends IntegerTinyType<? extends Object>>
        extends ComparableTinyType<Integer, SELF_CLASS> {

    protected IntegerTinyType(final Integer value) {
        super(value);
    }

    /**
     * @return int value
     */
    public int getInt()
    {
        return this.get();
    }
}
