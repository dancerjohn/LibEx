package org.libex.primitives;

import java.util.Date;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class DatesEx {

    private static final Function<Date, Long> TO_TIME = new Function<Date, Long>() {

        @Override
        public Long apply(
                final Date input)
        {
            return input == null ? null : input.getTime();
        }
    };

    public static final Function<Date, Long> toTime()
    {
        return TO_TIME;
    }

    private static final Supplier<Date> SYSTEM_DATE_SUPPLIER = new Supplier<Date>() {

        @Override
        public Date get()
        {
            return new Date();
        }
    };

    public static final Supplier<Date> getSystemDateSupplier()
    {
        return SYSTEM_DATE_SUPPLIER;
    }

    private DatesEx() {
    }

}
