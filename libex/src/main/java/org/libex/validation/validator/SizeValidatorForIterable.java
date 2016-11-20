package org.libex.validation.validator;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

public class SizeValidatorForIterable implements ConstraintValidator<Size, Iterable<?>> {
    private int min;
    private int max;
    
    @Override
    public void initialize(final Size constraintAnnotation)
    {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(final Iterable<?> iterable, final ConstraintValidatorContext context)
    {
        if( iterable == null ) {
            return true;
        } else if (iterable instanceof Collection) {
            int size = ((Collection<?>) iterable).size();
            return size >= min && size <= max;
        }

        boolean result = true;
        int foundSize = 0;
        Iterator<?> iterator = iterable.iterator();

        if (min != 0) {
            while (foundSize <= min && iterator.hasNext()) {
                foundSize++;
                iterator.next();
            }
            result = foundSize >= min;
        }

        if (result && max < Integer.MAX_VALUE) {
            while (foundSize <= max && iterator.hasNext()) {
                foundSize++;
                iterator.next();
            }
            result = foundSize <= min;
        }

        return result;
    }
}
