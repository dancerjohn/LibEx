package org.libex.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.testing.NullPointerTester.Visibility;

public class FinalizableSettableOnceTest extends SettableOnceTest {

	private FinalizableSettableOnce<String> settable;

	@Override
	protected FinalizableSettableOnce<String> createSettableWith(String name, String message) {
		if (name != null) {
			return FinalizableSettableOnce.withName(name);
		} else if (message != null) {
			return FinalizableSettableOnce.withMessage(message);
		} else {
			return FinalizableSettableOnce.empty();
		}
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		settable = createSettableWith(null, null);
	}

	@Test
	public void testNullsFinalize() {
		nullPointerTester.testConstructors(FinalizableSettableOnce.class, Visibility.PROTECTED);
		nullPointerTester.testInstanceMethods(settable, Visibility.PROTECTED);
	}

	@Test
	public void testSet_emptyFinalizedNoMessage() {
		// setup
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field has been finalized");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_emptyFinalizedWithName() {
		// setup
		settable = createSettableWith("MyName", null);
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field MyName has been finalized");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_emptyFinalizedWithMessage() {
		// setup
		final String message = "this is some random message";
		settable = createSettableWith(null, message);
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage(message);

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_notEmptyFinalizedNoMessage() {
		// setup
		settable.set(VALUE);
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field has been finalized");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_notEmptyFinalizedWithName() {
		// setup
		settable = createSettableWith("MyName", null);
		settable.set(VALUE);
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Field MyName has been finalized");

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSet_notEmptyFinalizedWithMessage() {
		// setup
		final String message = "this is some random message";
		settable = createSettableWith(null, message);
		settable.set(VALUE);
		settable.makeImmutable();

		// expect
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage(message);

		// test
		settable.set(VALUE);
	}

	@Test
	public void testSetIfAbsent_finalized() {
		// setup
		settable.set(VALUE);
		settable.makeImmutable();

		// test
		settable.setIfAbsent("something else");

		// verify
		assertThat(settable.get(), equalTo(VALUE));
	}

}
