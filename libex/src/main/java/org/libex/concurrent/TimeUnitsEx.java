package org.libex.concurrent;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.enums.SingularPlural;

import com.google.common.collect.ImmutableMap;

/**
 * Utilities on {@link TimeUnit}
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class TimeUnitsEx {

	private static final ImmutableMap<TimeUnit, String> lowercaseSingularNameMap;
	private static final ImmutableMap<TimeUnit, String> lowercasePluralNameMap;

	static {
		ImmutableMap.Builder<TimeUnit, String> pluralBuilder = ImmutableMap
				.builder();
		ImmutableMap.Builder<TimeUnit, String> singlarBuilder = ImmutableMap
				.builder();

		for (TimeUnit timeUnit : TimeUnit.values()) {
			String pluralName = timeUnit.name().toLowerCase();

			pluralBuilder.put(timeUnit, pluralName);
			singlarBuilder.put(timeUnit,
					pluralName.substring(0, pluralName.length() - 1));
		}

		lowercaseSingularNameMap = singlarBuilder.build();
		lowercasePluralNameMap = pluralBuilder.build();
	}

	/**
	 * Gets the string representation of the passed duration and time unit. The
	 * representation will be with lowercase, plural-appropriate time unit
	 * 
	 * @param duration
	 *            the duration
	 * @param timeUnit
	 *            the time unit
	 * @return the string representation of the passed duration and time unit
	 */
	@Nonnull
	public static String toString(final long duration, final TimeUnit timeUnit) {
		return String.format(
				"%d %s",
				duration,
				toLowercase(timeUnit, duration == 1L ? SingularPlural.SINGULAR
						: SingularPlural.PLURAL));
	}

	        /**
     * Gets the plural/singular lower-case name of the passed time unit
     * 
     * @param timeUnit
     *            the time unit
     * @param singularPlural
     *            if the returned string should be singular
     * @return the name of the timeUnit in lower case
     */
	@Nonnull
	public static String toLowercase(final TimeUnit timeUnit,
			final SingularPlural singularPlural) {
		if (singularPlural.isSingular()) {
			return toSingularLowercase(timeUnit);
		} else {
			return toPluralLowercase(timeUnit);
		}
	}

	/**
	 * Gets the lowercase singular name of the timeunit
	 * 
	 * @param timeUnit
	 *            the time unit
	 * @return the lowercase singular name of the timeunit
	 */
	@Nonnull
	public static String toSingularLowercase(final TimeUnit timeUnit) {
		return lowercaseSingularNameMap.get(timeUnit);
	}

	/**
	 * Gets the lowercase plural name of the timeunit
	 * 
	 * @param timeUnit
	 *            the time unit
	 * @return the lowercase plural name of the timeunit
	 */
	@Nonnull
	public static String toPluralLowercase(final TimeUnit timeUnit) {
		return lowercasePluralNameMap.get(timeUnit);
	}

	private TimeUnitsEx() {
	}
}
