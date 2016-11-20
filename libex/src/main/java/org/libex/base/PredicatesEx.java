package org.libex.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.lang3.Validate;
import org.hamcrest.Matcher;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class PredicatesEx {
    
    public static <T> Predicate<T> matches(final Matcher<?> matcher) {
        checkNotNull(matcher);
        return new Predicate<T>() {

            @Override
            public boolean apply(final T input) {
                return matcher.matches(input);
            }};
    }

    /**
     * Converts the passed Guava {@code Predicate} to a {@code java.unit.function.Predicate}
     * 
     * @param <T>
     *            the {@code Predicate} input parameter type
     * @param input
     *            the Guava {@code Predicate} to convert
     * @return the passed {@code Predicate} wrapped as a {@code java.unit.function.Predicate}
     */
    public static <T> java.util.function.Predicate<T> fromGuava(final 
            Predicate<T> input)
    {
        checkNotNull(input);
        return o -> input.apply(o);
    }

    /**
     * Converts the passed Guava {@code Predicate} to a {@code java.unit.function.Predicate}
     * 
     * @param <T>
     *            the {@code Predicate} input parameter type
     * @param input
     *            the Guava {@code Predicate} to convert
     * @return the passed {@code Predicate} wrapped as a {@code java.unit.function.Predicate}
     */
    public static <T> java.util.function.Predicate<T> toJava(
            final Predicate<T> input)
    {
        checkNotNull(input);
        return o -> input.apply(o);
    }

    /**
     * Converts the passed Java {@code java.unit.function.Predicate} to a Guava {@code Predicate}
     * 
     * @param <T>
     *            the {@code Predicate} input parameter type
     * @param input
     *            the Java {@code java.unit.function.Predicate} to convert
     * @return the passed {@code java.unit.function.Predicate} wrapped as a Guava {@code Predicate}
     */
    public static <T> Predicate<T> fromJava(
            final java.util.function.Predicate<T> input)
    {
        checkNotNull(input);
        return o -> input.test(o);
    }

    /**
     * Converts the passed Java {@code java.unit.function.Predicate} to a Guava {@code Predicate}
     * 
     * @param <T>
     *            the {@code Predicate} input parameter type
     * @param input
     *            the Java {@code java.unit.function.Predicate} to convert
     * @return the passed {@code java.unit.function.Predicate} wrapped as a Guava {@code Predicate}
     */
    public static <T> Predicate<T> toGuava(
            final java.util.function.Predicate<T> input)
    {
        checkNotNull(input);
        return o -> input.test(o);
    }

    @Deprecated
    public static <T> java.util.function.Predicate<T> not(
            final java.util.function.Predicate<T> input)
    {
        checkNotNull(input);
        return toJava(Predicates.not(toGuava(input)));
    }

    @SafeVarargs
    @Deprecated
    public static <T> java.util.function.Predicate<T> or(
            final java.util.function.Predicate<T>... predicates)
    {
        Validate.notEmpty(predicates);
        Iterable<Predicate<T>> next = transform(newArrayList(predicates), (p -> toGuava(p)));

        return toJava(Predicates.or(next));
    }

    public static <A, B> java.util.function.Predicate<A> compose(
            final java.util.function.Predicate<B> predicate,
            final java.util.function.Function<A, ? extends B> function)
    {
        checkNotNull(predicate);
        checkNotNull(function);

        return (
                final A a) -> predicate.test(function.apply(a));
    }

    private PredicatesEx() {
    }

}
