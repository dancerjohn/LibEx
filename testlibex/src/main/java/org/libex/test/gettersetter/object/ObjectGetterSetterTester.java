package org.libex.test.gettersetter.object;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;
import org.libex.test.gettersetter.TypedGetterSetterTester;

import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ObjectGetterSetterTester<T> implements TypedGetterSetterTester<T> {

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
