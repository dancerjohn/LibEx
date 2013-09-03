package org.libex.collect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
@ParametersAreNonnullByDefault
public class CombiningHashMap2<K, I, V> extends HashMap<K, V> implements CombiningMap<K, V> {

	private static final long serialVersionUID = -5711996540977422317L;
	private final Combiner<? super I, V> combiner;

	public CombiningHashMap2(Combiner<? super I, V> combiner) {
		super();
		this.combiner = combiner;
	}

	public CombiningHashMap2(Combiner<? super I, V> combiner, int arg0, float arg1) {
		super(arg0, arg1);
		this.combiner = combiner;
	}

	public CombiningHashMap2(Combiner<? super I, V> combiner, int arg0) {
		super(arg0);
		this.combiner = combiner;
	}

	public CombiningHashMap2(Combiner<? super I, V> combiner, Map<? extends K, ? extends V> arg0) {
		super(arg0);
		this.combiner = combiner;
	}

	public V combineAndPut(K key, I value) {
		V previousValue = get(key);
		V valueToAdd = combiner.combine(previousValue, value);

		return super.put(key, valueToAdd);
	}
}
