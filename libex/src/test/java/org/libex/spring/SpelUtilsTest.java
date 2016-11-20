package org.libex.spring;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.libex.spring.spel.SpelUtils;
import org.libex.test.TestBaseLocal;

public class SpelUtilsTest extends TestBaseLocal {

    private static enum MyEnum {
        Value1, Value2;
    }

    @Test
    public void testType()
    {
        assertThat(SpelUtils.type(Date.class), equalTo("T(java.util.Date)"));
    }

    @Test
    public void testEnum()
    {
        assertThat(SpelUtils.enumConstant(MyEnum.Value1), equalTo("T(org.libex.spring.SpelUtilsTest$MyEnum).Value1"));
    }
}
