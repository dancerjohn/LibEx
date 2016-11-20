package org.libex.base;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBaseLocal;

@RunWith(Theories.class)
public class StringsExTest extends TestBaseLocal {

    @DataPoints
    public static final String[] input = new String[]{"", null, "5", "f", "sdfhsdf234234jfsdlkjfhs"};

    @Theory
    public void testLength(String input){
        int result = StringsEx.length().apply(input);

        assertThat(result, is((input == null) ? 0 : input.length()));
    }

    @Test
    public void testContains() {
        final String string = "this is a test";
        final String isContained = "test";
        final String isNotContained = "not there";

        assertThat(StringsEx.contains(isContained).apply(string), is(true));
        assertThat(StringsEx.contains(isNotContained).apply(string), is(false));
    }

}
