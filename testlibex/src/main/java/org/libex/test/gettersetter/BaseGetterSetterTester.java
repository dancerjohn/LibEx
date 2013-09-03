package org.libex.test.gettersetter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.Lists;

@ParametersAreNonnullByDefault
@ThreadSafe
public abstract class BaseGetterSetterTester<T> implements TypedGetterSetterTester<T> {

	private final Class<T> fieldType;
	private final List<T> values;

	protected BaseGetterSetterTester(Class<T> fieldType, List<T> values) {
		super();
		this.fieldType = fieldType;
		this.values = values;
	}

	public boolean isTestableType(Class<?> fieldType) {
		return this.fieldType == fieldType;
	}

	@Override
	public Iterable<Class<?>> getMatchingTypes() {
		return Lists.<Class<?>> newArrayList(fieldType);
	}

	@Override
	public DefaultGetterSetterTester<T> createTester(Class<?> classUnderTest, @Nullable Object instanceUnderTest, String fieldName, Class<?> fieldType) {
		checkArgument(isTestableType(fieldType), "fieldType is not of type: " + fieldType);

		return createTester(classUnderTest, instanceUnderTest, fieldName);
	}

	@Override
	public void testField(Class<?> classUnderTest, @Nullable Object instanceUnderTest, String fieldName, Class<?> fieldType) {
		createTester(classUnderTest, instanceUnderTest, fieldName, fieldType)
				.testGetterSetter();
	}

	public void testGetterSetter(Object instanceUnderTest,
			String fieldName) {
		createTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName)
				.testGetterSetter();
	}

	public void testGetterSetter(Class<?> classUnderTest,
			String staticFieldName) {
		createTester(classUnderTest, null, staticFieldName)
				.testGetterSetter();
	}

	public DefaultGetterSetterTester<T> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<T>(classUnderTest,
				instanceUnderTest,
				fieldName,
				fieldType,
				filter(values,
						DefaultGetterSetterTester.<T> getNullableFilter(classUnderTest, fieldName, fieldType)));
	}
}
