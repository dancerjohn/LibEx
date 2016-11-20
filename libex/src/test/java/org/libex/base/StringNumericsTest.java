package org.libex.base;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.libex.primitives.tiny.integer.NaturalInteger;
import org.libex.test.TestBaseLocal;

@RunWith(Enclosed.class)
public class StringNumericsTest {

    @RunWith(Enclosed.class)
    public static class LimitDecimalPlacesTest {

        @RunWith(Parameterized.class)
        public static class LimitDecimalPlacesTest_ValidValuesWith2Decimals extends TestBaseLocal {

            @Parameters
            public static Collection<Object[]> getData()
            {
                return Arrays.asList(new Object[][] {
                        new Object[] { null, null },
                        new Object[] { "", "" },
                        new Object[] { "  ", "  " },
                        new Object[] { "123123", "123123" },
                        new Object[] { "123123.1", "123123.1" },
                        new Object[] { "123123.12", "123123.12" },
                        new Object[] { "123123.123", "123123.12" },
                        new Object[] { "123123.123132132", "123123.12" },
                        new Object[] { "-123123", "-123123" },
                        new Object[] { "-123123.123", "-123123.12" }
                });
            }

            private final String input, output;

            public LimitDecimalPlacesTest_ValidValuesWith2Decimals(String input, String output) {
                super();
                this.input = input;
                this.output = output;
            }

            @Test
            public void testLimitDecimalPlaces()
            {
                assertThat(StringNumerics.limitDecimalPlaces(input, NaturalInteger.of(2)), equalTo(output));
            }
        }

        public static class LimitDecimalPlacesTest_MainTests extends TestBaseLocal {

            @Test(expected = NullPointerException.class)
            public void testNulls()
            {
                StringNumerics.limitDecimalPlaces("asd", null);
            }
        }
    }
}
