package org.libex.spring.spel;

import com.google.common.base.Joiner;


/**
 * Provides utility methods based on the {@code Simple} language
 */
public final class SpelUtils {

    private static final String SPEL_FORMAT = "#{%s}";
    private static final String METHOD_FORMAT = "%s(%s)";
    private static final String TYPE_FORMAT = "T(%s)";

    private static final Joiner joiner = Joiner.on(',')
            .useForNull("null");

    public static final String type(
            final Class<?> type)
    {
        return String.format(TYPE_FORMAT, type.getName());
    }

    public static final String constant(
            final Class<?> type,
            final String name)
    {
        return String.format(TYPE_FORMAT + ".%s", type.getName(), name);
    }

    public static final String enumConstant(
            final Enum<?> enumValue)
    {
        return constant(enumValue.getClass(), enumValue.name());
    }

    public static final String method(
            final String methodName,
            final String... arguments)
    {
        String argumentString = joiner.join(arguments);
        return String.format(METHOD_FORMAT, methodName, argumentString);
    }

    public static final String spelOf(final String input){
        return String.format(SPEL_FORMAT, input);
    }

    private SpelUtils() {
    }
}
