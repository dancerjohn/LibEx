package org.libex.reflect;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.libex.test.TestBase;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

@RunWith(Enclosed.class)
public class ReflectionUtilsTest {

	public static class BaseTests extends TestBase {

		@Test
		public void testNulls() {
			nullPointerTester.testAllPublicStaticMethods(ReflectionUtils.class);
		}
	}

	@RunWith(Parameterized.class)
	public static class ContainsAnnotation extends TestBase {

		private static final Predicate<Method> nonnullPredicate = ReflectionUtils.containsAnnotation(Nonnull.class);

		@Parameters
		public static Collection<Object[]> getData() {
			List<Object[]> result = newArrayList();
			for (Method method : ReflectionUtils.class.getDeclaredMethods()) {
				result.add(new Object[] { method, method.getAnnotation(Nonnull.class) != null });
			}
			return result;
		}

		private final Method method;
		private final boolean result;

		public ContainsAnnotation(Method method, boolean result) {
			super();
			this.method = method;
			this.result = result;
		}

		@Test
		public void testContainsAnnotation() {
			// test
			boolean value = nonnullPredicate.apply(method);

			// verify
			assertThat(method.getName(), value, is(result));
		}
	}

	@RunWith(Parameterized.class)
	public static class ContainsAllAnnotations extends TestBase {

		@Parameters
		public static Collection<Object[]> getData() {
			List<Object[]> result = newArrayList();
			for (Method method : ReflectionUtils.class.getDeclaredMethods()) {
				result.add(new Object[] { method,
						method.getAnnotation(Nonnull.class) != null,
						method.getAnnotation(Deprecated.class) != null,
						method.getAnnotation(Nullable.class) != null });
			}
			return result;
		}

		private final Method method;
		private final boolean containsNonnull;
		private final boolean containsDeprecated;
		private final boolean containsNullable;

		public ContainsAllAnnotations(Method method, boolean containsNonnull, boolean containsDeprecated, boolean containsNullable) {
			super();
			this.method = method;
			this.containsNonnull = containsNonnull;
			this.containsDeprecated = containsDeprecated;
			this.containsNullable = containsNullable;
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testContainsAnnotation_nonnull() {
			testContainsAllAnnotation(containsNonnull, Nonnull.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testContainsAnnotation_nullable() {
			testContainsAllAnnotation(containsNullable, Nullable.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testContainsAnnotation_deprecetated() {
			testContainsAllAnnotation(containsDeprecated, Deprecated.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testContainsAnnotation_nonnullAndDeprecated() {
			testContainsAllAnnotation(containsNonnull && containsDeprecated,
					Nonnull.class, Deprecated.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testContainsAnnotation_allThree() {
			testContainsAllAnnotation(containsNonnull && containsNullable && containsDeprecated,
					Nonnull.class, Nullable.class, Deprecated.class);
		}

		private void testContainsAllAnnotation(
				boolean expected, Class<? extends Annotation>... annotations) {
			// setup
			Predicate<Method> nonnullPredicate = ReflectionUtils.containsAllAnnotations(annotations);

			// test
			boolean value = nonnullPredicate.apply(method);

			// verify
			assertThat(method.getName(), value, is(expected));
		}
	}

	@SuppressWarnings("unused")
	public static class GetMethodsWithAnnotations extends TestBase {

		private Method deprecatedAndNonnull;
		private Method deprecated;
		private Method nonnull;
		private Method nullable;
		private Method privateNullable;
		private Method protectedNullable;
		private Method noAnnotations;

		@Before
		public void setUp() throws Exception {
			deprecatedAndNonnull = ReflectionUtilsExample.class.getDeclaredMethod("deprecatedAndNonnull");
			deprecated = ReflectionUtilsExample.class.getDeclaredMethod("deprecated");
			nonnull = ReflectionUtilsExample.class.getDeclaredMethod("nonnull");
			nullable = ReflectionUtilsExample.class.getDeclaredMethod("nullable");
			privateNullable = ReflectionUtilsExample.class.getDeclaredMethod("privateNullable");
			protectedNullable = ReflectionUtilsExample.class.getDeclaredMethod("protectedNullable");
			noAnnotations = ReflectionUtilsExample.class.getDeclaredMethod("noAnnotations");
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testNonnull() {
			assertGetMethodsWithAnnotations(
					newArrayList(deprecatedAndNonnull, nonnull),
					Nonnull.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testDeprecated() {
			assertGetMethodsWithAnnotations(
					newArrayList(deprecatedAndNonnull, deprecated),
					Deprecated.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testNonnullDeprecated() {
			assertGetMethodsWithAnnotations(
					newArrayList(deprecatedAndNonnull),
					Nonnull.class, Deprecated.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testNonnullNonnull() {
			assertGetMethodsWithAnnotations(
					Lists.<Method> newArrayList(),
					Nonnull.class, Nullable.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testNullable() {
			assertGetMethodsWithAnnotations(
					newArrayList(nullable),
					Nullable.class);
		}

		@SuppressWarnings("unchecked")
		@Test
		@Ignore
		// gets all Object methods as well
		public void testNoAnnotations() {
			assertGetMethodsWithAnnotations(newArrayList(
					deprecatedAndNonnull,
					deprecated,
					nonnull,
					nullable,
					noAnnotations));
		}

		private void assertGetMethodsWithAnnotations(List<Method> methods, Class<? extends Annotation>... annotations) {
			// test
			List<Method> result = ReflectionUtils.getMethodsWithAnnotations(ReflectionUtilsExample.class, annotations);

			// verify
			assertThat(result, IsIterableContainingInAnyOrder.containsInAnyOrder(methods.toArray()));
		}
	}

}
