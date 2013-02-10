package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.libex.test.TestBase;

public class IsThrowableTest extends TestBase {

	private static final Exception exception = new Exception("exception message");
	private static final RuntimeException runtimeException = new RuntimeException("runtimeException message");
	private static final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("illegalArgumentException message");
	private static final IOException ioexception = new IOException("ioexception message");

	@Test
	public void testNulls() throws NoSuchMethodException, SecurityException {
		nullPointerTester.testAllPublicStaticMethods(IsThrowable.class);
	}

	@Test
	public void testIsThrowableOfType() {
		assertThat(exception, not(IsThrowable.isThrowableOfType(RuntimeException.class)));
		assertThat(runtimeException, IsThrowable.isThrowableOfType(RuntimeException.class));
		assertThat(illegalArgumentException, IsThrowable.isThrowableOfType(RuntimeException.class));
		assertThat(ioexception, not(IsThrowable.isThrowableOfType(RuntimeException.class)));
	}

	@Test
	public void testIsThrowableWithMessage() {
		assertThat(runtimeException, IsThrowable.isThrowableWithMessage("message"));
		assertThat(runtimeException, IsThrowable.isThrowableWithMessage("runtimeException message"));
		assertThat(exception, not(IsThrowable.isThrowableWithMessage("runtimeException message")));
	}

	@Test
	public void testIsThrowable() {
		assertThat(exception, not(IsThrowable.isThrowable(RuntimeException.class, "exception message")));
		assertThat(exception, not(IsThrowable.isThrowable(Exception.class, "blah message")));
		assertThat(exception, IsThrowable.isThrowable(Exception.class, "exception message"));
		assertThat(runtimeException, IsThrowable.isThrowable(RuntimeException.class, "runtimeException message"));
		assertThat(runtimeException, IsThrowable.isThrowable(RuntimeException.class, "message"));
		assertThat(illegalArgumentException, IsThrowable.isThrowable(RuntimeException.class, "message"));
		assertThat(ioexception, not(IsThrowable.isThrowable(RuntimeException.class, "message")));
	}

}
