package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.instanceOf;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Optional;

@ParametersAreNonnullByDefault
@ThreadSafe
public class IsThrowable<T extends Throwable> extends BaseMatcher<Object> {

	/**
	 * Creates a {@link Matcher} of {@link Throwable} instances that matches
	 * instances of the specified type
	 * 
	 * @param type
	 *            the exception type to check for
	 * @return a new {@link Matcher}
	 */
	public static <T extends Throwable> IsThrowable<T> isThrowableOfType(
			Class<T> type) {
		return new IsThrowable<T>(Optional.of(type), Optional.<String> absent());
	}

	/**
	 * Creates a {@link Matcher} of {@link Throwable} instances that matches
	 * instances whose message contains the passed string
	 * 
	 * @param substring
	 *            the string for which to check in throwable
	 * @return a new {@link Matcher}
	 */
	public static IsThrowable<Throwable> isThrowableWithMessage(String substring) {
		return new IsThrowable<Throwable>(Optional.<Class<Throwable>> absent(),
				Optional.of(substring));
	}

	/**
	 * Creates a {@link Matcher} of {@link Throwable} instances that matches
	 * instances of the specified type whose message contains the passed string
	 * 
	 * @param type
	 *            the exception type to check for
	 * @param substring
	 *            the string for which to check in throwable
	 * @return a new {@link Matcher}
	 */
	public static <T extends Throwable> IsThrowable<T> isThrowable(
			Class<T> type, String substring) {
		return new IsThrowable<T>(Optional.of(type), Optional.of(substring));
	}

	@Nullable
	private final Matcher<Object> type;
	@Nullable
	private final Matcher<String> substring;

	private IsThrowable(Optional<Class<T>> type, Optional<String> substring) {
		super();
		this.type = type.isPresent() ? instanceOf(type.get()) : null;
		this.substring = substring.isPresent() ? Matchers.containsString(substring
				.get()) : null;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("A throwable");
		if (type != null) {
			description.appendText(" of type matching ");
			description.appendDescriptionOf(type);
		}

		if (substring != null) {
			if (type != null) {
				description.appendText("and");
			}
			description.appendText(" with message matching ");
			description.appendDescriptionOf(substring);
		}
	}

	@Override
	public boolean matches(Object arg0) {
		boolean result = arg0 instanceof Throwable;
		if (result) {
			Throwable t = (Throwable) arg0;
			if (type != null) {
				result &= type.matches(t);
			}

			if (substring != null) {
				if (arg0 == null) {
					result = false;
				} else {
					result &= substring.matches(t.getMessage());
				}
			}
		}

		return result;
	}

}
