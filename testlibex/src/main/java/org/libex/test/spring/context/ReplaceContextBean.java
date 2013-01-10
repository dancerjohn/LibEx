package org.libex.test.spring.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a field to be injected into a Spring context. There are two
 * optional fields:
 * <p>
 * <b>name:</b> the name of the bean in the Spring context (generally this is
 * the name of an existing bean to be replaced). If this attribute is not set
 * the name of the field will be used as the bean name.
 * <p>
 * <b>factoryMethod:</b> method to be invoked to retrieve the instance to be
 * used as the bean. If this is not set, the field's instance is used.
 * <p>
 * 
 * <p>
 * Basic usage:
 * 
 * <pre>
 * &#064;ReplaceContextBean(name = &quot;class1&quot;)
 * public Int1 other = new Class2();
 * 
 * &#064;ReplaceContextBean(name = &quot;class12&quot;, factoryMethod = &quot;createOther2&quot;)
 * public Int1 other2;
 * </pre>
 * <p>
 * 
 * @author John Butler
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReplaceContextBean {
	String name() default "";

	String factoryMethod() default "";
}
