package org.libex.test.gettersetter.old;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class FloatPrimitiveGetterSetterTester {

	private static final Float[] values = new Float[] { Float.MIN_VALUE, -1.0f, 0.0f, 1.0f, 1.2344532f, Float.MAX_VALUE };
	private static final ImmutableList<Float> FloatValues = ImmutableList.copyOf(values);

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

	public static DefaultGetterSetterTester<Float> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<Float>(classUnderTest,
				instanceUnderTest,
				fieldName,
				float.class,
				FloatValues);
	}
}
