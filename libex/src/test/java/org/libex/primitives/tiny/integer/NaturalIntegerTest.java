package org.libex.primitives.tiny.integer;

public class NaturalIntegerTest extends IntegerTinyTypeTest {

    @Override
    protected IntegerTinyType<?> createFrom(int value)
    {
        return NaturalInteger.of(value);
    }

    @Override
    protected int[] getValidValues()
    {
        return new int[] { 0, 1, 2, Integer.MAX_VALUE };
    }

    @Override
    protected int[] getInvalidValues()
    {
        return new int[] { -1, -2, Integer.MIN_VALUE };
    }

}
