package org.libex.collect;

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
	 */
	public static <K, V> V getOrInsert(Map<? super K, V> map, K key,
			Supplier<? extends V> supplier) {
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
	 */
	public static <T, K, V> Map<K, V> uniqueIndex(Iterable<T> input,
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction) {
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
	 */
	public static <T, K, V> Map<K, V> uniqueIndex(Iterator<T> input,
			Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction) {

		Map<K, V> map = Maps.newHashMap();
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
	 * @param input
	 *            the values to pass to the Functions
	 * @param keyFunction
	 *            the key producer, may produce more than one key for each value
	 * @param valueFunction
	 *            the value produces, the identity function may be used
	 * @return the created map
	 * 
	 * @see com.google.common.base.Functions#identity()
	 */
	public static <T, K, V> Map<K, V> multipleIndex(Iterable<T> inputs,
			Function<? super T, ? extends Iterable<? extends K>> keyFunction,
			Function<? super T, ? extends V> valueFunction) {
		return multipleIndex(inputs.iterator(), keyFunction, valueFunction);
	}

	/**
	 * Creates a Map given a set of inputs, a Function to generate the key(s)
	 * associated with each value and a Function to generate the value.
	 * 
	 * @param input
	 *            the values to pass to the Functions
	 * @param keyFunction
	 *            the key producer, may produce more than one key for each value
	 * @param valueFunction
	 *            the value produces, the identity function may be used
	 * @return the created map
	 * 
	 * @see com.google.common.base.Functions#identity()
	 */
	public static <T, K, V> Map<K, V> multipleIndex(Iterator<T> inputs,
			Function<? super T, ? extends Iterable<? extends K>> keyFunction,
			Function<? super T, ? extends V> valueFunction) {

		Map<K, V> map = Maps.newHashMap();
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

	private MapsEx() {
	}
}
