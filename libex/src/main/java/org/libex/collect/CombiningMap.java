package org.libex.collect;

import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public interface CombiningMap<K, V> extends Map<K, V> {

	public static interface Combiner<I, O> {
		@Nullable
		O combine(@Nullable O previousValue, @Nullable I newValue);
	}
}
