package org.libex.aop;

import static com.google.common.base.Preconditions.*;

import java.util.concurrent.Callable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.libex.concurrent.Caller;

/**
 * Converts an Around pointcut into a Callable and passes it to a configured
 * Callable executor for execution.
 * <p/>
 * This class can be used in conjuction with
 * {@link org.libex.concurrent.profile.Profiler} to profile method calls.
 * 
 * NOTE: The AspectJ-RT (aspectjrt) dependancy needed to compile this class has
 * been included with provided scope. In order to use this class, this
 * dependancy must be provided by the user at run-time.
 * 
 * @author John Butler
 * 
 */
public class ProceedingJoinPointToCallable {

	private final Caller caller;

	/**
	 * @param caller
	 *            the object responsible for invoking the join point callable
	 * @throws NullPointerException
	 *             if caller is null
	 */
	public ProceedingJoinPointToCallable(Caller caller) {
		super();
		this.caller = checkNotNull(caller);
	}

	/**
	 * Accepts a join point, wraps it inside a {@link Callable} and invokes the
	 * {@code caller} to execute the join point.
	 * 
	 * @param pjp
	 *            the join point to be executed
	 * @return the result of invoking the join point
	 * @throws Throwable
	 *             if the execution of the join point throws
	 * @throws NullPointerException
	 *             if pjp is null
	 */
	public Object doBasicProfiling(final ProceedingJoinPoint pjp) throws Throwable {
		checkNotNull(pjp);

		Callable<Object> callable = new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				try {
					return pjp.proceed();
				} catch (Exception e) {
					throw e;
				} catch (Throwable t) {
					throw new Exception(t);
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
