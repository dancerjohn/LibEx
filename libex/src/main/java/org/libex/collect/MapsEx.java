package org.libex.collect;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

/**
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class MapsEx {

	public static <K, V> V getOrInsert(Map<? super K, V> map, K key, Supplier<? extends V> supplier) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			V value = supplier.get();
			map.put(key, value);
			return value;
		}
	}

	public static <T, K, V> Map<K, V> uniqueIndex(Iterable<T> input, Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction) {
		return uniqueIndex(input.iterator(), keyFunction, valueFunction);
	}

	public static <T, K, V> Map<K, V> uniqueIndex(Iterator<T> input, Function<? super T, ? extends K> keyFunction,
			Function<? super T, ? extends V> valueFunction) {

		Map<K, V> map = Maps.newHashMap();
		while (input.hasNext()) {
			T inputValue = input.next();
			map.put(keyFunction.apply(inputValue), valueFunction.apply(inputValue));
		}
		return map;
	}

	public static <K, V> Map<K, V> multipleIndex(Iterable<V> values,
			Function<? super V, ? extends Iterable<? extends K>> keyFunction) {
		return multipleIndex(values.iterator(), keyFunction);
	}

	public static <K, V> Map<K, V> multipleIndex(Iterator<V> values,
			Function<? super V, ? extends Iterable<? extends K>> keyFunction) {

		Map<K, V> map = Maps.newHashMap();
		while (values.hasNext()) {
			V value = values.next();
			Iterable<? extends K> keys = keyFunction.apply(value);
			for (K key : keys) {
				map.put(key, value);
			}
		}
		return map;
	}

	private MapsEx() {
	}
}
