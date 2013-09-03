package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class DoubleGetterSetterTester extends PrimitiveGetterSetterTester<Double> {

	private static final Double[] values = new Double[] { null, Double.MIN_VALUE, -1.0, 0.0, 1.0, 1.2344532, Double.MAX_VALUE };

	public DoubleGetterSetterTester() {
		super(double.class, Double.class, values);
	}
}
