package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.libex.test.TestBase;

import com.google.common.base.Optional;

public class IsOptionalTest extends TestBase {

	private static final Optional<Integer> absent = Optional.absent();
	private static final Optional<Integer> present = Optional.of(5);

	@Test
	public void testNulls() throws NoSuchMethodException, SecurityException {
		nullPointerTester.ignore(IsOptional.class.getMethod("present", Class.class));
		nullPointerTester.ignore(IsOptional.class.getMethod("absent", Class.class));

		nullPointerTester.testAllPublicStaticMethods(IsOptional.class);
	}

	@Test
	public void testAbsent() {
		assertThat(absent, IsOptional.<Integer> absent());
		assertThat(present, not(IsOptional.<Integer> absent()));
		assertThat(null, not(IsOptional.<Integer> absent()));
	}

	@Test
	public void testAbsentClassOfT() {
		assertThat(absent, IsOptional.absent(Integer.class));
		assertThat(present, not(IsOptional.absent(Integer.class)));
		assertThat(null, not(IsOptional.absent(Integer.class)));
	}

	@Test
	public void testPresent() {
		assertThat(present, IsOptional.<Integer> present());
		assertThat(absent, not(IsOptional.<Integer> present()));
		assertThat(null, not(IsOptional.<Integer> present()));
	}

	@Test
	public void testPresentClassOfT() {
		assertThat(present, IsOptional.present(Integer.class));
		assertThat(absent, not(IsOptional.present(Integer.class)));
		assertThat(null, not(IsOptional.present(Integer.class)));
	}

	@Test
	public void testPresentContaining() {
		assertThat(present, IsOptional.presentContaining(5));
		assertThat(absent, not(IsOptional.presentContaining(5)));
		assertThat(null, not(IsOptional.presentContaining(5)));
		assertThat(Optional.of(2), not(IsOptional.presentContaining(5)));
	}

	@Test
	public void testPresentMatching() {
		assertThat(present, IsOptional.presentMatching(is(5)));
		assertThat(absent, not(IsOptional.presentMatching(is(5))));
		assertThat(null, not(IsOptional.presentMatching(is(5))));
		assertThat(Optional.of(2), not(IsOptional.presentMatching(is(5))));
	}

	@Test
	public void testOfNullableValue() {
		assertThat(present, IsOptional.ofNullableValue(5));
		assertThat(absent, not(IsOptional.ofNullableValue(5)));
		assertThat(absent, IsOptional.ofNullableValue((Integer) null));
		assertThat(present, not(IsOptional.ofNullableValue((Integer) null)));
	}
}
