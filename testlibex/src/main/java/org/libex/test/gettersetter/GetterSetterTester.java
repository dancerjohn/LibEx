package org.libex.test.gettersetter;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.collect.MapsEx;
import org.libex.test.gettersetter.object.DateGetterSetterTester;
import org.libex.test.gettersetter.object.DateTimeGetterSetterTester;
import org.libex.test.gettersetter.object.ObjectGetterSetterTester;
import org.libex.test.gettersetter.object.StringGetterSetterTester;
import org.libex.test.gettersetter.primitive.BooleanGetterSetterTester;
import org.libex.test.gettersetter.primitive.ByteGetterSetterTester;
import org.libex.test.gettersetter.primitive.CharacterGetterSetterTester;
import org.libex.test.gettersetter.primitive.DoubleGetterSetterTester;
import org.libex.test.gettersetter.primitive.FloatGetterSetterTester;
import org.libex.test.gettersetter.primitive.IntegerGetterSetterTester;
import org.libex.test.gettersetter.primitive.LongGetterSetterTester;
import org.libex.test.gettersetter.primitive.ShortGetterSetterTester;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@ParametersAreNonnullByDefault
@ThreadSafe
public class GetterSetterTester {

	private static final ImmutableMap<Class<?>, TypedGetterSetterTester<?>> mappedTesters;

	static {
		Iterable<TypedGetterSetterTester<?>> testers = Lists.<TypedGetterSetterTester<?>> newArrayList(
				// Primitives
				new BooleanGetterSetterTester(),
				new CharacterGetterSetterTester(),
				new ByteGetterSetterTester(),
				new ShortGetterSetterTester(),
				new IntegerGetterSetterTester(),
				new LongGetterSetterTester(),
				new FloatGetterSetterTester(),
				new DoubleGetterSetterTester(),
				// Objects
				new StringGetterSetterTester(),
				new DateGetterSetterTester(),
				new DateTimeGetterSetterTester(),
				new ObjectGetterSetterTester<Object>());
		mappedTesters = MapsEx.multipleIndex(testers, TypedGetterSetterTester.toMatchingTypes,
				Functions.<TypedGetterSetterTester<?>> identity());
	}

	public static void testGetterSetter(Object instanceUnderTest,
			String fieldName,
			Class<?> fieldType) {
		createTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName, fieldType)
				.testGetterSetter();
	}

	public static void testGetterSetter(Class<?> classUnderTest,
			String staticFieldName,
			Class<?> fieldType) {
		createTester(classUnderTest, null, staticFieldName, fieldType)
				.testGetterSetter();
	}

	public static DefaultGetterSetterTester<?> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName,
			Class<?> fieldType) {
		TypedGetterSetterTester<?> tester = findTester(fieldType);

		checkArgument(tester != null, "No tester found for field type " + fieldType);

		return tester.createTester(classUnderTest, instanceUnderTest, fieldName, fieldType);
	}

	@Nullable
	private static TypedGetterSetterTester<?> findTester(Class<?> fieldType) {
		TypedGetterSetterTester<?> tester = mappedTesters.get(fieldType);
		if (tester == null) {
			Class<?> parentType = fieldType.getSuperclass();
			if (parentType != null) {
				tester = findTester(parentType);
			}
		}
		return tester;
	}
}
