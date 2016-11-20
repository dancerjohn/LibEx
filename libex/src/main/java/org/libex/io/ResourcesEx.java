package org.libex.io;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class ResourcesEx {

    private static final Function<String, Resource> TO_CLASSPATH_RESOURCE = new Function<String, Resource>() {

        @Override
        public Resource apply(
                final String path)
        {
            return getClassPathResource(path);
        }
    };
    
    public static Resource getClassPathResource(
            final String path)
    {

        try {
            Resource re = new ClassPathResource(path);
            return re;
        } catch (Exception e) {
            throw new RuntimeException("path not found: " + path, e);
        }
    }

    public static Function<String, Resource> toClasspathResource()
    {
        return TO_CLASSPATH_RESOURCE;
    }

    private static Function<Resource, InputStream> TO_INPUT_STREAM = new Function<Resource, InputStream>() {

        @Override
        public InputStream apply(
                final Resource input)
        {
            try {
                return input.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Function<Resource, InputStream> toInputStream()
    {
        return TO_INPUT_STREAM;
    }

    private ResourcesEx() {
    }

}
