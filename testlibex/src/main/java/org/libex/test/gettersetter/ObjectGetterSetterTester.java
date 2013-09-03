package org.libex.test.gettersetter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ObjectGetterSetterTester<T> implements TypedGetterSetterTester<T> {

	// public static <T> void testObjectGetterSetter(Object instanceUnderTest,
	// String fieldName,
	// Class<T> fieldType) {
	// createTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName,
	// fieldType)
	// .testGetterSetter();
	// }
	//
	// public static <T> void testObjectGetterSetter(Class<?> classUnderTest,
	// String staticFieldName,
	// Class<T> fieldType) {
	// createTester(classUnderTest, null, staticFieldName, fieldType)
	// .testGetterSetter();
	// }
	//
	// @SuppressWarnings("unchecked")
	// public static <T> DefaultGetterSetterTester<T> createTester(
	// Class<?> classUnderTest,
	// @Nullable Object instanceUnderTest,
	// String fieldName,
	// Class<T> fieldType) {
	// T instanceToTest = null;
	// try {
	// instanceToTest = fieldType.newInstance();
	// } catch (Exception e) {
	// try {
	// instanceToTest = mock(fieldType);
	// } catch (Exception e2) {
	// instanceToTest = null;
	// }
	// }
	// checkArgument(instanceToTest != null,
	// String.format("Unable to create instance of field type %s, one must be provided",
	// fieldType));
	//
	// return new DefaultGetterSetterTester<T>(classUnderTest,
	// instanceUnderTest,
	// fieldName,
	// fieldType,
	// filter(newArrayList(null, instanceToTest),
	// DefaultGetterSetterTester.<T> getNullableFilter(classUnderTest,
	// fieldName, fieldType)));
	// }

	@Override
	@Nonnull
	public Iterable<Class<?>> getMatchingTypes() {
		return ImmutableList.<Class<?>> of(Object.class);
	}

	@Override
	@Nonnull
	public DefaultGetterSetterTester<T> createTester(Class<?> classUnderTest, Object instanceUnderTest, String fieldName, Class<?> fieldType) {
		Object instanceToTest = null;
		try {
			instanceToTest = fieldType.newInstance();
		} catch (Exception e) {
			try {
				instanceToTest = mock(fieldType);
			} catch (Exception e2) {
				instanceToTest = null;
			}
		}
		checkArgument(instanceToTest != null, String.format("Unable to create instance of field type %s, one must be provided", fieldType));

		// TODO this is wrong!
		return new DefaultGetterSetterTester<T>(classUnderTest,
				instanceUnderTest,
				fieldName,
				(Class<? super T>) fieldType,
				filter(newArrayList(null, instanceToTest),
						DefaultGetterSetterTester.<Object> getNullableFilter(classUnderTest, fieldName, fieldType)));
	}

	@Override
	public void testField(Class<?> classUnderTest, Object instanceUnderTest, String fieldName, Class<?> fieldType) {
		createTester(classUnderTest, instanceUnderTest, fieldName, fieldType)
				.testGetterSetter();
	}
}
