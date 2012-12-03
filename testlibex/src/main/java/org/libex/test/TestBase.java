package org.libex.test;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.google.common.testing.NullPointerTester;

/**
 * @author John Butler
 */
public abstract class TestBase {

	protected NullPointerTester nullPointerTester = new NullPointerTester();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	protected void expectException(Class<? extends Throwable> type, String substring) {
		expectedException.expect(type);
		expectedException.expectMessage(substring);
	}
}
