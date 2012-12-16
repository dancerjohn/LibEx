package org.libex.aop;

import java.util.concurrent.Callable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.libex.concurrent.Caller;

/**
 * NOTE: The AspectJ-RT (aspectjrt) dependancy needed to compile this class has
 * been included with provided scope. In order to use this class, this
 * dependancy must be provided by the user at run-time.
 * 
 * @author John Butler
 * 
 */
public class ProceedingJoinPointToCallable {

	private final Caller caller;

	public ProceedingJoinPointToCallable(Caller caller) {
		super();
		this.caller = caller;
	}

	public Object doBasicProfiling(final ProceedingJoinPoint pjp) throws Throwable {
		Callable<Object> callable = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				try {
					return pjp.proceed();
				} catch (Exception e) {
					throw e;
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}

			@Override
			public String toString() {
				return pjp.getTarget().toString();
			}
		};

		return caller.call(callable);
	}
}
