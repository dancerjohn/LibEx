package org.libex.hamcrest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeMatcher;

/**
 * A {@link TypeSafeMatcher} for {@link String} values against a regular expression
 * 
 * @author jjbutl3
 *
 */
public class RegularExpressionMatcher extends TypeSafeMatcher<String> {

    private enum MatchType {
        MATCHES, CONTAINS;
    }

    /**
     * Creates a Matcher that matches strings where the passed regex can
     * be found in (using find) the string.
     * 
     * @param regex
     *            the regular expression
     * @return the Matcher
     */
    @Factory
    public static RegularExpressionMatcher contains(final String regex)
    {
        return new RegularExpressionMatcher(regex, MatchType.CONTAINS);
    }

    /**
     * Creates a Matcher that matches strings where the passed regex matches (using matches) the string.
     * 
     * @param regex
     *            the regular expression
     * @return the Matcher
     */
    @Factory
    public static RegularExpressionMatcher matches(final String regex)
    {
        return new RegularExpressionMatcher(regex, MatchType.MATCHES);
    }

    private final String regex;
    private final Pattern pattern;
    private final MatchType matchType;

    private RegularExpressionMatcher(final String regex, final MatchType matchType)
    {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
        this.matchType = matchType;
    }

    @Override
    public boolean matchesSafely(final String item)
    {
        if (item == null) {
            return false;
        }

        boolean result;

        Matcher matcher = pattern.matcher(item);
        if (matchType == MatchType.MATCHES) {
            result = matcher.matches();
        } else {
            result = matcher.find();
        }
        return result;
    }

    @Override
    public void describeTo(final Description description)
    {
        description.appendText("a string matching the regular expression \"" + regex + "\"");
    }
}
