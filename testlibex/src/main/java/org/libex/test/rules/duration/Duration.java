package org.libex.test.rules.duration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Credit for the code in package is given to Nelson Llewellyn.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface Duration {
	TestDuration value() default TestDuration.SHORT;
}
