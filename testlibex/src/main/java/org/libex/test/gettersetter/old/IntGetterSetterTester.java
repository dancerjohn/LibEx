package org.libex.test.gettersetter.old;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class IntGetterSetterTester {

	private static final Integer[] values = new Integer[] { Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE };

	public static void testIntGetterSetter(Object instanceUnderTest,
			String fieldName) {
		createTester(instanceUnderTest.getClass(), fieldName)
				.instanceUnderTest(instanceUnderTest)
				.testGetterSetter();
	}

	public static void testIntGetterSetter(Class<?> classUnderTest,
			String staticFieldName) {
		createTester(classUnderTest, staticFieldName)
				.testGetterSetter();
	}

	private static BaseGetterSetterTester createTester(Class<?> classUnderTest,
			String fieldName) {
		return new BaseGetterSetterTester()
				.fieldName(fieldName)
				.fieldType(int.class)
				.classUnderTest(classUnderTest)
				.valuesToTest(values);
	}
}
