package org.libex.test.gettersetter.old;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Setter;
import lombok.experimental.Accessors;

import org.hamcrest.Matcher;
import org.junit.rules.ErrorCollector;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@ThreadSafe
@Setter
@Accessors(chain = true, fluent = true)
public class BaseGetterSetterTester {

	private Class<?> classUnderTest;
	private String fieldName;
	private Class<?> fieldType;

	private Method getter;
	private Method setter;
	private Object instanceUnderTest;
	private Function<Object, Object> expectedTransformer = Functions.identity();
	private Object[] valuesToTest;
	private Object setBeforeNullValue;
	private ErrorCollector errorCollector;

	public void testGetterSetter() {
		try {
			checkConditionsMet();
		} catch (Exception e) {
			if (errorCollector != null) {
				errorCollector.addError(e);
			} else {
				throw Throwables.propagate(e);
			}
			return;
		}

		for (Object value : valuesToTest) {
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

	private void checkValue(Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (value == null && setBeforeNullValue != null) {
			setter.invoke(instanceUnderTest, setBeforeNullValue);
		}

		Object preValue = invokeGetter();
		assertThat("values are equal before invoking setter with " + value,
				value, not(equalTo(preValue)));

		Object setterReturn = setter.invoke(instanceUnderTest, value);
		if (setterReturn != null) {
			assertThat("value returned from setter is not same instance as instance under test",
					setterReturn, sameInstance(instanceUnderTest));
		}

		Object postValue = invokeGetter();
		assertThat("values are not equal after invoking setter with " + value,
				value, equalTo(postValue));
	}

	@Nullable
	private Object invokeGetter() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object result = getter.invoke(instanceUnderTest);
		result = expectedTransformer.apply(result);
		return result;
	}

	private void checkConditionsMet() {
		if (classUnderTest == null && instanceUnderTest == null) {
			throw new AssertionError("class under test or instance under test must be provided");
		}

		if (classUnderTest == null && instanceUnderTest != null) {
			classUnderTest = instanceUnderTest.getClass();
		}

		if (Strings.isNullOrEmpty(fieldName)) {
			throw new AssertionError("field name to test must be supplied");
		}

		if (valuesToTest == null || valuesToTest.length == 0) {
			throw new AssertionError("there must be at least one value to test");
		}

		checkNotNull(fieldType, "field type must be supplied");

		getter = findGetter();
		setter = findSetter();

		checkIfInstanceNeeded();
	}

	private void checkNotNull(Object object, String format, Object... objects) {
		checkThat(object, notNullValue(), format, objects);
	}

	private void checkThat(Object object,
			@SuppressWarnings("rawtypes") Matcher matcher,
			String format, Object... objects) {
		if (!matcher.matches(object)) {
			throw new AssertionError(String.format(format, objects));
		}
	}

	@Nonnull
	private Method findGetter() {
		Method getter = this.getter;
		if (getter == null) {
			List<String> prefixes = newArrayList("", "get", "is");
			return findFieldMethod("getter", prefixes, fieldType);
		}
		return getter;
	}

	@Nonnull
	private Method findSetter() {
		if (setter == null) {
			List<String> prefixes = newArrayList("", "set", "with");
			return findFieldMethod("setter", prefixes, null, fieldType);
		} else {
			return setter;
		}
	}

	@Nonnull
	private Method findFieldMethod(String methodType, Iterable<String> prefixes,
			Class<?> returnType, Class<?>... types) {
		List<String> attempts = newArrayList();
		String upperCaseName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		for (String prefix : prefixes) {
			String nameToCheck = (Strings.isNullOrEmpty(prefix)) ? fieldName : prefix + upperCaseName;

			Method getterMethod = null;
			attempts.add(nameToCheck);
			getterMethod = findMethod(nameToCheck, returnType, types);

			if (getterMethod != null) {
				return getterMethod;
			}
		}

		throw new AssertionError(String.format("No %s method found for \"%s\", tried %s",
				methodType, fieldName, attempts));
	}

	private Method findMethod(String name, Class<?> returnType, Class<?>... types) {
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
		boolean instanceNeeded = false;

		int modifier = getter.getModifiers();
		instanceNeeded |= !Modifier.isStatic(modifier);

		modifier = setter.getModifiers();
		instanceNeeded |= !Modifier.isStatic(modifier);

		if (instanceNeeded) {
			if (instanceUnderTest == null) {
				try {
					instanceUnderTest = classUnderTest.newInstance();
				} catch (Exception e) {
					instanceUnderTest = null;
				}
			}

			checkNotNull(instanceUnderTest,
					"getter or setter is not static and no default constructor found, so instance must be provided");
		}
	}
}
