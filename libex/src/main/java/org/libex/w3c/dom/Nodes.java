package org.libex.w3c.dom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class Nodes {

    public static String toString(
            final Node node)
    {
        Source xmlSource = new DOMSource(node);
        return Documents.transform(xmlSource, false, 0);
    }

    public static String toIndentedString(
            final Node node)
    {
        Source xmlSource = new DOMSource(node);
        return Documents.transform(xmlSource, true, 2);
    }

    public static boolean isChildOf(
            final Element parent,
            final Node potentialChild)
    {
        if (parent == potentialChild) {
            return true;
        } else if (potentialChild == null) {
            return false;
        } else {
            Node node = (potentialChild instanceof Attr)
                    ? ((Attr) potentialChild).getOwnerElement()
                    : potentialChild.getParentNode();
            return isChildOf(parent, node);
        }
    }

    public static Predicate<Node> isSubNodeOf(
            final Element element)
    {
        return new Predicate<Node>() {

            @Override
            public boolean apply(
                    final Node input)
            {
                if (input == element) {
                    return false;
                } else {
                    return isChildOf(element, input);
                }
            }
        };
    }

    public static Predicate<Node> isSameInstanceOrSubNodeOf(
            final Element element)
    {
        return new Predicate<Node>() {

            @Override
            public boolean apply(
                    final Node input)
            {
                if (input == element) {
                    return true;
                } else {
                    return isChildOf(element, input);
                }
            }
        };
    }

    private static final Function<Node, String> TO_TEXT_CONTENT =
            new Function<Node, String>() {

                @Override
                @Nullable
                public String apply(
                        final Node resultNode)
                {
                    String value = (resultNode == null) ? null : resultNode.getTextContent();
                    value = Strings.emptyToNull(value);
                    return value;
                }
            };

    /**
     * Function that takes a Node and returns the Text of a Node
     * 
     * @return the text of a Node
     * 
     */
    public static Function<Node, String> toTextContent()
    {
        return TO_TEXT_CONTENT;
    }

    @CheckForNull
    public static <T extends Node> T getNextSiblingOfType(
            final Node node,
            final Class<T> type)
    {
        checkNotNull(type, "type");

        if (node == null) {
            return null;
        }

        Node sibling = node.getNextSibling();
        if (sibling != null &&
                !type.isInstance(sibling)) {
            sibling = sibling.getNextSibling();
        }

        return type.cast(sibling);
    }

    /**
     * Get the text value of the passed Element as an integer and specify a default value if the value
     * does not exist or cannot be parsed to an Integer
     * 
     * @param element
     *            The element to get the attribute from
     * @param defaultValue
     *            The default value to return if the attribute doesn't exist
     * @return The value of the attribute or the default value if the attribute
     *         doesn't exist or cannot be parsed to an integer
     */
    @Nullable
    public static Integer getIntegerValue(
            final Element element,
            @Nullable final Integer defaultValue)
    {
        String result = TO_TEXT_CONTENT.apply(element);
        Integer resultInt = (result == null) ? null : Ints.tryParse(result);
        return resultInt != null ? resultInt : defaultValue;
    }

    /**
     * Creates a {@link Predicate} that matches instances of {@link Node} whose name is the one provided
     * 
     * @param nodeName
     *            the name
     * @return a {@link Predicate} that matches instances of {@link Node} whose name is the one provided
     */
    public static java.util.function.Predicate<Node> localNameIs(
            final String nodeName)
    {
        checkArgument(!isNullOrEmpty(nodeName));

        return (
                final Node node) -> node.getLocalName().equals(nodeName);
    }

    private Nodes() {
    }

}
