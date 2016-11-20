package org.libex.concurrent;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
@FunctionalInterface
public interface RuntimeExceptionCallable<T, Ex extends RuntimeException> {

    T call() throws Ex;
}
