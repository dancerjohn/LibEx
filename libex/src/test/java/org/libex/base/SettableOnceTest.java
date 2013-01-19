package org.libex.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.libex.hamcrest.IsOptional;
import org.libex.test.TestBase;

import com.google.common.base.Optional;
import com.google.common.testing.NullPointerTester.Visibility;

public class SettableOnceTest extends TestBase {

	public static final String VALUE = "some test value";

	private SettableOnce<String> settable;

	protected SettableOnce<String> createSettableWith(String name, String message) {
		if (name != null) {
			return SettableOnce.withName(name);
		} else if (message != null) {
			return SettableOnce.withMessage(message);
		} else {
			return SettableOnce.empty();
		}
	}

	@Before
	public void setUp() throws Exception {
		settable = createSettableWith(null, null);
	}

	@Test
	public void testNulls() {
		nullPointerTester.testConstructors(SettableOnce.class, Visibility.PROTECTED);
		nullPointerTester.testInstanceMethods(settable, Visibility.PROTECTED);
	}

	@Test
	public void testSet_whenEmpty() {
		// test
		settable.set(VALUE);

		// verify
		assertThat(settable.get(), equalTo(VALUE));
	}

	@Test
	public void testSet_whenNotEmptyNoMessage() {
		// setup
		settable.set(VALUE);

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field cannot be set more than once");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_whenNotEmptyWithName() {
		// setup
		settable = createSettableWith("MyName", null);
		settable.set(VALUE);

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field MyName cannot be set more than once");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_whenNotEmptyWithMessage() {
		// setup
		final String message = "this is some random message";
		settable = createSettableWith(null, message);
		settable.set(VALUE);

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage(message);

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSetIfAbsent_whenEmpty() {
		// test
		settable.setIfAbsent(VALUE);

		// verify
		assertThat(settable.get(), equalTo(VALUE));
	}

	@Test
	public void testSetIfAbsent_whenNotEmpty() {
		// setup
		settable.set(VALUE);

		// test
		settable.setIfAbsent("somthing else");

		// verify
		assertThat(settable.get(), equalTo(VALUE));
	}

	@Test
	public void testGetOptional_whenEmpty() {
		// test
		Optional<String> result = settable.getOptional();

		// verify
		assertThat(result, IsOptional.absent(String.class));
	}

	@Test
	public void testGetOptional_whenNotEmpty() {
		// setup
		settable.set(VALUE);

		// test
		Optional<String> result = settable.getOptional();

		// verify
		assertThat(result, IsOptional.presentContaining(VALUE));
	}

	@Test
	public void testGetMessage_NoMessage() {
		// test
		String message = settable.getMessage();

		// verify
		assertThat(message, equalTo("Field cannot be set more than once"));
	}

	@Test
	public void testGetMessage_WithName() {
		// setup
		settable = createSettableWith("MyName", null);

		// test
		String message = settable.getMessage();

		// verify
		assertThat(message, equalTo("Field MyName cannot be set more than once"));
	}

	@Test
	public void testGetMessage_WithMessage() {
		// setup
		final String setMessage = "this is some random message";
		settable = createSettableWith(null, setMessage);

		// test
		String message = settable.getMessage();

		// verify
		assertThat(message, equalTo(setMessage));
	}

	@Test
	public void testSetMessage() {
		// setup
		final String setMessage = "this is some random message";
		settable = createSettableWith("MyName", null);

		// test
		settable.setMessage(setMessage);

		// verify
		assertThat(settable.getMessage(), equalTo(setMessage));
	}
}
