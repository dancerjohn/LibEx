package org.libex.commons.tuple;

import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Provides utility methods on {@link Pair}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class PairsEx {

    /**
     * Memoizes the left parameter to create a {@link Function} to create a {@link Pair} from the right parameter
     * 
     * @param <Left>
     *            the left type of the {@link Pair}
     * @param <Right>
     *            the right type of the {@link Pair}
     * @param left
     *            the Left value to memoize
     * @return the {@link Function}
     */
    public static <Left, Right> Function<Right, Pair<Left, Right>> memoizeLeft(
            final Left left,
            final Class<Right> rightType)
    {
        return right -> Pair.of(left, right);
    }

    /**
     * Memoizes the right parameter to create a {@link Function} to create a {@link Pair} from the left parameter
     * 
     * @param <Left>
     *            the left type of the {@link Pair}
     * @param <Right>
     *            the right type of the {@link Pair}
     * @param right
     *            the Right value to memoize
     * @return the {@link Function}
     */
    public static <Left, Right> Function<Left, Pair<Left, Right>> memoizeRight(
            final Right right,
            final Class<Left> leftType)
    {
        return left -> Pair.of(left, right);
    }

    private PairsEx() {
    }

}
