package org.libex.concurrent.profile;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.libex.concurrent.TimeSpan;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class LoggingProfiler extends ProfilingDecorator {

	private static final Logger logger = Logger.getLogger(LoggingProfiler.class);

	public static class Config extends ProfilingDecorator.Config {
		@Nonnull
		private Level level = Level.DEBUG;
		@Nullable
		private TimeUnit loggingTimeUnit;

		public void setLevel(Level level) {
			this.level = level;
		}

		public void setLoggingTimeUnit(TimeUnit loggingTimeUnit) {
			this.loggingTimeUnit = loggingTimeUnit;
		}
	}

	private final Level level;
	@Nullable
	private final TimeUnit outputTimeUnit;

	public LoggingProfiler(Config config) {
		super(config);
		this.level = config.level;
		this.outputTimeUnit = config.loggingTimeUnit;
	}

	@Override
	public void processProfileEvent(ProfileResult result) {
		TimeSpan timeSpan = result.getTimeSpan();

		if (outputTimeUnit != null) {
			timeSpan = timeSpan.convertTo(outputTimeUnit);
		}
		logger.log(level, String.format("%s took %s to execute", result.getCallable(), timeSpan));

		super.processProfileEvent(result);
	}
}
