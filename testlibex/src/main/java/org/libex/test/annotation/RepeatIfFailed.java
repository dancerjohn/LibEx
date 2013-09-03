package org.libex.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the test method as needing to be repeated. This must be used in
 * conjunction with the {@link org.libex.test.rules.RepeatTest} rule.
 * 
 * @author John Butler
 * 
 * @see org.libex.test.rules.RepeatTest
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatIfFailed {
	/**
	 * @return the number of times the method should be repeated if failed, must
	 *         be greater than 0. Default is 1.
	 */
	int maxRepeats() default 1;

	/**
	 * @return the amount of time to wait between retries in milliseconds
	 */
	int delayInMs() default 0;
}
