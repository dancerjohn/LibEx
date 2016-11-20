package org.libex.tiny;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

@Immutable
@ThreadSafe
@ParametersAreNonnullByDefault
public abstract class TinyType<T> {

    @Nonnull
    private final T value;

    protected TinyType(T value) {
        this.value = checkNotNull(value);
    }

    @Nonnull
    public T get()
    {
        return value;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        TinyType<T> tinyType = getClass().cast(o);
        return value.equals(tinyType.get());
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @Override
    @Nonnull
    public String toString()
    {
        return value.toString();
    }

}
