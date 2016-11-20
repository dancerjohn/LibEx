package org.libex.test.exampleinput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Path;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Throwables;

/**
 * Provides a set of Utility Functions for use in DefaultExampleInput
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class InputTypeFunctions {

    private static final Function<Path, String> pathToString = new Function<Path, String>() {

        @Override
        public String apply(
                final Path input)
        {
            try {
                return FileUtils.readFileToString(input.toFile());
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    };

    public static Function<Path, String> pathToString()
    {
        return pathToString;
    }

    private static final Function<File, String> fileToString = new Function<File, String>() {

        @Override
        public String apply(
                final File input)
        {
            try {
                return FileUtils.readFileToString(input);
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    };

    public static Function<File, String> fileToString()
    {
        return fileToString;
    }

    private static final Function<String, InputStream> classpathResourceInputStream = new Function<String, InputStream>() {

        @Override
        public InputStream apply(
                final String input)
        {
            try {
                Resource path = new ClassPathResource(input);
                InputStream inputStream = path.getInputStream();
                return inputStream;
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    };

    public static Function<String, String> classpathResourceToString()
    {
        return Functions.compose(inputStreamToString, classpathResourceInputStream);
    }

    private static final Function<InputStream, String> inputStreamToString = new Function<InputStream, String>() {

        @Override
        public String apply(
                final InputStream input)
        {
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(input, writer);
                return writer.toString();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    };

    private InputTypeFunctions() {
    }
}
