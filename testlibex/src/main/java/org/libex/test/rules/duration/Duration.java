package org.libex.test.rules.duration;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Credit for the code in package is given to Nelson Llewellyn.
 */
@ThreadSafe
public @interface Duration {
	TestDuration duration() default TestDuration.SHORT;
}
