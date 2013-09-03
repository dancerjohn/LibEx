package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class BooleanGetterSetterTester extends PrimitiveGetterSetterTester<Boolean> {

	private static final Boolean[] values = new Boolean[] { null, Boolean.TRUE, Boolean.FALSE };

	public BooleanGetterSetterTester() {
		super(boolean.class, Boolean.class, values);
	}
}
