package org.libex.test.rules.duration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.annotation.DefaultScope;
import org.libex.concurrent.TimeSpan;

import com.google.common.base.Optional;

/**
 * Credit for the code in package is given to Nelson Llewellyn.
 */
@ThreadSafe
public enum TestDuration {
	SHORT("runShortTests", true, TimeSpan.seconds(3)),
	MEDIUM("runMediumTests", true, TimeSpan.seconds(10)),
	LONG("runLongTests", false, null);

	private final String systemPropertyFieldName;
	private final boolean runByDefault;

	@Nullable
	private final TimeSpan maxDuration;

	private TestDuration(String systemPropertyFieldName, boolean runByDefault, @Nullable TimeSpan maxDuration) {
		this.systemPropertyFieldName = systemPropertyFieldName;
		this.runByDefault = runByDefault;
		this.maxDuration = maxDuration;
	}

	@DefaultScope
	@Nonnull
	Optional<TimeSpan> getMaxDuration() {
		return Optional.fromNullable(maxDuration);
	}

	@DefaultScope
	boolean shouldTestBeRun() {
		boolean result = runByDefault;

		String propertyValue = System.getProperty(systemPropertyFieldName);
		if (propertyValue != null) {
			if ("true".equalsIgnoreCase(propertyValue)) {
				result = true;
			} else if ("false".equalsIgnoreCase(propertyValue)) {
				result = false;
			}
		}

		return result;
	}
}
