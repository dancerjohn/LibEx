package org.libex.test.exampleinput;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Ignore
public class ExampleInputTest {

    @Test
    public void noVariableTests()
    {
        System.out.println(TestExampleInput.NO_VARIABLE_CLASSPATH_EXAMPLE.getInput());
        System.out.println(TestExampleInput.NO_VARIABLE_FILE_PATH_EXAMPLE.getInput());
    }

    @Test
    public void testVelocityExamples()
    {
        System.out.println(TestExampleInput.WITH_VARIABLE_EXAMPLE_1.getInput());
        System.out.println(TestExampleInput.WITH_VARIABLE_EXAMPLE_2.getInput());
        System.out.println(TestExampleInput.WITH_VARIABLE_EXAMPLE_3.getInput());

        System.out.println(TestExampleInput.FILE_PATH_EXAMPLE.getInput());
    }

    @Test
    public void testOverrides()
    {
        System.out.println(TestExampleInput.WITH_VARIABLE_EXAMPLE_3.getInputUsing(
                ImmutableMap.of("source", "MYNEWSOURCE")));

        System.out.println(
                TestExampleInput.WITH_VARIABLE_EXAMPLE_3.copy()
                        .override("source", "MYNEWSOURCE")
                        .build());
    }

    @Test
    public void testInFileDefaults()
    {
        System.out.println(TestExampleInput.WITH_INFILE_DEFAULT_EXAMPLE.getInput());
        System.out.println(TestExampleInput.WITH_INFILE_DEFAULT_EXAMPLE_2.getInput());
    }

    @Test
    public void testThymeDefaults()
    {
        System.out.println(TestExampleInput.THYME_EXAMPLE_1.getInput());

        Map<String, String> map = Maps.newHashMap();
        map.put("source", null);

        System.out.println(TestExampleInput.THYME_EXAMPLE_1.getInputUsing(map));
    }

    @Test
    public void testThymeDefaults2()
    {
        System.out.println(TestExampleInput.THYME_EXAMPLE_2.getInput());
    }
}
