package org.libex.test.theories.suppliers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indication that a test supplier should return the results as a single
 * PotentialAssigment containing a list of elements.
 * 
 * @author John Butler
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ReturnAsList {

}
