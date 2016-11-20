package org.libex.test.spring.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to provide classpath xml context files that should be used to replace beans
 * configured in the standard application context.:
 * <p>
 * <b>locations:</b> Array of classpath xml context files
 * <p>
 * Basic usage:
 *
 * <pre>
 * &#064;ReplacementBeanContextConfiguration(locations = {&quot;classpath:myReplacementBeanContext.xml&quot;})
 * </pre>
 * <p>
 *
 * @author John Butler
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReplacementBeanContextConfiguration {
    String[] locations();
}
