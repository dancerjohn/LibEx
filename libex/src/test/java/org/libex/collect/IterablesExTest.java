package org.libex.collect;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.libex.collect.IterablesEx.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.libex.hamcrest.IsOptional;
import org.libex.test.TestBase;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

@RunWith(Enclosed.class)
public class IterablesExTest extends TestBase {

	public static class FindFirstTest extends TestBase {

		private static final Optional<Integer> result = Optional.of(5);
		private static final Optional<Integer> result2 = Optional.of(6);
		private Function<Object, Optional<Integer>> absent1 = Functions.constant(Optional.<Integer> absent());
		private Function<Object, Optional<Integer>> absent2 = Functions.constant(Optional.<Integer> absent());
		private Function<Object, Optional<Integer>> present1 = Functions.constant(result);
		private Function<Object, Optional<Integer>> absent3 = Functions.constant(Optional.<Integer> absent());
		private Function<Object, Optional<Integer>> present2 = Functions.constant(result2);

		@Before
		public void setUp() throws Exception {
		}

		@Test
		public void testFindFirst_nulls() {
			nullPointerTester.testAllPublicStaticMethods(IterablesEx.class);
		}

		@Test
		public void testFindFirst_nullArg() {
			// test
			@SuppressWarnings("unchecked")
			Optional<Integer> result = findFirst(null, newArrayList(present1, absent3));

			// verify
			assertThat(result, sameInstance(FindFirstTest.result));
		}

		@Test
		public void testFindFirst_emptyList() {
			// test
			Optional<Integer> result = findFirst("", Lists.<Function<Object, Optional<Integer>>> newArrayList());

			// verify
			assertThat(result, IsOptional.absent(Integer.class));
		}

		@Test
		public void testFindFirst_notFound() {
			// test
			@SuppressWarnings("unchecked")
			Optional<Integer> result = findFirst("", newArrayList(absent1, absent2, absent3));

			// verify
			assertThat(result, IsOptional.absent(Integer.class));
		}

		@Test
		public void testFindFirst_found() {
			// test
			@SuppressWarnings("unchecked")
			Optional<Integer> result = findFirst("", newArrayList(absent1, absent2, present1, absent3, present2));

			// verify
			assertThat(result, sameInstance(FindFirstTest.result));
		}
	}
}
