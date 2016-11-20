package org.libex.primitives.tiny.integer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;
import org.junit.Test;

public abstract class IntegerTinyTypeTest {

    protected abstract IntegerTinyType<?> createFrom(int value);

    protected abstract int[] getValidValues();

    protected abstract int[] getInvalidValues();

    @Test
    public void testValidValues()
    {
        for (int value : getValidValues()) {
            IntegerTinyType<?> type = createFrom(value);
            assertThat(type.getInt(), is(value));
            assertThat(type.get(), is(value));
        }
    }

    @Test
    public void testInvalidValues()
    {
        for (int value : getInvalidValues()) {
            try {
                createFrom(value);
                Assert.fail("Expected exception with " + value);
            } catch (RuntimeException e) {
            }
        }
    }
}
