package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class IntegerGetterSetterTester extends PrimitiveGetterSetterTester<Integer> {

	private static final Integer[] values = new Integer[] { null, Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE };

	public IntegerGetterSetterTester() {
		super(int.class, Integer.class, values);
	}
}
