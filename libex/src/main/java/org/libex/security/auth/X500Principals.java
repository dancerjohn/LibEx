package org.libex.security.auth;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;
import javax.security.auth.x500.X500Principal;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class X500Principals {

    private static final Function<String, X500Principal> TO_X500_PRINCIPAL = new Function<String, X500Principal>() {

        @Override
        public X500Principal apply(
                final String input)
        {
            return new X500Principal(input);
        }
    };

    public static Function<String, X500Principal> toX500Principal()
    {
        return TO_X500_PRINCIPAL;
    }

    private X500Principals() {
    }
}
