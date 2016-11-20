package org.libex.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class InputStreamsEx {

    private static final Function<InputStream, String> AS_STRING = new Function<InputStream, String>() {

        @Override
        public String apply(
                final InputStream input)
        {
            return asString(input);
        }
    };

    public static Function<InputStream, String> asString()
    {
        return AS_STRING;
    }
    
    public static String asString(final InputStream stream) {
        
        try (InputStream myStream = stream) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(myStream, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private InputStreamsEx() {
    }

}
