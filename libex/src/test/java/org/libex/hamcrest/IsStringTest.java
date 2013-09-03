package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.libex.test.TestBase;

@NotThreadSafe
@ParametersAreNonnullByDefault
public class IsStringTest extends TestBase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNulls() throws NoSuchMethodException, SecurityException {
		nullPointerTester.testAllPublicStaticMethods(IsString.class);
	}

	@Test
	public void testMatchingRegularExpression() {
		Matcher<String> matcher = IsString.matchingRegularExpression(".*123.*");

		assertThat("123", matcher);
		assertThat("  asda 123  sdfdsf  ", matcher);
		assertThat("  asda123sdfdsf  ", matcher);
		assertThat("  asda12sdfdsf  ", not(matcher));
		assertThat("  asda12 3sdfdsf  ", not(matcher));
	}

}
