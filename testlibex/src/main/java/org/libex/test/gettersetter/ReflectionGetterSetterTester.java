package org.libex.test.gettersetter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.log4j.Logger;
import org.junit.rules.ErrorCollector;
import org.libex.reflect.ReflectionUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ReflectionGetterSetterTester {

	public static void testAllGetterSetters(
			Object instanceUnderTest,
			@Nullable ErrorCollector errorCollector,
			String... excludes) {
		testAllGetterSetters(instanceUnderTest.getClass(), Object.class, instanceUnderTest, errorCollector, excludes);
	}

	public static void testAllGetterSetters(
			Class<?> classUnderTest,
			@Nullable ErrorCollector errorCollector,
			String... excludes) {
		testAllGetterSetters(classUnderTest, Object.class, null, errorCollector, excludes);
	}

	public static void testAllGetterSetters(
			Class<?> classUnderTest,
			@Nullable Class<?> exclusiveSuperClass,
			@Nullable Object instanceUnderTest,
			@Nullable ErrorCollector errorCollector,
			String... excludes) {
		new ReflectionGetterSetterTester(classUnderTest,
				Objects.firstNonNull(exclusiveSuperClass, Object.class),
				instanceUnderTest, errorCollector, excludes)
				.testAllGettersAndSetters();
	}

	private static final Logger LOG = Logger.getLogger(ReflectionGetterSetterTester.class);

	private final Class<?> classUnderTest;
	private final Class<?> exclusiveSuperClass;
	@Nullable
	private final Object instanceUnderTest;
	private final ErrorCollector errorCollector;
	private final Set<String> excludes;

	protected ReflectionGetterSetterTester(
			Class<?> classUnderTest,
			Class<?> exclusiveSuperClass,
			@Nullable Object instanceUnderTest,
			@Nullable ErrorCollector errorCollector,
			String[] excludes) {
		this.classUnderTest = classUnderTest;
		this.exclusiveSuperClass = exclusiveSuperClass;
		this.instanceUnderTest = instanceUnderTest;
		this.errorCollector = errorCollector;
		this.excludes = Sets.newHashSet(excludes);
	}

	protected void testAllGettersAndSetters() {
		Iterable<Field> fields = ReflectionUtils.getFieldsUpTo(classUnderTest, exclusiveSuperClass);
		for (Field field : fields) {
			testField(field);
		}
	}

	protected void testField(Field field) {
		if (!Modifier.isFinal(field.getModifiers())) {

			String fieldName = field.getName();
			if (!excludes.contains(fieldName)) {
				LOG.debug("testing field " + field);
				DefaultGetterSetterTester<?> tester = GetterSetterTester.createTester(classUnderTest, instanceUnderTest, fieldName, field.getType());
				tester = setupTester(tester);
				tester.testGetterSetter();
			} else {
				LOG.debug("skipping excluded field " + field);
			}
		} else {
			LOG.debug("skipping private field " + field);
		}
	}

	protected DefaultGetterSetterTester<?> setupTester(DefaultGetterSetterTester<?> tester) {
		return tester.setErrorCollector(errorCollector);
	}

	// TODO test all error conditions, multiple Methods found, Builder pattern
}
