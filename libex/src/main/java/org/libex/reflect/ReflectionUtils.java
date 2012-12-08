package org.libex.reflect;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Reflection utilities.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class ReflectionUtils {

	/**
	 * Gets the list of Methods in the passed type that have all of the passed
	 * predicates.
	 * 
	 * @param type
	 *            the class in which to search
	 * @param annotations
	 *            the annotation(s) to search for
	 * @return a list of methods in {@code type} and contain all the annotations
	 *         in {@code annotations}
	 * 
	 * @throws NullPointerException
	 *             if type is null or any annotation in annotations is null
	 */
	@Nonnull
	public static List<Method> getMethodsWithAnnotations(Class<?> type, Class<? extends Annotation>... annotations) {
		Predicate<Method> predicate = containsAllAnnotations(annotations);
		List<Method> unfilteredMethods = newArrayList(type.getMethods());
		Iterable<Method> filteredMethods = filter(unfilteredMethods, predicate);
		return newArrayList(filteredMethods);
	}

	/**
	 * Creates a Predicate that matches methods that contain all the passed
	 * annotations.
	 * 
	 * @param annotations
	 *            the list of annotations to match
	 * @return a Predicate that matches methods that contain all the passed
	 *         annotations
	 * 
	 * @throws NullPointerException
	 *             if any annotation in annotations is null
	 */
	public static Predicate<Method> containsAllAnnotations(Class<? extends Annotation>... annotations) {
		Predicate<Method> predicate = Predicates.alwaysTrue();

		for (Class<? extends Annotation> annotation : annotations) {
			predicate = Predicates.and(predicate, containsAnnotation(annotation));
		}
		return predicate;
	}

	/**
	 * Creates a Predicate that matches methods that contain the passed
	 * annotation.
	 * 
	 * @param annotation
	 *            the annotation to match
	 * @return a Predicate that matches methods that contain the passed
	 *         annotation
	 * 
	 * @throws NullPointerException
	 *             if any annotation is null
	 */
	public static Predicate<Method> containsAnnotation(final Class<? extends Annotation> annotation) {
		checkNotNull(annotation, "annotation cannot be null");
		return new Predicate<Method>() {

			@Override
			public boolean apply(@Nullable Method method) {
				return method.getAnnotation(annotation) != null;
			}
		};
	}

	private ReflectionUtils() {
	}
}
