package org.libex.test.gettersetter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.apache.log4j.Logger;
import org.junit.rules.ErrorCollector;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;

@ParametersAreNonnullByDefault
@ThreadSafe
@Setter
@Accessors(chain = true)
public class DefaultGetterSetterTester<T> {

	private static final Logger LOG = Logger.getLogger(DefaultGetterSetterTester.class);

	@Getter
	@Accessors(chain = true, fluent = true)
	public static class ProcessingArguments<T> {
		private final Class<?> classUnderTest;
		private final Object instanceUnderTest;
		private final Method getter;
		private final Method setter;
		@Nullable
		private final T valueBeingSet;

		private ProcessingArguments(
				@Nonnull DefaultGetterSetterTester<T> tester,
				@Nullable T valueBeingSet) {
			this.valueBeingSet = valueBeingSet;
			this.classUnderTest = tester.classUnderTest;
			this.instanceUnderTest = tester.instanceUnderTest;
			this.setter = tester.setter;
			this.getter = tester.getter;
		}
	}

	public static interface PreProcessor<T> {
		void preprocess(@Nonnull ProcessingArguments<T> args);
	}

	public static interface ExpectedValueSupplier<T> {
		@Nullable
		T transform(@Nonnull ProcessingArguments<T> args);
	}

	@Nonnull
	public static <T> Predicate<T> getNullableFilter(
			Class<?> classUnderTest,
			String fieldName,
			Class<?> fieldType) {
		Method setter = DefaultGetterSetterTester.findSetter(classUnderTest, fieldName, fieldType);
		Annotation[] annotations = setter.getParameterAnnotations()[0];
		boolean containsNullable = Iterables.any(newArrayList(annotations),
				Predicates.instanceOf(Nullable.class));

		return containsNullable ?
				Predicates.<T> alwaysTrue() :
				Predicates.<T> notNull();
	}

	private final Class<?> classUnderTest;

	@Nullable
	@Setter(AccessLevel.NONE)
	private Object instanceUnderTest;

	private final String fieldName;
	private final Class<? super T> fieldType;

	private final List<T> valuesToTest;

	@Nullable
	private Method getter = null;
	@Nullable
	private Method setter = null;

	@Nullable
	private PreProcessor<T> preProcessor = null;

	@Nullable
	private ExpectedValueSupplier<T> expectedValueSupplier = null;

	@Nullable
	private ErrorCollector errorCollector = null;

	public DefaultGetterSetterTester(
			Object instanceUnderTest,
			String fieldName,
			Class<? super T> fieldType,
			Iterable<?> valuesToTest) {
		this(instanceUnderTest.getClass(),
				instanceUnderTest,
				fieldName,
				fieldType,
				valuesToTest);
	}

	public DefaultGetterSetterTester(
			Class<?> classUnderTest,
			@Nullable Object instanceUnderTest,
			String fieldName,
			Class<? super T> fieldType,
			Iterable<?> valuesToTest) {
		checkArgument(!Strings.isNullOrEmpty(fieldName), "field name must be provided");
		checkArgument(valuesToTest != null && !isEmpty(valuesToTest), "at least one value to test must be provided");

		this.classUnderTest = Preconditions.checkNotNull(classUnderTest, "class under test must be provided");
		this.instanceUnderTest = instanceUnderTest;
		this.fieldName = fieldName;
		this.fieldType = Preconditions.checkNotNull(fieldType, "field type must be provided");
		this.valuesToTest = toList(valuesToTest);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> toList(Iterable<?> values) {
		List<T> result = newArrayList();
		for (Object t : values) {
			result.add((T) t);
		}
		return result;
	}

	public void testGetterSetter() {
		if (!preconditionsMet()) {
			return;
		}

		checkValues();
	}

	protected boolean preconditionsMet() {
		try {
			checkConditionsMet();
			return true;
		} catch (Exception e) {
			if (errorCollector != null) {
				errorCollector.addError(e);
			} else {
				throw Throwables.propagate(e);
			}
			return false;
		}
	}

	private void checkConditionsMet() {
		getter = findGetter();
		setter = findSetter();

		checkIfInstanceNeeded();
	}

	@Nonnull
	private Method findGetter() {
		Method getter = this.getter;
		if (getter == null) {
			return findGetter(classUnderTest, fieldName, fieldType);
		}
		return getter;
	}

	@Nonnull
	public static Method findGetter(
			Class<?> classUnderTest,
			String fieldName,
			Class<?> fieldType) {
		List<String> prefixes = newArrayList("", "get", "is");
		return findFieldMethod(classUnderTest, fieldName, "getter", prefixes, fieldType);
	}

	@Nonnull
	private Method findSetter() {
		if (setter == null) {
			return findSetter(classUnderTest, fieldName, fieldType);
		} else {
			return setter;
		}
	}

	@Nonnull
	public static Method findSetter(
			Class<?> classUnderTest,
			String fieldName,
			Class<?> fieldType) {
		List<String> prefixes = newArrayList("", "set", "with");
		return findFieldMethod(classUnderTest, fieldName, "setter", prefixes, null, fieldType);
	}

	@Nonnull
	public static Method findFieldMethod(
			Class<?> classUnderTest,
			String fieldName,
			String methodType,
			Iterable<String> prefixes,
			Class<?> returnType,
			Class<?>... types) {
		List<String> attempts = newArrayList();
		String upperCaseName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		for (String prefix : prefixes) {
			String nameToCheck = (Strings.isNullOrEmpty(prefix)) ? fieldName : prefix + upperCaseName;

			Method getterMethod = null;
			attempts.add(nameToCheck);
			getterMethod = findMethod(classUnderTest, nameToCheck, returnType, types);

			if (getterMethod != null) {
				return getterMethod;
			}
		}

		throw new AssertionError(String.format("No %s method found for \"%s\" in %s with return type of %s and parameter types of %s, tried %s",
				methodType, fieldName, classUnderTest,
				Objects.firstNonNull(returnType, "<undefined>"), Arrays.toString(types), attempts));
	}

	public static Method findMethod(
			Class<?> classUnderTest,
			String name,
			Class<?> returnType,
			Class<?>... types) {
		try {
			Method result = classUnderTest.getMethod(name, types);
			if (result != null && returnType != null) {
				Class<?> actualReturnType = result.getReturnType();
				if (!returnType.isAssignableFrom(actualReturnType)) {
					result = null;
				}
			}
			return result;
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			throw Throwables.propagate(e);
		}
	}

	private void checkIfInstanceNeeded() {
		if (instanceUnderTest == null) {
			boolean instanceNeeded = false;

			int modifier = getter.getModifiers();
			instanceNeeded |= !Modifier.isStatic(modifier);

			modifier = setter.getModifiers();
			instanceNeeded |= !Modifier.isStatic(modifier);

			if (instanceNeeded) {
				try {
					instanceUnderTest = classUnderTest.newInstance();
				} catch (Exception e) {
					instanceUnderTest = null;
				}
				if (instanceUnderTest == null) {
					throw new AssertionError(
							"getter or setter is not static and no default constructor found, so instance must be provided");
				}
			}
		}
	}

	protected void checkValues() {
		for (T value : valuesToTest) {
			try {
				checkValue(value);
			} catch (Exception e) {
				if (errorCollector != null) {
					errorCollector.addError(e);
				} else {
					throw Throwables.propagate(e);
				}
			}
		}
	}

	private void checkValue(T input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ProcessingArguments<T> args = new ProcessingArguments<T>(this, input);
		preInvokeWith(args);

		T preValue = invokeGetter();
		assertThat(String.format("values are equal before invoking setter with \"%s\"", input),
				input, not(equalTo(preValue)));

		Object setterReturn = invokeSetter(input);
		if (setterReturn != null) {
			assertThat("value returned from setter is not same instance as instance under test",
					setterReturn, sameInstance(instanceUnderTest));
		}

		T postValue = invokeGetter();
		T expectedValue = getExpectedFrom(args);
		assertThat(String.format("values are not equal after invoking setter with \"%s\"", input),
				postValue, equalTo(expectedValue));
	}

	private void preInvokeWith(ProcessingArguments<T> input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (preProcessor != null) {
			preProcessor.preprocess(input);
		} else {
			setToNonnullOnNull(input);
		}
	}

	private void setToNonnullOnNull(ProcessingArguments<T> input) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (input.valueBeingSet == null) {
			T currentValue = invokeGetter();
			if (currentValue == null) {
				T firstNonNull = Iterables.get(
						filter(valuesToTest, Predicates.notNull()), 0);

				assertThat("when attempting to test null value, current value is null. At least one value to test must be non-null",
						firstNonNull, notNullValue());

				invokeSetter(firstNonNull);
			}
		}
	}

	private T getExpectedFrom(ProcessingArguments<T> input) {
		if (expectedValueSupplier != null) {
			return expectedValueSupplier.transform(input);
		} else {
			return input.valueBeingSet;
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private T invokeGetter() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (T) getter.invoke(instanceUnderTest);
	}

	@Nullable
	private Object invokeSetter(T value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		LOG.trace(String.format("Invoking setter %s with \"%s\"", setter, value));
		return setter.invoke(instanceUnderTest, value);
	}

	@Nonnull
	public DefaultGetterSetterTester<T> setPreProcessor(
			@Nullable PreProcessor<T> preProcessor) {
		checkState(this.preProcessor == null, "preProcesssor already set");

		this.preProcessor = preProcessor;
		return this;
	}

	@Nonnull
	public DefaultGetterSetterTester<T> setExpectedValueSupplier(
			@Nullable ExpectedValueSupplier<T> expectedValueSupplier) {
		checkState(this.expectedValueSupplier == null, "expectedValueSupplier already set");

		this.expectedValueSupplier = expectedValueSupplier;
		return this;
	}

}
