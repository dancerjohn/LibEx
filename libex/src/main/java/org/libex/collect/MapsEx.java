package org.libex.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

/**
 * Utilities on Map instances.
 * 
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class MapsEx {

	    /**
     * Gets the value associated with the passed key. If no value is associated
     * gets a value from the supplier and inserts that value in the map. In
     * either case the value returned is the value stored in the map after the
     * insert (if done).
     * 
     * @param map
     *            the map to search and/or modify
     * @param key
     *            the key of the value to retrieve
     * @param supplier
     *            the supplier to use to retrieve a value to insert in the map
     *            if no value is associated with the key
     * @return the value stored in the map if the key is mapped to a value,
     *         otherwise the value returned by the supplier
     * 
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
	public static <K, V> V getOrInsert(final Map<? super K, V> map, final K key,
			final Supplier<? extends V> supplier) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			V value = supplier.get();
			map.put(key, value);
			return value;
		}
	}

	    /**
     * Creates a Map given a set of inputs, a Function to generate the key and a
     * Function to generate the value.
     * 
     * @param input
     *            the values to pass to the Functions
     * @param keyFunction
     *            the key producer, the identity function may be used
     * @param valueFunction
     *            the value produces, the identity function may be used
     * @return the created map
     * 
     * @see com.google.common.base.Functions#identity()
     * 
     * @param <T>
     *            input type
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
	public static <T, K, V> Map<K, V> uniqueIndex(final Iterable<T> input,
			final Function<? super T, ? extends K> keyFunction,
			final Function<? super T, ? extends V> valueFunction) {
		return uniqueIndex(input.iterator(), keyFunction, valueFunction);
	}

	    /**
     * Creates a Map given a set of inputs, a Function to generate the key and a
     * Function to generate the value.
     * 
     * @param input
     *            the values to pass to the Functions
     * @param keyFunction
     *            the key producer, the identity function may be used
     * @param valueFunction
     *            the value produces, the identity function may be used
     * @return the created map
     * 
     * @see com.google.common.base.Functions#identity()
     * 
     * @param <T>
     *            input type
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
	public static <T, K, V> Map<K, V> uniqueIndex(final Iterator<T> input,
			final Function<? super T, ? extends K> keyFunction,
			final Function<? super T, ? extends V> valueFunction) {

        Map<K, V> map = Maps.newLinkedHashMap();
		while (input.hasNext()) {
			T inputValue = input.next();
			map.put(keyFunction.apply(inputValue),
					valueFunction.apply(inputValue));
		}
		return map;
	}

	        /**
     * Creates a Map given a set of inputs, a Function to generate the key(s)
     * associated with each value and a Function to generate the value.
     * 
     * @param inputs
     *            the values to pass to the Functions
     * @param keyFunction
     *            the key producer, may produce more than one key for each value
     * @param valueFunction
     *            the value produces, the identity function may be used
     * @return the created map
     * 
     * @see com.google.common.base.Functions#identity()
     * 
     * @param <T>
     *            input type
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
	public static <T, K, V> Map<K, V> multipleIndex(final Iterable<T> inputs,
			final Function<? super T, ? extends Iterable<? extends K>> keyFunction,
			final Function<? super T, ? extends V> valueFunction) {
		return multipleIndex(inputs.iterator(), keyFunction, valueFunction);
	}

	            /**
     * Creates a Map given a set of inputs, a Function to generate the key(s)
     * associated with each value and a Function to generate the value.
     * 
     * @param inputs
     *            the values to pass to the Functions
     * @param keyFunction
     *            the key producer, may produce more than one key for each value
     * @param valueFunction
     *            the value produces, the identity function may be used
     * @return the created map
     * 
     * @see com.google.common.base.Functions#identity()
     * 
     * @param <T>
     *            input type
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
	public static <T, K, V> Map<K, V> multipleIndex(final Iterator<T> inputs,
			final Function<? super T, ? extends Iterable<? extends K>> keyFunction,
			final Function<? super T, ? extends V> valueFunction) {

        Map<K, V> map = Maps.newLinkedHashMap();
		while (inputs.hasNext()) {
			T input = inputs.next();
			V value = valueFunction.apply(input);
			Iterable<? extends K> keys = keyFunction.apply(input);
			for (K key : keys) {
				map.put(key, value);
			}
		}
		return map;
	}

    /**
     * Creates a {@link Function} that retrieves the value associated with the key
     * passed to the {@link Function} with the value in the map passed to this method.
     * 
     * @param map
     *            the map from which to retrieve the value
     * @return the value associated with the map, if any
     * 
     * @deprecated Use Guava's Functions.forMap()
     * 
     * @param <K>
     *            key type
     * @param <V>
     *            value type
     */
    @Deprecated
    public static <K,V> Function<K,V> toValue(final Map<? super K, ? extends V> map){
        checkNotNull(map);
        return new Function<K, V>() {

            @Override
            public V apply(final K input)
            {
                return map.get(input);
            }
        };
	}

	private MapsEx() {
	}
}
