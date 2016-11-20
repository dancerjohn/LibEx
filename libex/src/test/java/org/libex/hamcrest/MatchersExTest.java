package org.libex.hamcrest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.hamcrest.number.OrderingComparison;
import org.junit.Before;
import org.junit.Test;
import org.libex.base.StringsEx;
import org.libex.test.TestBaseLocal;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Function;

public class MatchersExTest extends TestBaseLocal {

    @Mock
    private Function<Object, Object> function;

    @Mock
    private Matcher<Object> matcher;

    private Matcher<Object> composeMatcher;
    private final Object input = new Object(),
            output = new Object();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(function.apply(input)).thenReturn(output);
        when(matcher.matches(output)).thenReturn(true);

        composeMatcher = MatchersEx.compose(function, matcher);
    }

    @Test
    public void testNulls() {
        nullPointerTester.testAllPublicStaticMethods(MatchersEx.class);
    }

    @Test
    public void testCompose() {
        // test
        boolean result = composeMatcher.matches(input);

        // verify
        verify(function).apply(input);
        verify(matcher).matches(output);
        assertThat(result, is(true));
    }

    @Test
    public void testCompose_functionReturnsNull() {
        // test
        boolean result = composeMatcher.matches("");

        // verify
        verify(function).apply("");
        verify(matcher).matches(null);
        assertThat(result, is(false));
    }

    Matcher<String> lengthGreaterThan2 = MatchersEx.compose(StringsEx.length(),
            OrderingComparison.greaterThan(2));
    
    @Test
    public void testCompose_actualFalse() {
        assertThat("a", not(lengthGreaterThan2));
    }

    @Test
    public void testCompose_actualFalse2() {
        assertThat("ab", not(lengthGreaterThan2));
    }

    @Test
    public void testCompose_actualNull() {
        assertThat(null, not(lengthGreaterThan2));
    }

    @Test
    public void testCompose_actualTrue() {
        assertThat("a23", lengthGreaterThan2);
    }

    @Test
    public void testCompose_describeTo() {
        try {
        assertThat("a", lengthGreaterThan2);
            fail();
        } catch (AssertionError ae) {
            assertThat(ae.getMessage(),
                    containsString("a value that when transformed by " + StringsEx.length().toString()));
        }
    }
}
