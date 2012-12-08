package org.libex.test;

import org.junit.Rule;
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

	/**
	 * Sets the {@code expectedException} to expect an exception of the provided {@code type}
	 * and with a superstring of the provided {@code substring} 
	 * @param type the type of Exception to expect
	 * @param substring a substring of the exception message to expect
	 */
	protected void expectException(Class<? extends Throwable> type, String substring) {
		expectedException.expect(type);
		expectedException.expectMessage(substring);
	}
}
