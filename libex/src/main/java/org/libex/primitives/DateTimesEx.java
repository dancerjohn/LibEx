package org.libex.primitives;

import java.util.Date;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class DateTimesEx {

    private static final Function<DateTime, Date> TO_DATE = new Function<DateTime, Date>() {

        @Override
        public Date apply(
                final DateTime input)
        {
            return toDate(input);
        }
    };

    public static Function<DateTime, Date> toDate()
    {
        return TO_DATE;
    }

    @Nullable
    public static Date toDate(
            @Nullable final DateTime dateTime)
    {
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.toDate();
        }
    }

    private DateTimesEx() {
    }

}
