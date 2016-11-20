package org.libex.collect;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class MultimapsEx {

    private MultimapsEx() {
    }

    private static final Function<Multimap<?, ?>, Collection<?>> TO_VALUES = new Function<Multimap<?, ?>, Collection<?>>() {

        @Override
        public Collection<?> apply(
                final Multimap<?, ?> input)
        {
            return input.values();
        }
    };

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Function<Multimap<?, T>, Collection<T>> toValues()
    {
        return (Function) TO_VALUES;
    }
}
