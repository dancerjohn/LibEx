package org.libex.test.gettersetter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public interface TypedGetterSetterTester<T> {

	static final Function<TypedGetterSetterTester<?>, Iterable<Class<?>>> toMatchingTypes =
			new Function<TypedGetterSetterTester<?>, Iterable<Class<?>>>() {

				@Override
				@Nonnull
				public Iterable<Class<?>> apply(@Nonnull TypedGetterSetterTester<?> input) {
					return input.getMatchingTypes();
				}
			};

	@Nonnull
	Iterable<Class<?>> getMatchingTypes();

	@Nonnull
	DefaultGetterSetterTester<T> createTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName,
			Class<?> fieldType);

	void testField(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName,
			Class<?> fieldType);
}
