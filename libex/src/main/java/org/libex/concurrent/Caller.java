package org.libex.concurrent;

import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Implements the ability to execute a {@link Callable} possible on the invoking
 * thread.
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public interface Caller {

	/**
	 * Causes the callable to be invoked.
	 * 
	 * @param callable
	 *            the callable
	 * @return the value returned by the callable
	 * @throws Exception
	 *             any exception thrown by the callable
	 */
	<T> T call(Callable<T> callable) throws Exception;
}
