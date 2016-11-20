package org.libex.w3c.dom;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Utility methods on Attributes {@link Attr}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class Attributes {

    private static final Function<Node, Attr> TO_ATTRIBUTE = new Function<Node, Attr>() {

        @Override
        public Attr apply(
                final Node input)
        {
            return Attr.class.cast(input);
        }
    };
    
    /**
     * @return a {@link Function} that will cast a provided {@link Node} to a {@link Attr}. The {@link Function} will
     *         return {@code null} if passed {@code null} and will throw a {@code ClassCastException} if the passed
     *         {@link Node} is not an instance of {@link Attr}
     */
    public static Function<Node, Attr> toAttribute()
    {
        return TO_ATTRIBUTE;
    }

    private static final Function<Attr, String> TO_VALUE = new Function<Attr, String>() {

        @Override
        public String apply(
                final Attr input)
        {
            return (input == null) ? null : input.getValue();
        }
    };

    /**
     * @return a {@link Function} that return the value of the passed {@link Attr}. The {@link Function} will
     *         return {@code null} if passed {@code null}.
     */
    public static Function<Attr, String> toValue()
    {
        return TO_VALUE;
    }

    public static Predicate<Attr> isOwnedBy(
            final Element element)
    {
        return new Predicate<Attr>() {

            @Override
            public boolean apply(
                    final Attr input)
            {
                return input != null && input.getOwnerElement() == element;
            }
        };
    }

    private Attributes() {
    }
}
