package org.libex.test.logging.log4j;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.find;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.log4j.Appender;
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
import org.libex.hamcrest.IsThrowable;
import org.libex.logging.log4j.InMemoryAppender;
import org.libex.logging.log4j.LoggingEventsEx;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Rule that allows for capturing Log4J logging for test verification.
 * 
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class Log4jCapturer implements TestRule {

	private static final String DEFAULT_LAYOUT = "%d{DATE} %5p %C{1}.%M(),%L - %m%n";
	private static final String APPENDER_NAME = "Log4jCapturerAppender";

	/**
	 * @return new capturer builder
	 */
	public static Log4jCapturerBuilder builder() {
		return new Log4jCapturerBuilder();
	}

	/**
	 * Builder for {@link Log4jCapturer}
	 */
	public static class Log4jCapturerBuilder {
		private Level threshold = Level.INFO;
		private Layout layout = new PatternLayout(DEFAULT_LAYOUT);

		/**
		 * Sets the logging threshold for messages that should be recorded. This
		 * is set as the threshold on the created {@link Appender}
		 * 
		 * @param threshold
		 *            the lowest level of messages that should be held
		 * @return this instance
		 * 
		 * @see org.apache.log4j.AppenderSkeleton#setThreshold(org.apache.log4j.Priority)
		 */
		public Log4jCapturerBuilder setThreshold(Level threshold) {
			this.threshold = threshold;
			return this;
		}

		/**
		 * Sets the logging layout for message that are recorded. This is set as
		 * the layout on the created {@link Appender}
		 * 
		 * @param layout
		 *            the layout to set
		 * @return this instance
		 * 
		 * @see org.apache.log4j.AppenderSkeleton#setLayout(Layout)
		 */
		public Log4jCapturerBuilder setLayout(Layout layout) {
			this.layout = layout;
			return this;
		}

		/**
		 * @return a new {@link Log4jCapturer}
		 */
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

	/**
	 * Clears the list of currently recorded logs.
	 */
	public void clearLog() {
		appender.clear();
	}

	/**
	 * Gets the list of logs that matches the passed assertion
	 * 
	 * @param assertion
	 *            the filter by which to retrieve logs
	 * @return an unmodifiable Iterable over the list of logs that match the
	 *         passed assertion
	 */
	public Iterable<LoggingEvent> filter(Predicate<LoggingEvent> assertion) {
		List<LoggingEvent> logs = appender.getLoggingEvents();
		return Iterables.filter(logs, assertion);
	}

	/**
	 * Gets the list of logs that matches the passed assertion
	 * 
	 * @param assertion
	 *            the filter by which to retrieve logs
	 * @return an unmodifiable Iterable over the list of logs that match the
	 *         passed assertion
	 */
	public Iterable<LoggingEvent> getLogs(LogAssertion assertion) {
		return filter(assertion.criteria());
	}

	/**
	 * Asserts the passed assertion
	 * 
	 * @param assertion
	 *            the logging assertion to verify
	 */
	public void assertThat(LogAssertion assertion) {

		List<LoggingEvent> logs = appender.getLoggingEvents();
		LoggingEvent event = find(logs, assertion.criteria(), null);

		Matcher<Object> matcher = (assertion.logged) ? notNullValue()
				: nullValue();
		MatcherAssert.assertThat(assertion.toString(), event, matcher);
	}

	/**
	 * A LoggingEvent assertion
	 */
	public static class LogAssertion {

		/**
		 * @return a new empty assertion with default values
		 */
		public static LogAssertion newLogAssertion() {
			return new LogAssertion();
		}

		private boolean logged = true;
		private Matcher<? super Level> level = Matchers.anything();
		private Matcher<? super String> message = Matchers.anything();
		private Matcher<? super Throwable> exception = Matchers.anything();

		/**
		 * Sets the assertion to expect the message to be logged. This method
		 * should be used in conjuctions with one of the other {@code withX}
		 * methods. This method is mutually exclusive with
		 * {@link #isNotLogged()}
		 * 
		 * @return this instance
		 */
		public LogAssertion isLogged() {
			this.logged = true;
			return this;
		}

		/**
		 * Sets the assertion to expect the message to NOT be logged. This
		 * method should be used in conjuctions with one of the other
		 * {@code withX} methods. This method is mutually exclusive with
		 * {@link #isLogged()}
		 * 
		 * @return this instance
		 */
		public LogAssertion isNotLogged() {
			this.logged = false;
			return this;
		}

		/**
		 * Sets the assertion to expect the message to have the passed
		 * {@code level}. The use of this method is sufficient to assert a
		 * message is logged. No other method calls are required, other than the
		 * call to {@link Log4jCapturer#assertThat(LogAssertion)}.
		 * 
		 * @param level
		 *            the level to expect
		 * @return this instance
		 */
		public LogAssertion withLevel(Level level) {
			return withLevel(Matchers.equalTo(level));
		}

		/**
		 * Sets the assertion to expect the message to have a level that matches
		 * the passed {@code level}. The use of this method is sufficient to
		 * assert a message is logged. No other method calls are required, other
		 * than the call to {@link Log4jCapturer#assertThat(LogAssertion)}.
		 * 
		 * @param level
		 *            the level to expect
		 * @return this instance
		 */
		public LogAssertion withLevel(Matcher<? super Level> level) {
			this.level = level;
			return this;
		}

		/**
		 * Sets the assertion to expect the rendered (formatted) message to have
		 * a message that is super-string of the passed {@code substring}. The
		 * use of this method is sufficient to assert a message is logged. No
		 * other method calls are required, other than the call to
		 * {@link Log4jCapturer#assertThat(LogAssertion)}.
		 * 
		 * @param substring
		 *            the message to expect
		 * @return this instance
		 */
		public LogAssertion withRenderedMessage(String substring) {
			return withRenderedMessage(Matchers.containsString(substring));
		}

		/**
		 * Sets the assertion to expect the rendered (formatted) message to
		 * match the passed {@code message}. The use of this method is
		 * sufficient to assert a message is logged. No other method calls are
		 * required, other than the call to
		 * {@link Log4jCapturer#assertThat(LogAssertion)}.
		 * 
		 * @param message
		 *            the message to expect
		 * @return this instance
		 */
		public LogAssertion withRenderedMessage(Matcher<? super String> message) {
			this.message = message;
			return this;
		}

		/**
		 * Sets the assertion to expect the logging event to contain an
		 * exception that matches the passed {@code exception}. The use of this
		 * method is sufficient to assert a message is logged. No other method
		 * calls are required, other than the call to
		 * {@link Log4jCapturer#assertThat(LogAssertion)}.
		 * 
		 * @param exception
		 *            the exception matcher, consider {@link IsThrowable}
		 * @return this instance
		 */
		public LogAssertion withException(Matcher<? super Throwable> exception) {
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

			if (notIsAnything(level)) {
				description.appendText(" with level ");
				description.appendDescriptionOf(level);
			}

			if (notIsAnything(message)) {
				description.appendText(" with message ");
				description.appendDescriptionOf(message);
			}

			if (notIsAnything(exception)) {
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
