package org.libex.tiny;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

@Immutable
@ThreadSafe
@ParametersAreNonnullByDefault
public abstract class ComparableTinyType<T extends Comparable<? super T>, SELF_CLASS extends ComparableTinyType<T, ? extends Object>>
        extends TinyType<T>
        implements Comparable<SELF_CLASS> {

    public ComparableTinyType(T value) {
        super(value);
    }

    @Override
    public int compareTo(SELF_CLASS other)
    {
        return get().compareTo(other.get());
    }
}
