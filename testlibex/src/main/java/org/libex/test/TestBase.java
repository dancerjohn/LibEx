package org.libex.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;

import javax.annotation.Nullable;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import com.google.common.testing.NullPointerTester;

/**
 * JUnit test base class.
 * 
 * @author John Butler
 */
public abstract class TestBase {

	protected NullPointerTester nullPointerTester = new NullPointerTester();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Rule
	public ErrorCollector errorCollector = new ErrorCollector();

	/**
	 * Sets the {@code expectedException} to expect an exception of the provided
	 * {@code type} and with a superstring of the provided {@code substring}
	 * 
	 * @param type
	 *            the type of Exception to expect
	 * @param substring
	 *            a substring of the exception message to expect
	 */
	protected void expectException(Class<? extends Throwable> type, @Nullable String substring) {
		expectedException.expect(type);
		if (substring != null) {
			expectedException.expectMessage(substring);
		}
	}

	protected void expectException(Class<? extends Throwable> type,
			@Nullable String substring,
			Exception cause) {
		expectException(type, substring);
		expectedException.expectCause(sameInstance(cause));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void expectException(Class<? extends Throwable> type,
			@Nullable String substring,
			Class<? extends Throwable> causeType) {
		expectException(type, substring);
		expectedException.expectCause((Matcher) instanceOf(causeType));
	}
}
