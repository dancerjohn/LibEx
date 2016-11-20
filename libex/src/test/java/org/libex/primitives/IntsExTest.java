package org.libex.primitives;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;

public class IntsExTest {

    @Before
    public void setUp() throws Exception {
    }

    private Function<Integer, Integer> mod3 = IntsEx.modulus(3);

    @Test
    public void testModulus() {
        assertThat(mod3.apply(null), is(0));
        assertThat(mod3.apply(0), is(0));
        assertThat(mod3.apply(1), is(1));
        assertThat(mod3.apply(2), is(2));
        assertThat(mod3.apply(3), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModulus_0() {
        IntsEx.modulus(0);
    }

    @Test
    public void testModulus_negative() {
        mod3 = IntsEx.modulus(-3);
        assertThat(mod3.apply(null), is(0));
        assertThat(mod3.apply(0), is(0));
        assertThat(mod3.apply(1), is(1));
        assertThat(mod3.apply(2), is(2));
        assertThat(mod3.apply(3), is(0));
    }

}
