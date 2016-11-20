package org.libex.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the test method as needing to be repeated if it failed. This must be used in
 * conjunction with the {@link org.libex.test.rules.RepeatFailingTest} rule.
 * 
 * @author John Butler
 * 
 * @see org.libex.test.rules.RepeatFailingTest
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RepeatIfFailed {

    public static final int DEFAULT_MAX_REPEATS = 1;
    public static final int DEFAULT_DELAY_MS = 0;

    /**
     * The maximum number of times the test method should be repeated if it failed. Must
     * be greater than 0. Default is 1.
     * 
     * @return The maximum number of times the test method should be repeated if it failed
     */
    int maxRepeats() default DEFAULT_MAX_REPEATS;

    /**
     * The amount of time to wait between retries in milliseconds.
     * 
     * @return amount of time to wait between retries in milliseconds.
     */
    int delayInMS() default DEFAULT_DELAY_MS;
}
