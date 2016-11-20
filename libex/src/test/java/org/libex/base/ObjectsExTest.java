package org.libex.base;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;



import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.libex.test.TestUtilities;

import com.google.common.base.Function;

@RunWith(Theories.class)
public class ObjectsExTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@DataPoints
	public static String[] points = { "", null, "v", "sdf", "123" };

	@DataPoints
	public static Date[] dates = { new Date(), new Date(1234), null };

	@Theory
	public void testToStringObject(Object input) {
		String expected = (input == null) ? null : input.toString();

		assertThat(ObjectsEx.toString(input), equalTo(expected));
		assertThat(ObjectsEx.toStringFunction().apply(input), equalTo(expected));
	}

	@Theory
	public void testToStringOrDefault(Object input, String defaultValue) {
		if (defaultValue == null) {
			expectedException.expect(NullPointerException.class);
		}

		// test
		Function<Object, String> function = ObjectsEx.toStringOrDefault(defaultValue);

		// verify
		if (defaultValue != null) {
			String result = function.apply(input);
			String expected = (input == null) ? defaultValue : input.toString();
			assertThat(expected, notNullValue());
			assertThat(result, equalTo(expected));
		} else {
			Assert.fail();
		}
	}

	@Theory
	public void testToStringObjectString(Object input, String defaultValue) {
		if (defaultValue == null) {
			expectedException.expect(NullPointerException.class);
		}

		// test
		String result = ObjectsEx.toString(input, defaultValue);

		// verify
		if (defaultValue != null) {
			String expected = (input == null) ? defaultValue : input.toString();
			assertThat(expected, notNullValue());
			assertThat(result, equalTo(expected));
		} else {
			Assert.fail();
		}
	}

	@Test
	public void testConstructorForCoverage() throws Exception {
		TestUtilities.invokeDefaultConstructor(ObjectsEx.class);
	}
}
