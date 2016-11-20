package org.libex.test.exampleinput;

import java.io.File;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.Ignore;

import com.google.common.collect.ImmutableMap;

@ParametersAreNonnullByDefault
@ThreadSafe
@Ignore
public class TestExampleInput {

    // No Variable examples

    public static ExampleInput NO_VARIABLE_CLASSPATH_EXAMPLE = new DefaultExampleInput<String>(
            "example-input/exampleInput1.xml",
            InputTypeFunctions.classpathResourceToString());

    public static ExampleInput NO_VARIABLE_FILE_PATH_EXAMPLE = new DefaultExampleInput<File>(
            new File("/home/Desktop/crmSample.xml"),
            InputTypeFunctions.fileToString());

    public static DefaultModifiableExampleInput<String> WITH_VARIABLE_EXAMPLE_1 = new DefaultModifiableExampleInput<String>(
            "example-input/exampleInput2.xml",
            ImmutableMap.of(
                    "source", "",
                    "", "blah"),
            VelocityEngineConversionFunction.classpathResourceInstance());

    // /////////////////////////////////////////////
    // Classpath Velocity Examples

    public static ModifiableExampleInput WITH_VARIABLE_EXAMPLE_2 = new DefaultModifiableExampleInput<String>(
            WITH_VARIABLE_EXAMPLE_1,
            ImmutableMap.of("source", ""));

    public static ModifiableExampleInput WITH_VARIABLE_EXAMPLE_3 = WITH_VARIABLE_EXAMPLE_2.copy()
            .override("", "")
            .build();

    public static DefaultModifiableExampleInput<String> WITH_INFILE_DEFAULT_EXAMPLE = new DefaultModifiableExampleInput<String>(
            "example-input/exampleInputWithDefault.xml",
            ImmutableMap.of(
                    "source", ""),
            VelocityEngineConversionFunction.classpathResourceInstance());

    public static ModifiableExampleInput WITH_INFILE_DEFAULT_EXAMPLE_2 = WITH_INFILE_DEFAULT_EXAMPLE.copy()
            .override("", "")
            .build();

    // /////////////////////////////////////////////
    // File System Velocity Examples

    public static DefaultModifiableExampleInput<String> FILE_PATH_EXAMPLE = new DefaultModifiableExampleInput<String>(
            "/home/Desktop/crmSample.xml",
            ImmutableMap.of(
                    "source", "",
                    "", "blah"),
            VelocityEngineConversionFunction.rootFilepathResourceInstance());

    // /////////////////////////////////////////////
    // Thyme Leaf Examples

    public static DefaultModifiableExampleInput<String> THYME_EXAMPLE_1 = new DefaultModifiableExampleInput<String>(
            "example-input/thymeExampleInputWithDefault.xml",
            ImmutableMap.of(
                    "source", "",
                    "", "blah"),
            ThymeLeafConversionFunction.classpathResourceInstance());

    public static DefaultModifiableExampleInput<String> THYME_EXAMPLE_2 = new DefaultModifiableExampleInput<String>(
            "/home/Desktop/blah.xml",
            ImmutableMap.of(
                    "source", "",
                    "", "blah"),
            ThymeLeafConversionFunction.filepathResourceInstance());
}
