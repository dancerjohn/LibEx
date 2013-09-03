package org.libex.test.gettersetter.primitive;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester;
import org.libex.test.gettersetter.TypedGetterSetterTester;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;

@ParametersAreNonnullByDefault
@ThreadSafe
public abstract class PrimitiveGetterSetterTester<WrapperType> implements TypedGetterSetterTester<WrapperType> {

	private final Class<? super WrapperType> primitiveType;
	private final Class<WrapperType> wrapperType;
	private final List<Class<?>> matchingTypes;
	private final List<WrapperType> values;

	protected PrimitiveGetterSetterTester(
			Class<? super WrapperType> primitiveType,
			Class<WrapperType> wrapperType,
			WrapperType... values) {
		super();
		this.primitiveType = primitiveType;
		this.wrapperType = wrapperType;
		this.values = newArrayList(values);
		this.matchingTypes = ImmutableList.<Class<?>> of(wrapperType, primitiveType);
	}

	public boolean isTestableType(Class<?> fieldType) {
		return matchingTypes.contains(fieldType);
	}

	@Override
	public Iterable<Class<?>> getMatchingTypes() {
		return matchingTypes;
	}

	@Override
	public DefaultGetterSetterTester<WrapperType> createTester(Class<?> classUnderTest, @Nullable Object instanceUnderTest, String fieldName,
			Class<?> fieldType) {
		checkArgument(isTestableType(fieldType), "fieldType is not one of Byte or Byte: " + fieldType);

		if (fieldType == primitiveType) {
			return createPrimitiveTester(classUnderTest, instanceUnderTest, fieldName);
		} else {
			return createWrapperTester(classUnderTest, instanceUnderTest, fieldName);
		}
	}

	@Override
	public void testField(Class<?> classUnderTest, @Nullable Object instanceUnderTest, String fieldName, Class<?> fieldType) {
		createTester(classUnderTest, instanceUnderTest, fieldName, fieldType)
				.testGetterSetter();
	}

	public void testPrimitiveGetterSetter(Object instanceUnderTest,
			String fieldName) {
		createPrimitiveTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName)
				.testGetterSetter();
	}

	public void testPrimitiveGetterSetter(Class<?> classUnderTest,
			String staticFieldName) {
		createPrimitiveTester(classUnderTest, null, staticFieldName)
				.testGetterSetter();
	}

	public DefaultGetterSetterTester<WrapperType> createPrimitiveTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<WrapperType>(classUnderTest,
				instanceUnderTest,
				fieldName,
				primitiveType,
				filter(values, Predicates.notNull()));
	}

	public void testWrapperGetterSetter(Object instanceUnderTest,
			String fieldName) {
		createWrapperTester(instanceUnderTest.getClass(), instanceUnderTest, fieldName)
				.testGetterSetter();
	}

	public void testWrapperGetterSetter(Class<?> classUnderTest,
			String staticFieldName) {
		createWrapperTester(classUnderTest, null, staticFieldName)
				.testGetterSetter();
	}

	public DefaultGetterSetterTester<WrapperType> createWrapperTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName) {
		return new DefaultGetterSetterTester<WrapperType>(classUnderTest,
				instanceUnderTest,
				fieldName,
				wrapperType,
				filter(values,
						DefaultGetterSetterTester.<WrapperType> getNullableFilter(classUnderTest, fieldName, wrapperType)));
	}
}
