package org.libex.test.exampleinput.expected;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import ognl.OgnlException;

import org.junit.Test;
import org.libex.base.StringsEx;
import org.libex.test.exampleinput.expected.DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput;
import org.libex.test.exampleinput.expected.TestModifiableExampleInputWithExpectedValues.RootValues;
import org.libex.test.exampleinput.expected.TestModifiableExampleInputWithExpectedValues.RootValues2;
import org.libex.test.exampleinput.expected.TestModifiableExampleInputWithExpectedValues.SubClass1;
import org.libex.test.exampleinput.expected.TestModifiableExampleInputWithExpectedValues.SubClass21;

import com.google.common.collect.Maps;

public class ModifiableExampleInputWithExpectedValuesTest {

    @Test
    public void testWithFluent() throws OgnlException
    {
        RootValues rv = new RootValues()
                .class1(
                new SubClass1()
                        .value1("blah"));

        OgnlExpectedValueUpdater updater =
                OgnlExpectedValueUpdater.formatSetUsingGetValueOgnlExpression("class1().value1(%s)")
                        .build();

        ExpectedValueConverterInput<RootValues> input = ExpectedValueConverterInput.<RootValues> builder()
                .expectedValue(rv)
                .currentEntry(Maps.immutableEntry("sfhsdf", "someNewValue"))
                .build();

        updater.update(input);
        System.out.println(rv);
        assertThat(rv.class1().value1(), equalTo("someNewValue"));
    }

    @Test
    public void testWithSetter() throws OgnlException
    {
        RootValues2 rv = new RootValues2()
                .setClass1(
                new SubClass21()
                        .setValue1("blah"));

        String expression = "class1.value1";
        OgnlExpectedValueUpdater updater =
                OgnlExpectedValueUpdater.createSetValueOgnlWithExpression(expression)
                        .setValueConverter(StringsEx.toUpperCase())
                        .build();

        ExpectedValueConverterInput<RootValues2> input = ExpectedValueConverterInput.<RootValues2> builder()
                .expectedValue(rv)
                .currentEntry(Maps.immutableEntry("sfhsdf", "someNewValue"))
                .build();

        updater.update(input);
        System.out.println(rv);
        assertThat(rv.getClass1().getValue1(), equalTo("someNewValue".toUpperCase()));
    }
}
