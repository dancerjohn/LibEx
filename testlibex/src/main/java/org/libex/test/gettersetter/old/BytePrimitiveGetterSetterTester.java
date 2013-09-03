package org.libex.test.gettersetter.old;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
@Deprecated
public class BytePrimitiveGetterSetterTester {

	private static final Byte[] values = new Byte[] { Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };
	private static final ImmutableList<Byte> ByteValues = ImmutableList.copyOf(values);

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

	public static DefaultGetterSetterTester<Byte> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<Byte>(classUnderTest,
				instanceUnderTest,
				fieldName,
				byte.class,
				ByteValues);
	}
}
