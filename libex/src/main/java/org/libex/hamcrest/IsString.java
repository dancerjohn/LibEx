package org.libex.hamcrest;

import java.util.regex.Pattern;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Implements a String matching {@link Matcher} that checks the input matches a
 * provided regular expression.
 * 
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class IsString {

	/**
	 * Creates a {@link Matcher} that checks the input matches a provided
	 * regular expression.
	 * 
	 * @param regularExpression
	 *            the regular expression to match against
	 * @return a {@link Matcher} that checks the input matches a provided
	 *         regular expression.
	 * @throws NullPointerException
	 *             if regularExpression is null
	 */
	public static TypeSafeMatcher<String> matchingRegularExpression(String regularExpression) {
		return new RegularExpressionMatcher(regularExpression);
	}

	private final static class RegularExpressionMatcher extends TypeSafeMatcher<String> {

		private final String regularExpression;
		private final Pattern pattern;

		private RegularExpressionMatcher(String regularExpression) {
			this.regularExpression = regularExpression;
			this.pattern = Pattern.compile(regularExpression);
		}

		@Override
		public void describeTo(Description arg0) {
			arg0.appendText("regular expression: " + regularExpression);
		}

		@Override
		protected boolean matchesSafely(String arg0) {
			return pattern.matcher(arg0).matches();
		}
	}

}
