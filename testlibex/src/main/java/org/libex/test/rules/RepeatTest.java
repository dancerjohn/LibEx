package org.libex.test.rules;

import static org.hamcrest.MatcherAssert.*;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.number.OrderingComparisons;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libex.test.annotation.Repeat;

/**
 * Rule used to repeat tests marked with the {@link Repeat} annotation
 * 
 * @author John Butler
 * 
 * @see Repeat
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class RepeatTest implements TestRule {
	public static RepeatTest repeat() {
		return new RepeatTest();
	}

	@Override
	public Statement apply(final Statement statement, Description description) {
		final int repeatCount = getRepeatCount(description);
		assertThat("Repeat count must be > 0", repeatCount,
				OrderingComparisons.greaterThan(0));

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				for (int i = 0; i < repeatCount; i++) {
					statement.evaluate();
				}
			}
		};
	}

	private int getRepeatCount(Description description) {
		Repeat repeat = description.getAnnotation(Repeat.class);
		int count = 1;
		if (repeat != null) {
			count = repeat.count();
		}
		return count;
	}
}
