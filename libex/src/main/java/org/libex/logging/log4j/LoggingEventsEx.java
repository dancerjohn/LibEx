package org.libex.logging.log4j;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.hamcrest.Matcher;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

/**
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public final class LoggingEventsEx {

	public static Predicate<LoggingEvent> withLevel(final Level level) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null && event.getLevel().equals(level);
			}
		};
	}

	public static Predicate<LoggingEvent> withLevel(final Matcher<? super Level> matcher) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null && matcher.matches(event.getLevel());
			}
		};
	}

	public static Predicate<LoggingEvent> withRenderedMessage(final String message) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null && Objects.equal(event.getRenderedMessage(), message);
			}
		};
	}

	public static Predicate<LoggingEvent> withRenderedMessage(final Matcher<? super String> matcher) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null && matcher.matches(event.getRenderedMessage());
			}
		};
	}

	public static Predicate<LoggingEvent> withThrowable(final Class<? extends Throwable> type) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null &&
						event.getThrowableInformation() != null &&
						event.getThrowableInformation().getThrowable() != null &&
						type.isAssignableFrom(event.getThrowableInformation().getThrowable().getClass());
			}
		};
	}

	public static Predicate<LoggingEvent> withThrowable(final Matcher<? super Throwable> matcher) {
		return new Predicate<LoggingEvent>() {

			@Override
			public boolean apply(@Nullable LoggingEvent event) {
				return event != null
						&&
						matcher.matches((event.getThrowableInformation() == null) ? null : event
								.getThrowableInformation().getThrowable());
			}
		};
	}

	private LoggingEventsEx() {
	}
}
