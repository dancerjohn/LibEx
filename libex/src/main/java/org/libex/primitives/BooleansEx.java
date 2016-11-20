package org.libex.primitives;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class BooleansEx {

    private static final Function<String, Boolean> PARSE_BOOLEAN = new Function<String, Boolean>() {

        @Override
        public Boolean apply(final String input)
        {
            if (input == null) {
                return null;
            }

            return Boolean.parseBoolean(input);
        }
    };

    public static final Function<String, Boolean> parseBoolean()
    {
        return PARSE_BOOLEAN;
    }

    private BooleansEx()
    {
    }
}
