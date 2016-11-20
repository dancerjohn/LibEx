package org.libex.collect;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class MapEntrysEx {

    public static <K> Function<? extends Map.Entry<? extends K, ?>, K> toKey()
    {
        return new Function<Map.Entry<? extends K, ?>, K>() {

            @Override
            public K apply(
                    final Entry<? extends K, ?> input)
            {
                return input.getKey();
            }
        };
    }

    public static <V> Function<? extends Map.Entry<?, ? extends V>, V> toValue()
    {
        return new Function<Map.Entry<?, ? extends V>, V>() {

            @Override
            public V apply(
                    final Entry<?, ? extends V> input)
            {
                return input.getValue();
            }
        };
    }

    private MapEntrysEx()
    {
    }
}
