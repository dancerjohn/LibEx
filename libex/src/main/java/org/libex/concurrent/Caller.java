package org.libex.concurrent;

import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@ParametersAreNonnullByDefault
public interface Caller {

	<T> T call(Callable<T> callable) throws Exception;
}
