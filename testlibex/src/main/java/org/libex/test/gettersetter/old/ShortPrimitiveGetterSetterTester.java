package org.libex.test.gettersetter.old;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ShortPrimitiveGetterSetterTester {

	private static final Short[] values = new Short[] { Short.MIN_VALUE, -1, 0, 1, Short.MAX_VALUE };
	private static final ImmutableList<Short> ShortValues = ImmutableList.copyOf(values);

	public static void testIntGetterSetter(Object instanceUnderTest,
			String fieldName) {
		createTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName)
				.testGetterSetter();
	}

	public static void testIntGetterSetter(Class<?> classUnderTest,
			String staticFieldName) {
		createTester(classUnderTest, null, staticFieldName)
				.testGetterSetter();
	}

	public static DefaultGetterSetterTester<Short> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<Short>(classUnderTest,
				instanceUnderTest,
				fieldName,
				short.class,
				ShortValues);
	}
}
