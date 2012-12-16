package org.libex.concurrent.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.concurrent.TimeSpan;

/**
 * A Profiling that is used to filter Profiling notifications to those whose
 * time vales exceed some threshold.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public class ThresholdNotificationProfiler extends ProfilingDecorator {

	public static class Config extends ProfilingDecorator.Config {
		@Nullable
		private TimeSpan thresholdForNotification;

		public void setThresholdForNotification(
				TimeSpan thresholdForNotification) {
			this.thresholdForNotification = thresholdForNotification;
		}
	}

	private final TimeSpan thresholdForNotification;

	public ThresholdNotificationProfiler(Config config) {
		super(config);
		this.thresholdForNotification = checkNotNull(config.thresholdForNotification);
	}

	@Override
	public void processProfileEvent(ProfileResult result) {
		if (thresholdMet(result.getTimeSpan())) {
			super.processProfileEvent(result);
		}
	}

	private boolean thresholdMet(TimeSpan timeSpan) {
		boolean invoke = timeSpan.compareTo(thresholdForNotification) >= 0;
		return invoke;
	}
}
