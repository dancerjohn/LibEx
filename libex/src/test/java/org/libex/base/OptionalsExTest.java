package org.libex.base;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestUtilities;

import com.google.common.base.Optional;

@RunWith(Theories.class)
public class OptionalsExTest {

	@DataPoint
	public static Optional<String> present = Optional.of("");

	@DataPoint
	public static Optional<String> absent = Optional.absent();

	@DataPoints
	public static String[] strings = { "", null };

	@Theory
	public void testFromNullable(String input) {
		Optional<Object> optional = OptionalsEx.<Object> fromNullable().apply(input);
		assertThat(optional.orNull(), equalTo((Object) input));

		Optional<String> optionalSt = OptionalsEx.<String> fromNullable().apply(input);
		assertThat(optionalSt.orNull(), equalTo(input));
	}

	@Theory
	public void testToValueOrNull(Optional<String> optional) {
		Object value = OptionalsEx.<Object> toValueOrNull().apply(optional);
		assertThat(value, equalTo((Object) optional.orNull()));
	}

	@Theory
	public void testToValueOr(Optional<String> optional) {
		String value = OptionalsEx.toValueOr("default").apply(optional);
		assertThat(value, equalTo(optional.or("default")));
	}

	@Test
	public void testConstructorForCoverage() throws Exception {
		TestUtilities.invokeDefaultConstructor(OptionalsEx.class);
	}
}
