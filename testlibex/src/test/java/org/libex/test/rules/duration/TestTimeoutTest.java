package org.libex.test.rules.duration;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.Statement;
import org.libex.concurrent.TimeSpan;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TestTimeoutTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testEvaluate_finishInTime() throws Throwable {
		// setup
		TestTimeout test = new TestTimeout(createDelayedStatement(TimeSpan.milliseconds(100)), 200);

		// test
		test.evaluate();
	}

	@Test
	public void testEvaluate_finishTooLate() throws Throwable {
		// setup
		TestTimeout test = new TestTimeout(createDelayedStatement(TimeSpan.milliseconds(200)), 100);

		// expect
		expectedException.expect(Exception.class);

		// test
		test.evaluate();
	}

	private Statement createDelayedStatement(final TimeSpan timeSpan) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				Thread.sleep(timeSpan.getDurationIn(TimeUnit.MILLISECONDS));
			}
		};
	}

}
