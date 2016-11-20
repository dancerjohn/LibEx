package org.libex.base;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class FoldFunctions {

    private static final FoldFunction<Integer, Integer> INTEGER_SUMMER = new FoldFunction<Integer, Integer>() {

        @Override
        public Integer apply(Integer a, Integer b) {
            if (a == null) {
                return b;
            }
            if (b == null) {
                return a;
            }
            return a + b;
        }
    };

    public static FoldFunction<Integer, Integer> integerSummer() {
        return INTEGER_SUMMER;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> FoldFunction<T, T> summer(Class<T> type) {
        if (type.equals(Integer.class) || type.equals(int.class)) {
            return (FoldFunction) INTEGER_SUMMER;
        } else {
            return null;
        }
    }
}
