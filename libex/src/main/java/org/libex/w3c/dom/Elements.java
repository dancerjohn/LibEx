package org.libex.w3c.dom;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Function;
import com.google.common.primitives.Ints;

/**
 * Utility methods on {@link Element}
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public final class Elements {

    private static final Function<Node, Element> TO_ELEMENT = new Function<Node, Element>() {

        @Override
        public Element apply(
                final Node input)
        {
            return Element.class.cast(input);
        }
    };

    /**
     * @return a {@link Function} that will cast a provided {@link Node} to a {@link Element}. The {@link Function} will
     *         return {@code null} if passed {@code null} and will throw a {@code ClassCastException} if the passed
     *         {@link Node} is not an instance of {@link Element}
     */
    public static Function<Node, Element> toElement()
    {
        return TO_ELEMENT;
    }

    /**
     * Creates a Function that returns the specified attribute {@link Node} of the provided {@link Element}
     * 
     * @param attributeName
     *            the name of the attribute that the returned function should retrieve
     * @return the created Function
     */
    public static Function<Element, Attr> toAttributeNode(
            final String attributeName)
    {
        return new Function<Element, Attr>() {

            @Override
            public Attr apply(
                    final Element input)
            {
                return input.getAttributeNode(attributeName);
            }
        };
    }

    /**
     * Get an attribute from a node and specify a default value if the attribute
     * does not exist.
     * 
     * @param element
     *            The element to get the attribute from
     * @param attributeName
     *            The name of the attribute to be retrieved
     * @param defaultValue
     *            The default value to return if the attribute doesn't exist
     * @return The value of the attribute or the default value if the attribute
     *         doesn't exist
     */
    @Nullable
    public static String getAttribute(
            final Element element,
            final String attributeName,
            @Nullable final String defaultValue)
    {
        String result = element.getAttribute(attributeName);
        return result.isEmpty() ? defaultValue : result.trim();
    }

    /**
     * Get an integer attribute from a node and specify a default value if the attribute
     * does not exist or cannot be parsed to an Integer
     * 
     * @param element
     *            The element to get the attribute from
     * @param attributeName
     *            The name of the attribute to be retrieved
     * @param defaultValue
     *            The default value to return if the attribute doesn't exist
     * @return The value of the attribute or the default value if the attribute
     *         doesn't exist or cannot be parsed to an integer
     */
    @Nullable
    public static Integer getIntegerAttribute(
            final Element element,
            final String attributeName,
            @Nullable final Integer defaultValue)
    {
        String result = element.getAttribute(attributeName);
        Integer resultInt = (result == null) ? null : Ints.tryParse(result);
        return resultInt != null ? resultInt : defaultValue;
    }

    private Elements() {
    }
}
