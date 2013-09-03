package org.libex.collect;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Supplier;

@ThreadSafe
@ParametersAreNonnullByDefault
public final class ObjectArraysEx {

	public static <T> T[] fill(T[] array, Supplier<? extends T> supplier) {
		return fill(array, supplier, 0, array.length);
	}

	public static <T> T[] fill(T[] array, Supplier<? extends T> supplier, int startIndex, int endIndex) {
		for (int i = startIndex; i < endIndex; i++) {
			array[i] = supplier.get();
		}
		return array;
	}

	private ObjectArraysEx() {
	}

}
