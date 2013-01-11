package org.libex.test.rules.duration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libex.concurrent.TimeSpan;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Credit for the code in package is given to Nelson Llewellyn.
 */
public class TestDurationRule implements TestRule {

	private final LoadingCache<TestDuration, Boolean> runTestCache = CacheBuilder.newBuilder()
			.build(new CacheLoader<TestDuration, Boolean>() {

				@Override
				public Boolean load(TestDuration key) throws Exception {
					return key.shouldTestBeRun();
				}
			});

	@Nullable
	private Optional<TestDuration> classLevelTestDuration = null;

	@Nonnull
	private Optional<TestDuration> getClassLevelTestDuration(Description description) {
		if (classLevelTestDuration == null) {
			Duration duration = description.getTestClass().getAnnotation(Duration.class);
			if (duration != null) {
				classLevelTestDuration = Optional.of(duration.value());
			} else {
				classLevelTestDuration = Optional.absent();
			}
		}

		return classLevelTestDuration;
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		return new DurationCheckingStatement(statement, description);
	}

	private class DurationCheckingStatement extends Statement {

		private final Statement statement;
		private final Description description;

		private DurationCheckingStatement(Statement statement, Description description) {
			super();
			this.statement = statement;
			this.description = description;
		}

		@Override
		public void evaluate() throws Throwable {
			Statement statementToEvaluate = statement;

			Optional<TestDuration> testDuration = getTestDuration();
			checkTestShouldBeRun(testDuration);

			statementToEvaluate = createStatementWithTestDuration(statementToEvaluate, testDuration);
			statementToEvaluate.evaluate();
		}

		@Nonnull
		private Optional<TestDuration> getTestDuration() {
			return getMethodLevelTestDuration()
					.or(getClassLevelTestDuration(description));
		}

		@Nonnull
		private Optional<TestDuration> getMethodLevelTestDuration() {
			TestDuration testDuration = null;
			Duration duration = description.getAnnotation(Duration.class);
			if (duration != null) {
				testDuration = duration.value();
			}

			return Optional.fromNullable(testDuration);
		}

		private void checkTestShouldBeRun(Optional<TestDuration> testDuration) {
			if (testDuration.isPresent()) {
				boolean runTest = runTestCache.getUnchecked(testDuration.get());
				Assume.assumeTrue(runTest);
			}
		}

		@Nonnull
		private Statement createStatementWithTestDuration(Statement statementToEvaluate, Optional<TestDuration> testDuration) throws Throwable {
			Statement result = statementToEvaluate;

			if (testDuration.isPresent()) {
				TestDuration duration = testDuration.get();
				Optional<TimeSpan> maxDuration = duration.getMaxDuration();
				if (maxDuration.isPresent()) {
					result = createStatementWithMaxDuration(maxDuration.get());
				}
			}

			return result;
		}

		@Nonnull
		private Statement createStatementWithMaxDuration(TimeSpan maxDuration) throws Throwable {
			TestTimeout testTimeout = new TestTimeout(statement, maxDuration.getDuration(), maxDuration.getTimeUnit());
			return testTimeout;
		}
	}
}
