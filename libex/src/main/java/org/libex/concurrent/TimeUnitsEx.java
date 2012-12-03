package org.libex.concurrent;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableMap;

/**
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public final class TimeUnitsEx {

	private static final ImmutableMap<TimeUnit, String> lowercaseSingularNameMap;
	private static final ImmutableMap<TimeUnit, String> lowercasePluralNameMap;

	static {
		ImmutableMap.Builder<TimeUnit, String> pluralBuilder = ImmutableMap.builder();
		ImmutableMap.Builder<TimeUnit, String> singlarBuilder = ImmutableMap.builder();

		for (TimeUnit timeUnit : TimeUnit.values()) {
			String pluralName = timeUnit.name().toLowerCase();

			pluralBuilder.put(timeUnit, pluralName);
			singlarBuilder.put(timeUnit, pluralName.substring(0, pluralName.length() - 1));
		}

		lowercaseSingularNameMap = singlarBuilder.build();
		lowercasePluralNameMap = pluralBuilder.build();
	}

	@Nonnull
	public static String toString(long duration, TimeUnit timeUnit) {
		return String.format("%d %s", duration, toLowercase(timeUnit, duration == 1L));
	}

	@Nonnull
	public static String toLowercase(TimeUnit timeUnit, boolean singular) {
		if (singular) {
			return toLowercaseSingular(timeUnit);
		} else {
			return toLowercasePlural(timeUnit);
		}
	}

	@Nonnull
	public static String toLowercaseSingular(TimeUnit timeUnit) {
		return lowercaseSingularNameMap.get(timeUnit);
	}

	@Nonnull
	public static String toLowercasePlural(TimeUnit timeUnit) {
		return lowercasePluralNameMap.get(timeUnit);
	}

	private TimeUnitsEx() {
	}
}
