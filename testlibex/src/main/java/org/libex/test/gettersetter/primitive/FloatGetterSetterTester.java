package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class FloatGetterSetterTester extends PrimitiveGetterSetterTester<Float> {

	private static final Float[] values = new Float[] { null, Float.MIN_VALUE, -1.0f, 0.0f, 1.0f, 1.2344532f, Float.MAX_VALUE };

	public FloatGetterSetterTester() {
		super(float.class, Float.class, values);
	}
}
