package org.libex.test.gettersetter.old;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class CharPrimitiveGetterSetterTester {

	private static final Character[] values = new Character[] { Character.MIN_VALUE, 'a', ' ', '1', '#', '\r', Character.MAX_VALUE };
	private static final ImmutableList<Character> CharacterValues = ImmutableList.copyOf(values);

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

	public static DefaultGetterSetterTester<Character> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<Character>(classUnderTest,
				instanceUnderTest,
				fieldName,
				char.class,
				CharacterValues);
	}
}
