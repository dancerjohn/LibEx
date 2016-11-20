package org.libex.spring.spel;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Joiner;

@ParametersAreNonnullByDefault
@NotThreadSafe
public class SpelBuilder {

    public static SpelBuilder createSpel()
    {
        return new SpelBuilder(true);
    }

    public static SpelBuilder createSpelNoBraces()
    {
        return new SpelBuilder(false);
    }

    private static final Joiner joiner = Joiner.on(' ')
            .useForNull("null");

    private final boolean withBraces;
    private final List<String> parts = newArrayList();

    private SpelBuilder(
            final boolean withBraces) {
        super();
        this.withBraces = withBraces;
    }

    public SpelBuilder with(
            final String part)
    {
        this.parts.add(part);
        return this;
    }

    public String buildSpel()
    {
        String join = joiner.join(parts);

        if (withBraces) {
            return SpelUtils.spelOf(join);
        } else {
            return join;
        }
    }

    public static class MethodBuilder {

        public static MethodBuilder method(
                final String name)
        {
            return new MethodBuilder(name);
        }

        private final String name;
        private final List<String> arguments = newArrayList();

        private MethodBuilder(
                final String name) {
            this.name = name;
        }

        public MethodBuilder argument(
                final String argument)
        {
            arguments.add(argument);
            return this;
        }

        public MethodBuilder typeArgument(
                final Class<?> argument)
        {
            arguments.add(SpelUtils.type(argument));
            return this;
        }

        public MethodBuilder constantArgument(
                final Class<?> type,
                final String name)
        {
            arguments.add(SpelUtils.constant(type, name));
            return this;
        }

        public String buildMethod()
        {
            return SpelUtils.method(name, toArray(arguments, String.class));
        }
    }

    public static class Chain {

        public static Chain chain(
                final String firstPart)
        {
            return new Chain(firstPart);
        }

        private static final Joiner joiner = Joiner.on('.')
                .skipNulls();

        private final List<String> chain = newArrayList();

        private Chain(
                final String firstPart) {
            chain.add(firstPart);
        }

        public Chain dot(
                final String part)
        {
            chain.add(part);
            return this;
        }

        public String buildChain()
        {
            return joiner.join(chain);
        }
    }
}
