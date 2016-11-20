package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.equalTo;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Optional;

/**
 * Provides Matchers for Optional instances
 * 
 * @param <T>
 *            the type Optional to match
 */
@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
public class IsOptional<T> extends TypeSafeMatcher<Optional<T>> {

	    /**
     * @param <T>
     *            the type Optional to match
     * @return a Matcher that matches an Optional that is not present ( {@code isPresent} returns false
     */
	@Factory
	public static <T> IsOptional<T> absent() {
		return new IsOptional<T>(false);
	}

	        /**
     * NOTE: the parameter is ONLY used for providing a type-specific generic to
     * remove compile-time warnings. It does not validate the type of the
     * contained data at runtime as at runtime the Optional should not contain
     * any data.
     * 
     * @param <T>
     *            the type Optional to match
     * @param type
     *            the type
     * @return an Matcher that matches an Optional that is present (isPresent
     *         returns false)
     */
	@Factory
	public static <T> IsOptional<T> absent(final Class<T> type) {
		return new IsOptional<T>(false);
	}

	    /**
     * @param <T>
     *            the type Optional to match
     * @return a Matcher that matches an Optional that is present ( {@code isPresent} returns true
     */
	@Factory
	public static <T> IsOptional<T> present() {
		return new IsOptional<T>(true);
	}

	        /**
     * NOTE: the parameter is ONLY used for providing a type-specific generic to
     * remove compile-time warnings. It does not validate the type of the
     * contained data at runtime. To do this use {@link #presentMatching(Matcher)} passing the
     * {@code CoreMatchers.instanceOf} matcher.
     * 
     * @param <T>
     *            the type Optional to match
     * @param type
     *            the type
     * @return an Matcher that matches an Optional that is present (isPresent
     *         returns true)
     */
	@Factory
	public static <T> IsOptional<T> present(final Class<? extends T> type) {
		return new IsOptional<T>(true);
	}

	    /**
     * @param <T>
     *            the type Optional to match
     * @param value
     *            the value to test for
     * @return a Matcher that matches an Optional that is present and whose
     *         value is equal to the passed value
     */
	@Factory
	public static <T> IsOptional<T> presentContaining(final T value) {
		return new IsOptional<T>(true, equalTo(value));
	}

	    /**
     * @param <T>
     *            the type Optional to match
     * @param matcher
     *            the matcher to use against the value contained in the Optional
     * @return a Matcher that matches an Optional that is present and whose
     *         value matches the passed matcher
     */
	@Factory
	public static <T> IsOptional<T> presentMatching(final Matcher<T> matcher) {
		return new IsOptional<T>(true, matcher);
	}

	    /**
     * Creates an absent or present IsOptional based on the null state of the
     * passed value. If the value is null, an absent IsOptional is returned. If
     * non-null, a present IsOptional matching the value is returned.
     * 
     * @param <T>
     *            the type Optional to match
     * @param value
     *            the value to test for
     * @return an absent or present IsOptional based on the null state of the
     *         passed value
     * 
     * @see #absent()
     * @see #presentContaining(Object)
     */
	@Factory
	public static <T> IsOptional<T> ofNullableValue(@Nullable final T value) {
		if (value == null) {
			return new IsOptional<T>(false);
		} else {
			return presentContaining(value);
		}
	}

	private final boolean present;
	private final Optional<Matcher<T>> matcher;

	private IsOptional(final boolean present) {
		super();
		this.present = present;
		this.matcher = Optional.absent();
	}

	private IsOptional(final boolean present, final Matcher<T> matcher) {
		super();
		this.present = present;
		this.matcher = Optional.of(matcher);
	}

	@Override
	public boolean matchesSafely(final Optional<T> item) {
		boolean result = item != null && present == item.isPresent();

		if (result && present && matcher.isPresent()) {
			result = matcher.get().matches(item.get());
		}

		return result;
	}

	@Override
	public void describeTo(final Description description) {
		if (present) {
			description.appendText("a present Optional");

			if (matcher.isPresent()) {
				description.appendText(" containing ").appendDescriptionOf(matcher.get());
			}
		} else {
			description.appendText("an absent Optional");
		}
	}
}