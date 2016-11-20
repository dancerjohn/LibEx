package org.libex.base;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class BiFunctions {

    public static <T> BiFunction<T, T> identity()
    {
        return new BiFunction<T, T>() {

            @Override
            public T toRight(
                    final T input)
            {
                return input;
            }

            @Override
            public T toLeft(
                    final T input)
            {
                return input;
            }
        };
    }

    private BiFunctions() {
    }
}
