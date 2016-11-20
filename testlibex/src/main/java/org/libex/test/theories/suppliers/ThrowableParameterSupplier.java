package org.libex.test.theories.suppliers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParametersSuppliedBy;

public class ThrowableParameterSupplier extends AbstractParameterSupplier<Class<? extends Throwable>> {

    /**
     * Provides the supplied list of {@link Throwable}s
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.PARAMETER })
    @ParametersSuppliedBy(ThrowableParameterSupplier.class)
    public @interface Exceptions {

        Class<? extends Throwable>[] value() default {};

    }

    @Override
    public List<Class<? extends Throwable>> getTestValues(final ParameterSignature sig)
    {
        final List<Class<? extends Throwable>> values = new ArrayList<Class<? extends Throwable>>();
        final Exceptions exceptionAnnotation = sig.getAnnotation(Exceptions.class);
        if (exceptionAnnotation != null) {
            values.addAll(Arrays.asList(exceptionAnnotation.value()));
        }
        return values;
    }

    @Override
    protected String toKey(final Class<? extends Throwable> record)
    {
        return record.getSimpleName();
    }

}
