package org.libex.base;

import static com.google.common.collect.Iterables.*;
import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Date;
import java.util.List;

import org.hamcrest.collection.IsIn;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;

import com.google.common.base.Function;

@RunWith(Enclosed.class)
public class StringsExTest {

	public static class MainTests extends TestBase {
		@Before
		public void setUp() throws Exception {
		}

		@After
		public void tearDown() throws Exception {
		}

		@Test
		public void testNulls() {
			nullPointerTester.testAllPublicStaticMethods(StringsEx.class);
		}
	}

	public static class IsNullOrEmptyTest extends TestBase {

		private static final List<String> input = newArrayList("", null, "1", "flsfdjf");
		private static final List<String> result = newArrayList("", null);

		@Test
		public void testIsNullOrEmpty() {
			// test
			Iterable<String> resultSet = filter(input, StringsEx.isNullOrEmpty());

			// verify
			assertThat(resultSet, IsIterableContainingInOrder.contains(result.toArray()));
		}
	}

	@RunWith(Theories.class)
	public static class FormatterStringTests extends TestBase {

		@DataPoints
		public static String[] invalidFormats = { "%s blah %s", "%d" };

		@DataPoints
		public static String[] validFormats = { "", "%s", "blah %s" };

		public static String[] valuesToTest = { null, "", "blah" };

		@Theory
		public void testFormatterString_valid(String format) {
			// assumptions
			Assume.assumeThat(format, IsIn.isIn(validFormats));

			// test
			Function<Object, String> formatter = StringsEx.formatter(format);

			// verify
			for (String value : valuesToTest) {
				assertThat(formatter.apply(value), equalTo(String.format(format, value)));
			}
		}

		@Theory
		public void testFormatterString_invalid(String format) {
			// assumptions
			Assume.assumeThat(format, IsIn.isIn(invalidFormats));

			// expect
			expectedException.expect(RuntimeException.class);

			// test
			Function<Object, String> formatter = StringsEx.formatter(format);

			// verify
			formatter.apply("blah");
		}
	}

	@RunWith(Theories.class)
	public static class FormatterStringClassTests extends TestBase {

		@DataPoints
		public static String[] invalidFormats = { "%s blah %s", "%d" };

		@DataPoints
		public static String[] validFormats = { "", "%s", "blah %s" };

		public static String[] valuesToTest = { null, "", "blah" };

		@Test
		public void testFormatMismatch() {
			// test
			StringsEx.formatter("%d", Date.class);
		}

		@Theory
		public void testFormatterString_invalid(String format) {
			// assumptions
			Assume.assumeThat(format, IsIn.isIn(invalidFormats));

			// expect
			expectedException.expect(RuntimeException.class);

			// test
			Function<String, String> formatter = StringsEx.formatter(format, String.class);

			// verify
			formatter.apply("blah");
		}

		@Test
		public void testFormatSubclass() {
			// test
			Function<Number, String> formatter = StringsEx.formatter("%d", Number.class);

			// verify
			Integer value = 5;
			assertThat(formatter.apply(value), equalTo(String.format("%d", value)));
		}

		@Theory
		public void testFormatterString_valid(String format) {
			// assumptions
			Assume.assumeThat(format, IsIn.isIn(validFormats));

			// test
			Function<String, String> formatter = StringsEx.formatter(format, String.class);

			// verify
			for (String value : valuesToTest) {
				assertThat(formatter.apply(value), equalTo(String.format(format, value)));
			}
		}
	}

	@RunWith(Theories.class)
	public static class TrimTests extends TestBase {

		@DataPoints
		public static String[] validFormats = { "", null, " ", " b lah " };

		@Theory
		public void testTrim(String format) {
			// test
			String result = StringsEx.trim(format);

			// verify
			assertThat(result, equalTo((format == null) ? null : format.trim()));
		}
	}
}
