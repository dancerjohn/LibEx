package org.libex.collect;

import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@RunWith(Enclosed.class)
public class MapsExTest {

	public static class GetOrInsert extends TestBase {
		private static final String DEFAULT_VALUE = "default value";
		private static final Date date1 = new Date();
		private static final Date date2 = new Date(1231213L);

		@Mock
		Supplier<String> supplier;

		@Before
		public void setup() {
			MockitoAnnotations.initMocks(this);
			when(supplier.get()).thenReturn(DEFAULT_VALUE);
		}

		@Test
		public void testNulls() {
			nullPointerTester.testAllPublicStaticMethods(MapsEx.class);
		}

		@Test
		public void testWhenMapEmpty() {
			// setup
			Map<Date, String> map = newHashMap();
			assertThat(map, not(IsMapContaining.hasKey(date1)));

			// test
			String result = MapsEx.getOrInsert(map, date1, supplier);

			// verify
			assertThat(result, equalTo(DEFAULT_VALUE));
			assertThat(map, IsMapContaining.hasEntry(date1, DEFAULT_VALUE));
		}

		@Test
		public void testWhenMapNotEmpty() {
			// setup
			Map<Date, String> map = newHashMap();
			map.put(date2, "something else");
			assertThat(map, not(IsMapContaining.hasKey(date1)));

			// test
			String result = MapsEx.getOrInsert(map, date1, supplier);

			// verify
			assertThat(result, equalTo(DEFAULT_VALUE));
			assertThat(map, IsMapContaining.hasEntry(date1, DEFAULT_VALUE));
			assertThat(map, IsMapContaining.hasEntry(date2, "something else"));
		}

		@Test
		public void testWhenMapContainsResult() {
			// setup
			Map<Date, String> map = newHashMap();
			map.put(date1, "something else");

			// test
			String result = MapsEx.getOrInsert(map, date1, supplier);

			// verify
			assertThat(result, equalTo("something else"));
			assertThat(map, IsMapContaining.hasEntry(date1, "something else"));
			verifyNoMoreInteractions(supplier);
		}

	}

	@RunWith(Theories.class)
	public static class UniqueIndex {

		Function<Integer, String> keyFunction = new Function<Integer, String>() {

			@Override
			@Nullable
			public String apply(@Nullable Integer input) {
				return Integer.toString(input);
			}
		};

		Function<Integer, Date> valueFunction = new Function<Integer, Date>() {

			@Override
			@Nullable
			public Date apply(@Nullable Integer input) {
				return new Date(input);
			}
		};

		@DataPoint
		public static List<Integer> emtpyList = newArrayList();

		@DataPoint
		public static List<Integer> singleList = newArrayList(5);

		@DataPoint
		public static List<Integer> multipleList = newArrayList(4, 7, 3, 5, 2);

		@DataPoint
		public static List<Integer> duplicateInList = newArrayList(4, 7, 3, 5, 7, 2);

		@Theory
		public void testDefault(List<Integer> input) {
			// test
			Map<String, Date> result = MapsEx.uniqueIndex(input, keyFunction, valueFunction);

			// verify
			Set<String> keys = Sets.newLinkedHashSet(transform(input, keyFunction));
			assertThat("key set", result.keySet(), IsIterableContainingInAnyOrder.containsInAnyOrder(keys.toArray()));

			Map<String, Date> expected = newHashMap();
			for (Integer i : input) {
				expected.put(keyFunction.apply(i), valueFunction.apply(i));
			}

			for (Entry<String, Date> entry : expected.entrySet()) {
				assertThat(result, IsMapContaining.hasEntry(entry.getKey(), entry.getValue()));
			}
		}

		@Test
		public void keyFunctionReturnsNull() {
			// setup
			Function<Object, String> nullFunction = Functions.constant((String) null);

			// test
			Map<String, Date> result = MapsEx.uniqueIndex(multipleList, nullFunction, valueFunction);

			// verify
			assertThat("size", result.size(), is(1));
			assertThat(result.get(null), equalTo(valueFunction.apply(Iterables.getLast(multipleList))));
		}
	}

	@RunWith(Theories.class)
	public static class MultipleIndex extends TestBase {

		Function<Integer, List<String>> keyFunction = new Function<Integer, List<String>>() {

			@Override
			@Nullable
			public List<String> apply(@Nullable Integer input) {
				return newArrayList(Integer.toString(input), Integer.toString(input * 2));
			}
		};

		Function<Integer, Date> valueFunction = new Function<Integer, Date>() {

			@Override
			@Nullable
			public Date apply(@Nullable Integer input) {
				return new Date(input);
			}
		};

		@DataPoint
		public static List<Integer> emtpyList = newArrayList();

		@DataPoint
		public static List<Integer> singleList = newArrayList(5);

		@DataPoint
		public static List<Integer> multipleList = newArrayList(4, 7, 3, 5, 2);

		@DataPoint
		public static List<Integer> duplicateInList = newArrayList(4, 7, 3, 5, 7, 2);

		@Theory
		public void testDefault(List<Integer> input) {
			// test
			Map<String, Date> result = MapsEx.multipleIndex(input, keyFunction, valueFunction);

			// verify
			Set<String> keys = Sets.newLinkedHashSet();
			for (Integer i : input) {
				keys.addAll(keyFunction.apply(i));
			}
			assertThat("key set", result.keySet(), IsIterableContainingInAnyOrder.containsInAnyOrder(keys.toArray()));

			Map<String, Date> expected = newHashMap();
			for (Integer i : input) {
				Date value = valueFunction.apply(i);
				for (String key : keyFunction.apply(i)) {
					expected.put(key, value);
				}
			}

			for (Entry<String, Date> entry : expected.entrySet()) {
				assertThat(result, IsMapContaining.hasEntry(entry.getKey(), entry.getValue()));
			}
		}

		@Test
		public void keyFunctionReturnsNull() {
			// setup
			Function<Object, List<String>> nullFunction = Functions.constant((List<String>) null);

			// expect
			expectedException.expect(NullPointerException.class);

			// test
			MapsEx.multipleIndex(multipleList, nullFunction, valueFunction);
		}
	}
}
