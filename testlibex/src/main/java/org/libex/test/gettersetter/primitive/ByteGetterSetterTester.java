package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ByteGetterSetterTester extends PrimitiveGetterSetterTester<Byte> {
	private static final Byte[] values = new Byte[] { null, Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };

	public ByteGetterSetterTester() {
		super(byte.class, Byte.class, values);
	}
}
