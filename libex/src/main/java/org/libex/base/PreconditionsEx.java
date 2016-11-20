package org.libex.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class PreconditionsEx {

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param array
     *            object to check
     * @return passed object
     * 
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * @param <T>
     *            iterable type
     */
    public static <T> T[] checkNotEmpty(final T[] array)
    {
        return checkNotEmpty(array, null);
    }

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param array
     *            object to check
     * @param message
     *            message to use if check fails
     * @return passed object
     * 
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * @param <T>
     *            iterable type
     */
    public static <T> T[] checkNotEmpty(final T[] array, @Nullable final String message)
    {
        checkNotNull(array, message);
        checkArgument(array.length > 0, message);
        return array;
    }

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param iterable
     *            object to check
     * @return passed object
     * 
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * 
     * @param <V>
     *            type in the the Iterable
     * @param <T>
     *            iterable type
     */
    public static <V, T extends Iterable<V>> T checkNotEmpty(final T iterable)
    {
        return checkNotEmpty(iterable, null);
    }

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param iterable
     *            object to check
     * @param message
     *            message to use if check fails
     * @return passed object
     * 
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * 
     * @param <V>
     *            type in the the Iterable
     * @param <T>
     *            iterable type
     * 
     * @param <V>
     *            value type
     * @param <T>
     *            iterable type
     */
    public static <V, T extends Iterable<V>> T checkNotEmpty(final T iterable, @Nullable final String message)
    {
        checkNotNull(iterable, message);
        checkArgument(iterable.iterator().hasNext(), message);
        return iterable;
    }

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param map
     *            the map to check
     * @return the passed map
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * 
     * @param <M>
     *            type of the map
     * @param <V>
     *            map value type
     * @param <T>
     *            map key type
     */
    public static <T, V, M extends Map<T, V>> M checkNotEmpty(final M map)
    {
        return checkNotEmpty(map, null);
    }

    /**
     * Checks that passed value is not null and not empty.
     * 
     * @param map
     *            the map to check
     * @param message
     *            message to use in exception if map is empty
     * @return the passed map
     * @throws NullPointerException
     *             if null
     * @throws IllegalArgumentException
     *             if empty
     * 
     * @param <M>
     *            type of the map
     * @param <V>
     *            map value type
     * @param <T>
     *            map key type
     */
    public static <T, V, M extends Map<T, V>> M checkNotEmpty(final M map, @Nullable final String message)
    {
        checkNotNull(map, message);
        checkArgument(!map.isEmpty(), message);
        return map;
    }

    private PreconditionsEx()
    {
    }
}
