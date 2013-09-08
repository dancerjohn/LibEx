package org.libex.test.gettersetter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.rules.ErrorCollector;

import com.google.common.base.Objects;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ReflectionBuilderGetterSetterTester extends ReflectionGetterSetterTester {

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
		new ReflectionBuilderGetterSetterTester(classUnderTest,
				Objects.firstNonNull(exclusiveSuperClass, Object.class),
				instanceUnderTest, errorCollector, excludes)
				.testAllGettersAndSetters();
	}

	protected ReflectionBuilderGetterSetterTester(
			Class<?> classUnderTest,
			Class<?> exclusiveSuperClass,
			@Nullable Object instanceUnderTest,
			@Nullable ErrorCollector errorCollector,
			String[] excludes) {
		super(classUnderTest, exclusiveSuperClass, instanceUnderTest, errorCollector, excludes);
	}

	@Override
	protected DefaultGetterSetterTester<?> setupTester(DefaultGetterSetterTester<?> tester) {
		tester = super.setupTester(tester);
		return BuilderGetterInvoker.builderTester(tester);
	}
}
