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
public @interface Repeat {
	int count();
}
