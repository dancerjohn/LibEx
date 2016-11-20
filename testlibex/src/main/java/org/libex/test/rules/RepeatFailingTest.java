package org.libex.test.rules;

import static org.hamcrest.MatcherAssert.assertThat;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.number.OrderingComparison;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libex.test.annotation.RepeatIfFailed;

/**
 * Rule used to repeat tests marked with the {@link RepeatIfFailed} annotation
 * 
 * @author John Butler
 * 
 * @see RepeatIfFailed
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class RepeatFailingTest implements TestRule {

	public static RepeatFailingTest findRepeats() {
		return new RepeatFailingTest();
	}

	private RepeatFailingTest() {
		super();
	}

	@Override
	public Statement apply(final Statement statement, Description description) {
		return new FailingStatementRepeater(statement, description);
	}

	private final class FailingStatementRepeater extends Statement {

		private final Statement statement;
        private final Description description;
		private final int repeatCount;
        private final int delayMs;

		private FailingStatementRepeater(Statement statement, Description description) {
			super();
			this.statement = statement;
            this.description = description;
            RepeatIfFailed repeat = description.getAnnotation(RepeatIfFailed.class);

			this.repeatCount = getRepeatCount(repeat);
            this.delayMs = getDelayMs(repeat);
		}

        private final int getRepeatCount(@Nullable RepeatIfFailed repeat) {
            int count = RepeatIfFailed.DEFAULT_MAX_REPEATS;
			if (repeat != null) {
                count = repeat.maxRepeats();
			}

			assertThat("Repeat count must be > 0", count,
					OrderingComparison.greaterThan(0));
			return count;
		}

        private final int getDelayMs(@Nullable RepeatIfFailed repeat) {
            int delay = RepeatIfFailed.DEFAULT_DELAY_MS;
            if (repeat != null) {
                delay = repeat.delayInMS();
            }

            assertThat("Repeat count must be >= 0", delay,
                    OrderingComparison.greaterThanOrEqualTo(0));
            return delay;
        }

		@Override
		public void evaluate() throws Throwable {
            Throwable caughtThrowable = null;
            for (int i = 0; i < repeatCount; i++) {
                try {
                    statement.evaluate();
                    return;
                } catch (Throwable t) {
                    caughtThrowable = t;
                    System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed");

                    if (delayMs > 0 && repeatCount > i + 1) {
                        Thread.sleep(delayMs);
                    }
                }
            }
            System.err.println(description.getDisplayName() + ": giving up after " + repeatCount + " failures");
            throw caughtThrowable;
		}
	}
}
