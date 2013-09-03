package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class LongGetterSetterTester extends PrimitiveGetterSetterTester<Long> {

	private static final Long[] values = new Long[] { null, Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE };

	public LongGetterSetterTester() {
		super(long.class, Long.class, values);
	}
}
