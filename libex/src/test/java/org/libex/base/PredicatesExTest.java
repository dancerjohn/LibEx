package org.libex.base;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.libex.test.TestBaseLocal;

import com.google.common.base.Predicate;

public class PredicatesExTest extends TestBaseLocal {

    private static final Matcher<Integer> matcher = is(5);
    @DataPoint
    public static final Predicate<Object> obPred = PredicatesEx.matches(matcher);
    @DataPoint
    public static final Predicate<Integer> intPred = PredicatesEx.matches(matcher);

    @Test
    public void testNulls() {
        nullPointerTester.testAllPublicStaticMethods(PredicatesEx.class);
    }

    @Test
    public void testMatches_obTrue() {
        assertThat(obPred.apply(5), is(true));
    }

    @Test
    public void testMatches_obFalse() {
        assertThat(obPred.apply(6), is(false));
    }

    @Test
    public void testMatches_intTrue() {
        assertThat(intPred.apply(5), is(true));
    }

    @Test
    public void testMatches_intFalse() {
        assertThat(intPred.apply(6), is(false));
    }

    @Test
    public void testMatches_obFalseOb() {
        assertThat(obPred.apply(""), is(false));
    }

    @Test
    public void testMatches_null() {
        assertThat(obPred.apply(null), is(false));
    }
}
