package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ShortGetterSetterTester extends PrimitiveGetterSetterTester<Short> {

	private static final Short[] values = new Short[] { null, Short.MIN_VALUE, -1, 0, 1, Short.MAX_VALUE };

	public ShortGetterSetterTester() {
		super(short.class, Short.class, values);
	}
}
