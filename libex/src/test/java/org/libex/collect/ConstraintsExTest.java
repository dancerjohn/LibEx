package org.libex.collect;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.libex.base.StringsEx;
import org.libex.test.TestBase;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Constraint;

public class ConstraintsExTest extends TestBase {

	private Predicate<String> isNullOrEmpty = StringsEx.isNullOrEmpty();

	@Test
	public void testConstrainedWithPredicateOfT() {
		// test
		Constraint<String> constraint = ConstraintsEx.constrainedWith(isNullOrEmpty);

		// verify
		constraint.checkElement("");

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage(equalTo(""));

		constraint.checkElement("123");
	}

	@Test
	public void testConstrainedWithPredicateOfTString() {
		// setup
		final String message = "my message";

		// test
		Constraint<String> constraint = ConstraintsEx.constrainedWith(isNullOrEmpty, message);

		// verify
		constraint.checkElement("");

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage(equalTo(message));

		constraint.checkElement("123");
	}

	@Test
	public void testConstrainedWithPredicateOfTSupplierOfString() {
		// setup
		final String message = "my message";
		@SuppressWarnings("unchecked")
		Supplier<String> supplier = mock(Supplier.class);
		when(supplier.get()).thenReturn(message);

		// test
		Constraint<String> constraint = ConstraintsEx.constrainedWith(isNullOrEmpty, supplier);

		// verify
		constraint.checkElement("");
		verifyNoMoreInteractions(supplier);

		try {
			constraint.checkElement("123");
		} catch (RuntimeException re) {
			verify(supplier).get();
			assertThat(re.getMessage(), equalTo(message));
		}

		try {
			constraint.checkElement("123");
		} catch (RuntimeException re) {
			verify(supplier, times(2)).get();
			assertThat(re.getMessage(), equalTo(message));
		}
	}

}
