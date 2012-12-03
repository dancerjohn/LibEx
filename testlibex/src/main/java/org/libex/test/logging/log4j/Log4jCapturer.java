package org.libex.test.logging.log4j;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.find;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libex.logging.log4j.InMemoryAppender;
import org.libex.logging.log4j.LoggingEventsEx;

import com.google.common.base.Predicate;

/**
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class Log4jCapturer implements TestRule {

	private static final String DEFAULT_LAYOUT = "%d{DATE} %5p %C{1}.%M(),%L - %m%n";
	private static final String APPENDER_NAME = "Log4jCapturerAppender";

	public static Log4jCapturerBuilder builder() {
		return new Log4jCapturerBuilder();
	}

	public static class Log4jCapturerBuilder {
		private Level threshold = Level.INFO;
		private Layout layout = new PatternLayout(DEFAULT_LAYOUT);

		public Log4jCapturerBuilder setThreshold(Level threshold) {
			this.threshold = threshold;
			return this;
		}

		public Log4jCapturerBuilder setLayout(Layout layout) {
			this.layout = layout;
			return this;
		}

		public Log4jCapturer build() {
			return new Log4jCapturer(threshold, layout);
		}
	}

	private final InMemoryAppender appender;

	private Log4jCapturer(Level threshold, Layout layout) {
		appender = new InMemoryAppender();
		appender.setThreshold(threshold);
		appender.setLayout(layout);
		appender.setName(APPENDER_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement,
	 * org.junit.runner.Description)
	 */
	@Override
	public Statement apply(final Statement statement, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				addAppender();

				try {
					statement.evaluate();
				} finally {
					removeAppender();
				}
			}
		};
	}

	private void addAppender() {
		appender.clear();
		Logger.getRootLogger().addAppender(appender);
	}

	private void removeAppender() {
		try {
			appender.clear();
			Logger.getRootLogger().removeAppender(appender);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void clearLog() {
		appender.clear();
	}

	public void assertThat(LogAssertionBuilder assertion) {

		List<LoggingEvent> logs = appender.getLoggingEvents();
		LoggingEvent event = find(logs, assertion.criteria(), null);

		Matcher<Object> matcher = (assertion.logged) ? notNullValue() : nullValue();
		MatcherAssert.assertThat(assertion.toString(), event, matcher);
	}

	public static class LogAssertionBuilder {

		public static LogAssertionBuilder newLogAssertion() {
			return new LogAssertionBuilder();
		}

		private boolean logged = true;
		private Matcher<? super Level> level = Matchers.anything();
		private Matcher<? super String> message = Matchers.anything();
		private Matcher<? super Throwable> exception = Matchers.anything();

		public LogAssertionBuilder isLogged(boolean logged) {
			this.logged = logged;
			return this;
		}

		public LogAssertionBuilder withLevel(Level level) {
			return withLevel(Matchers.equalTo(level));
		}

		public LogAssertionBuilder withLevel(Matcher<? super Level> level) {
			this.level = level;
			return this;
		}

		public LogAssertionBuilder withRenderedMessage(String substring) {
			return withRenderedMessage(Matchers.containsString(substring));
		}

		public LogAssertionBuilder withRenderedMessage(Matcher<? super String> message) {
			this.message = message;
			return this;
		}

		public LogAssertionBuilder withException(Matcher<? super Throwable> exception) {
			this.exception = exception;
			return this;
		}

		@SuppressWarnings("unchecked")
		private Predicate<LoggingEvent> criteria() {
			return and(LoggingEventsEx.withLevel(level),
					LoggingEventsEx.withRenderedMessage(message),
					LoggingEventsEx.withThrowable(exception));
		}

		@Override
		public String toString() {
			org.hamcrest.Description description = new StringDescription();

			if (logged) {
				description.appendText("Message logged");
			} else {
				description.appendText("No message logged");
			}

			if (notIsAnything(level))
			{
				description.appendText(" with level ");
				description.appendDescriptionOf(level);
			}

			if (notIsAnything(message))
			{
				description.appendText(" with message ");
				description.appendDescriptionOf(message);
			}

			if (notIsAnything(exception))
			{
				description.appendText(" with exception ");
				description.appendDescriptionOf(exception);
			}

			return description.toString();
		}

		private boolean notIsAnything(Matcher<?> matcher) {
			return !(matcher instanceof org.hamcrest.core.IsAnything);
		}
	}
}
