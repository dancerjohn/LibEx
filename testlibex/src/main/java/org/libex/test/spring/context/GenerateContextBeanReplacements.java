package org.libex.test.spring.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a method in a test class for use in generating beans for
 * injection into a Spring context. The methods marked by this annotation should
 * take no arguments and return a {@code Map<String, Object>}.
 * <p>
 * 
 * <p>
 * Basic usage:
 * 
 * <pre>
 * &#064;GenerateContextBeanReplacements
 * public Map&lt;String, Object&gt; myFactoryMethod() {
 * 	Map&lt;String, Objects&gt; map = new HashMap&lt;String, Object&gt;();
 * 	map.put(&quot;beanName&quot;, beanInstance);
 * 	return map;
 * }
 * </pre>
 * <p>
 * 
 * @author John Butler
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateContextBeanReplacements {

}
