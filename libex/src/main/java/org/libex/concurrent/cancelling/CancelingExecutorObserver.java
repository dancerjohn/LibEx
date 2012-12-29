package org.libex.concurrent.cancelling;

import java.util.concurrent.Callable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author John Butler
 * 
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public interface CancelingExecutorObserver<T> {

	public void onTaskCanceled(Callable<? extends T> task);
}
