package org.libex.test.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker to be used as a JUnit Category to indicate that a test is considered
 * an integration test.
 * 
 * @author John Butler
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegrationTest {

}
