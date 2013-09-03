package org.libex.collect;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Supplier;

@ThreadSafe
@ParametersAreNonnullByDefault
public final class ListsEx {

	public static <T> List<T> fill(List<T> list, Supplier<T> supplier) {
		return fill(list, supplier, 0, list.size());
	}

	public static <T> List<T> fill(List<T> list, Supplier<T> supplier, int startIndex, int endIndex) {
		for (int i = startIndex; i < endIndex; i++) {
			if (list.size() > i) {
				list.set(i, supplier.get());
			} else if (list.size() == i) {
				list.add(supplier.get());
			} else {
				throw new IllegalArgumentException("List is not populated enough to set position " + startIndex);
			}
		}

		return list;
	}

	private ListsEx() {

	}
}
