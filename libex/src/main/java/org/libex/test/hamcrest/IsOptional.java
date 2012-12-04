package org.libex.test.hamcrest;

import static org.hamcrest.CoreMatchers.equalTo;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.base.Optional;

@ParametersAreNonnullByDefault
@ThreadSafe
@Immutable
public class IsOptional<T> extends TypeSafeMatcher<Optional<T>> {

	@Factory
	public static <T> IsOptional<T> absent() {
		return new IsOptional<T>(false);
	}

	@Factory
	public static <T> IsOptional<T> present() {
		return new IsOptional<T>(true);
	}

	@Factory
	public static <T> IsOptional<T> presentContaining(T value) {
		return new IsOptional<T>(true, equalTo(value));
	}

	@Factory
	public static <T> IsOptional<T> presentMatching(Matcher<T> value) {
		return new IsOptional<T>(true, value);
	}

	private final boolean present;
	private final Optional<Matcher<T>> matcher;

	private IsOptional(boolean present) {
		super();
		this.present = present;
		this.matcher = Optional.absent();
	}

	private IsOptional(boolean present, Matcher<T> matcher) {
		super();
		this.present = present;
		this.matcher = Optional.of(matcher);
	}

	@Override
	public boolean matchesSafely(Optional<T> item) {
		boolean result = item != null && present == item.isPresent();

		if (result && present && matcher.isPresent()) {
			result = matcher.get().matches(item.get());
		}

		return result;
	}

	@Override
	public void describeTo(Description description) {
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