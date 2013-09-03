package org.libex.test.gettersetter.old;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class LongPrimitiveGetterSetterTester {

	private static final Long[] values = new Long[] { Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE };
	private static final ImmutableList<Long> LongValues = ImmutableList.copyOf(values);

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

	public static DefaultGetterSetterTester<Long> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<Long>(classUnderTest,
				instanceUnderTest,
				fieldName,
				long.class,
				LongValues);
	}
}
