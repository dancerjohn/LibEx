package org.libex.primitives;

import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Supplier;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class UUIDsEx {

    private static final Supplier<UUID> RANDOM_UUID_SUPPLIER = new Supplier<UUID>() {

        @Override
        public UUID get()
        {
            return UUID.randomUUID();
        }
    };

    /**
     * @return a {@link Supplier} that returns a randomly generated UUID each time the {@link Supplier} is invoked.
     */
    public static Supplier<UUID> randomUuidSupplier()
    {
        return RANDOM_UUID_SUPPLIER;
    }

    private UUIDsEx() {
    }

}
